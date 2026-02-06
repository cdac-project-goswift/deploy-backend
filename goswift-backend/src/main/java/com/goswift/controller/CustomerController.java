package com.goswift.controller;

import com.goswift.dto.ApiResponse;
import com.goswift.dto.BookingRequest;
import com.goswift.dto.SearchResultDTO;
import com.goswift.dto.FeedbackRequest;
import com.goswift.entity.Booking;
import com.goswift.entity.City;
import com.goswift.entity.Feedback;
import com.goswift.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired private CustomerService customerService;

    // Public endpoint for cities (needed for customer search)
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<City>>> getAllCities() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Cities fetched", customerService.getAllCities()));
    }

    // Req 14 & 27: Search with Filters and Sorting
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchResultDTO>>> searchBuses(
            @RequestParam String source,
            @RequestParam String dest,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String busType,
            @RequestParam(required = false) String sortBy) {
        
        List<SearchResultDTO> results = customerService.searchBuses(source, dest, date, busType, sortBy);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Buses found", results));
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<Booking>> bookTicket(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Booking confirmed", customerService.createBooking(request)));
    }

    // Req 15: Upcoming vs Completed
    @GetMapping("/{userId}/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyBookings(
            @PathVariable Long userId,
            @RequestParam(required = false) String type) { // 'upcoming' or 'history'
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Bookings fetched", customerService.getUserBookings(userId, type)));
    }

    // Req 11: Add Feedback
    @PostMapping("/feedback")
    public ResponseEntity<ApiResponse<Feedback>> addFeedback(@RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Feedback submitted", customerService.addFeedback(request)));
    }
    
    // Req 26: Available Seats for Selection
    @GetMapping("/schedule/{scheduleId}/seats")
    public ResponseEntity<ApiResponse<List<String>>> getBookedSeats(@PathVariable Long scheduleId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Fetched occupied seats", customerService.getBookedSeats(scheduleId, date)));
    }

    // Check if feedback exists for a booking
    @GetMapping("/booking/{bookingId}/feedback-exists")
    public ResponseEntity<ApiResponse<Boolean>> checkFeedbackExists(@PathVariable Long bookingId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Checked feedback status", customerService.hasFeedback(bookingId)));
    }
}