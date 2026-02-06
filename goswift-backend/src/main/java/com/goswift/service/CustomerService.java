package com.goswift.service;

import com.goswift.dto.*;
import com.goswift.entity.*;
import com.goswift.enums.BusType;
import com.goswift.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;
    private final CityRepository cityRepository;
    private final SystemConfigRepository configRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final FeedbackRepository feedbackRepository;
    private final ModelMapper mapper;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public List<SearchResultDTO> searchBuses(String source, String dest, LocalDate date, String busTypeStr, String sortBy) {
        BusType type = (busTypeStr != null && !busTypeStr.isEmpty()) ? BusType.valueOf(busTypeStr) : null;
        List<Schedule> schedules = scheduleRepository.searchSchedules(source, dest, type);
        
        // Filter out schedules where departure time has passed for today
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        List<SearchResultDTO> results = schedules.stream()
            .filter(s -> {
                // If search date is today, check if departure time hasn't passed
                if (date.equals(today)) {
                    return s.getDepartureTime().isAfter(currentTime);
                }
                return true; // For future dates, show all
            })
            .map(s -> {
                SearchResultDTO dto = mapper.map(s, SearchResultDTO.class);
                dto.setAgencyName(s.getBus().getAgency().getAgencyName());
                dto.setBusType(s.getBus().getBusType().name());
                dto.setSourceCity(s.getSourceCity().getCityName());
                dto.setDestCity(s.getDestCity().getCityName());
                
                Integer bookedCount = bookingRepository.countConfirmedSeats(s.getScheduleId(), date);
                dto.setAvailableSeats(s.getBus().getCapacity() - bookedCount);
                return dto;
            }).collect(Collectors.toList());

        if ("price".equals(sortBy)) results.sort(Comparator.comparing(SearchResultDTO::getBaseFare));
        else if ("time".equals(sortBy)) results.sort(Comparator.comparing(SearchResultDTO::getDepartureTime));
        
        return results;
    }

    @Transactional
    public Booking createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        int passengerCount = request.getPassengers().size();
        Integer bookedSeats = bookingRepository.countConfirmedSeats(schedule.getScheduleId(), request.getJourneyDate());
        
        if (bookedSeats + passengerCount > schedule.getBus().getCapacity()) {
            throw new RuntimeException("Not enough seats available");
        }

        SystemConfig config = configRepository.getGlobalConfig();
        BigDecimal baseTotal = schedule.getBaseFare().multiply(new BigDecimal(passengerCount));
        BigDecimal tax = baseTotal.multiply(config.getServiceTaxPct().divide(new BigDecimal(100)));
        BigDecimal totalFare = baseTotal.add(tax).add(config.getBookingFee());

        Booking booking = Booking.builder()
                .user(user)
                .schedule(schedule)
                .journeyDate(request.getJourneyDate())
                .totalFare(totalFare)
                .status("Confirmed")
                .bookingRefNo("BK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .build();
        
        Booking savedBooking = bookingRepository.save(booking);

        List<Ticket> tickets = request.getPassengers().stream().map(p ->
            Ticket.builder()
                .booking(savedBooking)
                .passengerName(p.getPassengerName())
                .passengerAge(p.getPassengerAge())
                .passengerGender(p.getPassengerGender())
                .seatNo(p.getSeatNo())
                .ticketNo("TK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .build()
        ).collect(Collectors.toList());
        
        ticketRepository.saveAll(tickets);
        return bookingRepository.findById(savedBooking.getBookingId()).orElseThrow();
    }

    public List<Booking> getUserBookings(Long userId, String type) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Booking> allBookings = bookingRepository.findByUser(user);
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if ("upcoming".equalsIgnoreCase(type)) {
            return allBookings.stream()
                    .filter(b -> {
                        LocalDate journeyDate = b.getJourneyDate();
                        LocalTime departureTime = b.getSchedule().getDepartureTime();
                        
                        // Future dates are upcoming
                        if (journeyDate.isAfter(today)) {
                            return true;
                        }
                        // Today's bookings are upcoming only if departure time hasn't passed
                        if (journeyDate.equals(today)) {
                            return departureTime.isAfter(currentTime);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        } else if ("completed".equalsIgnoreCase(type)) {
            return allBookings.stream()
                    .filter(b -> {
                        LocalDate journeyDate = b.getJourneyDate();
                        LocalTime arrivalTime = b.getSchedule().getArrivalTime();
                        
                        // Past dates are completed
                        if (journeyDate.isBefore(today)) {
                            return true;
                        }
                        // Today's bookings are completed only if arrival time has passed
                        if (journeyDate.equals(today)) {
                            return arrivalTime.isBefore(currentTime);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        return allBookings;
    }

    public Feedback addFeedback(FeedbackRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow();
        
        // Check if journey is completed (arrival time has passed)
        LocalDate journeyDate = booking.getJourneyDate();
        LocalTime arrivalTime = booking.getSchedule().getArrivalTime();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        boolean isCompleted = journeyDate.isBefore(today) || 
                             (journeyDate.equals(today) && arrivalTime.isBefore(currentTime));
        
        if (!isCompleted) {
            throw new RuntimeException("Cannot rate upcoming or ongoing trips");
        }
        
        if (feedbackRepository.existsByBooking(booking)) {
            throw new RuntimeException("Feedback already submitted for this booking");
        }
        return feedbackRepository.save(Feedback.builder()
                .booking(booking)
                .rating(request.getRating())
                .comments(request.getComments())
                .build());
    }

    public List<String> getBookedSeats(Long scheduleId, LocalDate date) {
        return ticketRepository.findOccupiedSeats(scheduleId, date);
    }

    public boolean hasFeedback(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        return feedbackRepository.existsByBooking(booking);
    }
}