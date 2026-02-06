package com.goswift.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SystemConfigRequest {
    private BigDecimal serviceTaxPct;
    private BigDecimal bookingFee;
}