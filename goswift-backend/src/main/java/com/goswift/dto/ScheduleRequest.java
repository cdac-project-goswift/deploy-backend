package com.goswift.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class ScheduleRequest {
    private Long busId; // Changed from Integer to Long
    private String sourceCity;
    private String destCity;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal baseFare;
}