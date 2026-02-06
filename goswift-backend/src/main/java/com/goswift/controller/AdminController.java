package com.goswift.controller;

import com.goswift.dto.ApiResponse;
import com.goswift.dto.BookingDTO;
import com.goswift.dto.SystemConfigRequest;
import com.goswift.dto.SystemStatsDTO;
import com.goswift.dto.UserDTO;
import com.goswift.entity.Agency;
import com.goswift.entity.Bus;
import com.goswift.entity.City;
import com.goswift.entity.SystemConfig;
import com.goswift.enums.UserStatus;
import com.goswift.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Users fetched", adminService.getAllUsers()));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(@PathVariable Long userId, @RequestParam UserStatus status) {
        adminService.updateUserStatus(userId, status);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "User status updated", null));
    }

    // Req 10: Add City
    @PostMapping("/cities")
    public ResponseEntity<ApiResponse<City>> addCity(@RequestBody City city) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "City added", adminService.addCity(city)));
    }
    
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<City>>> getAllCities() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Cities fetched", adminService.getAllCities()));
    }

    // Req 20: System Reports (Separated Users/Agents)
    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<SystemStatsDTO>> getSystemStats() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Stats fetched", adminService.getSystemStats()));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookings() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bookings fetched", adminService.getAllBookings()));
    }

    @GetMapping("/agencies")
    public ResponseEntity<ApiResponse<List<Agency>>> getAllAgencies() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Agencies fetched", adminService.getAllAgencies()));
    }

    @GetMapping("/agencies/{agencyId}/buses")
    public ResponseEntity<ApiResponse<List<Bus>>> getBusesByAgency(@PathVariable Long agencyId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Buses fetched", adminService.getBusesByAgency(agencyId)));
    }

    @GetMapping("/bookings/search")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> searchBookings(
            @RequestParam(required = false) Long agencyId,
            @RequestParam(required = false) Long busId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bookings fetched", adminService.searchBookings(agencyId, busId)));
    }
    
    @PutMapping("/config")
    public ResponseEntity<ApiResponse<SystemConfig>> updateConfig(@RequestBody SystemConfigRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Config updated", adminService.updateConfig(request)));
    }
    
    @GetMapping("/config")
    public ResponseEntity<ApiResponse<SystemConfig>> getConfig() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Config fetched", adminService.getConfig()));
    }
}