package com.abhijeet.airbnb_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
                boolean success,
                String message,
                String timestamp,
                T data) {
        public ApiResponse(String message, boolean success, String timestamp) {
                this(success, message, timestamp, null);
        }

        public ApiResponse(boolean success, String message, T data) {
                this(success, message, LocalDateTime.now().toString(), data);
        }
}
