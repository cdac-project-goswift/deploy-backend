package com.goswift.controller;

import com.goswift.dto.ApiResponse;
import com.goswift.dto.UserDTO;
import com.goswift.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService userService;

    // Test endpoint to verify JWT authentication
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<String>> getCurrentUserProfile(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Authenticated user", "Welcome " + email));
    }

    // Req 23: Profile Update
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Profile updated", userService.updateProfile(userId, userDTO)));
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Profile fetched", userService.getUserProfile(userId)));
    }
}