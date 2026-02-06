package com.goswift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long bookingId;
    private String bookingRefNo;
    private LocalDateTime bookingDate;
    private LocalDate journeyDate;
    private BigDecimal totalFare;
    private String status;
    private UserInfo user;
    private ScheduleInfo schedule;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String email;
        private String firstName;
        private String lastName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleInfo {
        private Long scheduleId;
        private BusInfo bus;
        private CityInfo sourceCity;
        private CityInfo destCity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusInfo {
        private Long busId;
        private String registrationNo;
        private String busType;
        private AgencyInfo agency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgencyInfo {
        private Long agencyId;
        private String agencyName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityInfo {
        private Long cityId;
        private String cityName;
    }
}
