package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.BookingRequest;
import com.abhijeet.airbnb_backend.dto.BookingResponse;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> bookProperty(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal User guest) {
        return ResponseEntity.ok(bookingService.createBooking(request, guest));
    }
}
