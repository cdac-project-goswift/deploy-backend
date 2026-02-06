package com.goswift.service;

import com.goswift.dto.BookingDTO;
import com.goswift.dto.SystemConfigRequest;
import com.goswift.dto.SystemStatsDTO;
import com.goswift.dto.UserDTO;
import com.goswift.entity.Agency;
import com.goswift.entity.Booking;
import com.goswift.entity.Bus;
import com.goswift.entity.City;
import com.goswift.entity.SystemConfig;
import com.goswift.entity.User;
import com.goswift.enums.UserRole;
import com.goswift.enums.UserStatus;
import com.goswift.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final SystemConfigRepository configRepository;
    private final BookingRepository bookingRepository;
    private final BusRepository busRepository;
    private final AgencyRepository agencyRepository;
    private final ModelMapper mapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> mapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }

    public void updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus(status);
        userRepository.save(user);
    }

    public City addCity(City city) {
        return cityRepository.save(city);
    }
    
    public List<City> getAllCities() { return cityRepository.findAll(); }

    public SystemStatsDTO getSystemStats() {
        return SystemStatsDTO.builder()
                .totalRevenue(bookingRepository.getTotalRevenue())
                .totalBookings(bookingRepository.count())
                .activeBuses(busRepository.count())
                .activeAgents(userRepository.countByRole(UserRole.ROLE_AGENT))
                .build();
    }

    public List<Agency> getAllAgencies() {
        return agencyRepository.findAll();
    }

    public List<Bus> getBusesByAgency(Long agencyId) {
        return busRepository.findByAgency_AgencyId(agencyId);
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toBookingDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> searchBookings(Long agencyId, Long busId) {
        List<Booking> bookings;
        if (busId != null) {
            bookings = bookingRepository.findBySchedule_Bus_BusId(busId);
        } else if (agencyId != null) {
            bookings = bookingRepository.findByAgencyId(agencyId);
        } else {
            bookings = bookingRepository.findAll();
        }
        return bookings.stream()
                .map(this::toBookingDTO)
                .collect(Collectors.toList());
    }

    private BookingDTO toBookingDTO(Booking booking) {
        return BookingDTO.builder()
                .bookingId(booking.getBookingId())
                .bookingRefNo(booking.getBookingRefNo())
                .bookingDate(booking.getBookingDate())
                .journeyDate(booking.getJourneyDate())
                .totalFare(booking.getTotalFare())
                .status(booking.getStatus())
                .user(BookingDTO.UserInfo.builder()
                        .userId(booking.getUser().getUserId())
                        .email(booking.getUser().getEmail())
                        .firstName(booking.getUser().getFirstName())
                        .lastName(booking.getUser().getLastName())
                        .build())
                .schedule(BookingDTO.ScheduleInfo.builder()
                        .scheduleId(booking.getSchedule().getScheduleId())
                        .bus(BookingDTO.BusInfo.builder()
                                .busId(booking.getSchedule().getBus().getBusId())
                                .registrationNo(booking.getSchedule().getBus().getRegistrationNo())
                                .busType(booking.getSchedule().getBus().getBusType().toString())
                                .agency(BookingDTO.AgencyInfo.builder()
                                        .agencyId(booking.getSchedule().getBus().getAgency().getAgencyId())
                                        .agencyName(booking.getSchedule().getBus().getAgency().getAgencyName())
                                        .build())
                                .build())
                        .sourceCity(BookingDTO.CityInfo.builder()
                                .cityId(booking.getSchedule().getSourceCity().getCityId())
                                .cityName(booking.getSchedule().getSourceCity().getCityName())
                                .build())
                        .destCity(BookingDTO.CityInfo.builder()
                                .cityId(booking.getSchedule().getDestCity().getCityId())
                                .cityName(booking.getSchedule().getDestCity().getCityName())
                                .build())
                        .build())
                .build();
    }
    
    public SystemConfig getConfig() { return configRepository.getGlobalConfig(); }
    
    public SystemConfig updateConfig(SystemConfigRequest req) {
        SystemConfig config = configRepository.getGlobalConfig();
        if(config == null) config = new SystemConfig();
        config.setServiceTaxPct(req.getServiceTaxPct());
        config.setBookingFee(req.getBookingFee());
        return configRepository.save(config);
    }
}