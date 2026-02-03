package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.ApiResponse;
import com.abhijeet.airbnb_backend.dto.ReviewRequest;
import com.abhijeet.airbnb_backend.entity.review.UserReview;
import com.abhijeet.airbnb_backend.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class UserReviewController {

    private final UserReviewService userReviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserReview>> createReview(
            @RequestBody ReviewRequest request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.abhijeet.airbnb_backend.entity.user.User user) {

        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "User not authenticated", null));
        }

        UserReview review = userReviewService.createReview(request, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Review submitted successfully", review));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse<List<UserReview>>> getPropertyReviews(@PathVariable Long propertyId) {
        List<UserReview> reviews = userReviewService.getReviewsByProperty(propertyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reviews fetched successfully", reviews));
    }
}
