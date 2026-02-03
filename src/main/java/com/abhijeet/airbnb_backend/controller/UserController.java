package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.ApiResponse;
import com.abhijeet.airbnb_backend.dto.LoginRequest;
import com.abhijeet.airbnb_backend.dto.UserResponse;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody User user) {
        UserResponse savedUserDto = userService.registerNewUser(user);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // We call the service to ensure we return a clean DTO
        UserResponse response = userService.getUserById(currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/become-host")
    public ResponseEntity<ApiResponse<Void>> upgradeToHost(
            @AuthenticationPrincipal User currentUser,
            jakarta.servlet.http.HttpServletRequest request) {

        User freshUser = userService.findById(currentUser.getId());

        boolean isAlreadyHost = freshUser.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getRoleName().equalsIgnoreCase("HOST"));

        if (isAlreadyHost) {
            return ResponseEntity
                    .ok(new ApiResponse<>("Welcome Back to your dashboard!", true, LocalDateTime.now().toString()));
        }

        User updatedUser = userService.assignRole(currentUser.getId(), "HOST");

        // 1. Create new Auth with fresh authorities (ROLE_USER, ROLE_HOST)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                auth.getCredentials(),
                updatedUser.getAuthorities());

        // 2. Update the Context Holder
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // 3. CRITICAL: Persist this change to the HTTP Session
        // Without this, the next request will revert to ROLE_USER
        jakarta.servlet.http.HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return ResponseEntity.ok(new ApiResponse<>("You are now a Host!", true, LocalDateTime.now().toString()));
    }

}
