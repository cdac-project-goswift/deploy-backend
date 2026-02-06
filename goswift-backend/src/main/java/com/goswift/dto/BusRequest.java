package com.goswift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusRequest {
    @NotBlank
    private String registrationNo;
    
    @NotBlank
    private String busType; // "AC_SEATER" (String from UI)
    
    @NotNull
    private Integer capacity;
}