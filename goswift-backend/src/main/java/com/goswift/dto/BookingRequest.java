package com.goswift.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    private Long userId;        // Changed from Integer to Long
    private Long scheduleId;    // Changed from Integer to Long
    private LocalDate journeyDate;
    private List<PassengerDTO> passengers;
}