package com.goswift.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long bookingId;
    private Integer rating;
    private String comments;
}