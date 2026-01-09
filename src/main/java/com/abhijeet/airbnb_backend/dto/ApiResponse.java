package com.abhijeet.airbnb_backend.dto;

public record ApiResponse(
        String message,
        boolean success,
        String timestamp
) {
}
