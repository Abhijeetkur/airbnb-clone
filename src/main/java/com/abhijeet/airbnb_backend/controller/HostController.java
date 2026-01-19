package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.ApiResponse;
import com.abhijeet.airbnb_backend.dto.PropertyRequest;
import com.abhijeet.airbnb_backend.dto.PropertyResponse;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.service.PropertyService;
import com.abhijeet.airbnb_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/host")
public class HostController {
    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService;;
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getHostDashboard(@AuthenticationPrincipal User currentUser) {
        // This will only work if the user has ROLE_HOST
        return ResponseEntity.ok(new ApiResponse(
                "Welcome to the Host Dashboard, " + currentUser.getFname(),
                true,
                LocalDateTime.now().toString()
        ));
    }

    @PostMapping("/properties")
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid @RequestBody PropertyRequest propertyRequest,
            @AuthenticationPrincipal User currentUser){
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }
        PropertyResponse savedProperty = propertyService.addProperty(propertyRequest, currentUser);
        return new ResponseEntity<>(savedProperty,HttpStatus.CREATED);
    }
}
