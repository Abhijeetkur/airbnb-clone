package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.UserResponse;
import com.abhijeet.airbnb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @GetMapping("/dashboard")
    public String getAdminStats() {
        return "Welcome to the Admin Dashboard, " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String role
    ){
        Page<UserResponse> users = userService.getAllUsers(page, size, sortBy, role);
        return ResponseEntity.ok(users);
    }
}
