package com.abhijeet.airbnb_backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out must be in the future")
    private LocalDate endDate;

    private Integer numGuests;

    private java.util.List<GuestSelectionDTO> guestSelections;
}