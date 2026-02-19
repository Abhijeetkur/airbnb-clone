package com.abhijeet.airbnb_backend.dto;

import com.abhijeet.airbnb_backend.entity.review.UserReview;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String fname;
    private String lname;
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private String bio;
    private String token;
}
