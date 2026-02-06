package com.goswift.controller;

import com.goswift.dto.ApiResponse;
import com.goswift.dto.BusRequest;
import com.goswift.dto.ScheduleRequest;
import com.goswift.entity.Booking;
import com.goswift.entity.Bus;
import com.goswift.entity.City;
import com.goswift.entity.Schedule;
import com.goswift.service.AgentService;
import com.goswift.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired private AgentService agentService;
    @Autowired private AdminService adminService;

    // --- Fleet Management ---
    @PostMapping("/{userId}/buses")
    public ResponseEntity<ApiResponse<Bus>> addBus(@PathVariable Long userId, @Valid @RequestBody BusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bus added", agentService.addBus(userId, request)));
    }

    @GetMapping("/{userId}/buses")
    public ResponseEntity<ApiResponse<List<Bus>>> getMyBuses(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Fleet fetched", agentService.getMyBuses(userId)));
    }

    // Req 19: Update/Delete Bus
    @PutMapping("/buses/{busId}")
    public ResponseEntity<ApiResponse<Bus>> updateBus(@PathVariable Long busId, @RequestBody BusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bus updated", agentService.updateBus(busId, request)));
    }

    @DeleteMapping("/buses/{busId}")
    public ResponseEntity<ApiResponse<String>> deleteBus(@PathVariable Long busId) {
        agentService.deleteBus(busId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bus deleted", null));
    }

    // --- Schedule Management ---
    @PostMapping("/{userId}/schedules")
    public ResponseEntity<ApiResponse<Schedule>> addSchedule(@PathVariable Long userId, @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Schedule created", agentService.addSchedule(userId, request)));
    }

    @GetMapping("/{userId}/schedules")
    public ResponseEntity<ApiResponse<List<Schedule>>> getMySchedules(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Schedules fetched", agentService.getMySchedules(userId)));
    }

    // Req 19: Update/Delete Schedule
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ApiResponse<Schedule>> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Schedule updated", agentService.updateSchedule(scheduleId, request)));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<ApiResponse<String>> deleteSchedule(@PathVariable Long scheduleId) {
        agentService.deleteSchedule(scheduleId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Schedule deleted", null));
    }

    @GetMapping("/{userId}/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAgencyBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bookings fetched", agentService.getAgencyBookings(userId)));
    }

    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<City>>> getAllCities() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Cities fetched", adminService.getAllCities()));
    }
}