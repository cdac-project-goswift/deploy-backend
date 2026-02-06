package com.goswift.service;

import com.goswift.dto.BusRequest;
import com.goswift.dto.ScheduleRequest;
import com.goswift.entity.*;
import com.goswift.enums.BusType;
import com.goswift.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final BusRepository busRepository;
    private final ScheduleRepository scheduleRepository;
    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper mapper;

    private Agency getAgencyByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return agencyRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Agency not found for user"));
    }

    public Bus addBus(Long userId, BusRequest request) {
        Agency agency = getAgencyByUserId(userId);
        return busRepository.save(Bus.builder()
                .agency(agency)
                .registrationNo(request.getRegistrationNo())
                .busType(BusType.valueOf(request.getBusType()))
                .capacity(request.getCapacity())
                .build());
    }

    public List<Bus> getMyBuses(Long userId) {
        Agency agency = getAgencyByUserId(userId);
        return busRepository.findByAgency(agency);
    }

    public Bus updateBus(Long busId, BusRequest request) {
        Bus bus = busRepository.findById(busId)
            .orElseThrow(() -> new RuntimeException("Bus not found"));
        
        bus.setRegistrationNo(request.getRegistrationNo());
        bus.setBusType(BusType.valueOf(request.getBusType()));
        bus.setCapacity(request.getCapacity());
        return busRepository.save(bus);
    }

    public void deleteBus(Long busId) {
        busRepository.deleteById(busId);
    }

    @Transactional
    public Schedule addSchedule(Long userId, ScheduleRequest request) {
        Agency agency = getAgencyByUserId(userId);
        Bus bus = busRepository.findById(request.getBusId())
            .orElseThrow(() -> new RuntimeException("Bus not found"));
        
        if (!bus.getAgency().equals(agency)) throw new RuntimeException("Unauthorized");

        boolean overlap = scheduleRepository.existsOverlappingSchedule(
            bus, request.getDepartureTime(), request.getArrivalTime()
        );
        if (overlap) {
            throw new RuntimeException("Bus is already scheduled for this time slot");
        }

        Schedule schedule = mapper.map(request, Schedule.class);
        schedule.setBus(bus);
        schedule.setSourceCity(cityRepository.findByCityName(request.getSourceCity()).orElseThrow());
        schedule.setDestCity(cityRepository.findByCityName(request.getDestCity()).orElseThrow());
        
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getMySchedules(Long userId) {
        Agency agency = getAgencyByUserId(userId);
        return scheduleRepository.findByBus_Agency(agency);
    }

    public Schedule updateSchedule(Long scheduleId, ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        // Update fields
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setBaseFare(request.getBaseFare());
        
        // Update cities if changed
        if (!schedule.getSourceCity().getCityName().equals(request.getSourceCity())) {
            schedule.setSourceCity(cityRepository.findByCityName(request.getSourceCity()).orElseThrow());
        }
        if (!schedule.getDestCity().getCityName().equals(request.getDestCity())) {
            schedule.setDestCity(cityRepository.findByCityName(request.getDestCity()).orElseThrow());
        }
        
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public List<Booking> getAgencyBookings(Long userId) {
        Agency agency = getAgencyByUserId(userId);
        return bookingRepository.findByAgencyId(agency.getAgencyId());
    }
}