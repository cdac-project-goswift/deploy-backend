package com.goswift.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class SearchResultDTO {
    private Integer scheduleId;
    private String agencyName;
    private String busType;
    private String sourceCity;
    private String destCity;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private BigDecimal baseFare;
    private Integer availableSeats;
}