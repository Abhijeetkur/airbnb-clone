package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.ApiResponse;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/host")
public class HostController {
    @Autowired
    private UserService userService;
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getHostDashboard(@AuthenticationPrincipal User currentUser) {
        // This will only work if the user has ROLE_HOST
        return ResponseEntity.ok(new ApiResponse(
                "Welcome to the Host Dashboard, " + currentUser.getFname(),
                true,
                LocalDateTime.now().toString()
        ));
    }

}
