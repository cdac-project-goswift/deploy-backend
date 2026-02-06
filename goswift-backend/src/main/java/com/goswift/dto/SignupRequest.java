package com.goswift.dto;

import com.goswift.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String password;
    
    private UserRole role; // ROLE_AGENT or ROLE_CUSTOMER
    private String agencyName; // Optional, only for Agent
}