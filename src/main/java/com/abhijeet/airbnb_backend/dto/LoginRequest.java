package com.abhijeet.airbnb_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Login identifier is required")
    // This Regex allows: standard email format OR 10-15 digit phone numbers
    @Pattern(regexp = "^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})|(\\d{10,15})$",
            message = "Must be a valid email or a 10-15 digit phone number")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;
}
