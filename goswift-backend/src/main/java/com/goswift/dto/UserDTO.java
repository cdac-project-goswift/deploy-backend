package com.goswift.dto;

import com.goswift.enums.UserRole;
import com.goswift.enums.UserStatus;
import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;
}