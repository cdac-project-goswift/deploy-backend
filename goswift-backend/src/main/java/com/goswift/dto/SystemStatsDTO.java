package com.goswift.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatsDTO {
    private BigDecimal totalRevenue;
    private Long totalBookings;
    private Long activeBuses;
    private Long activeAgents;
}