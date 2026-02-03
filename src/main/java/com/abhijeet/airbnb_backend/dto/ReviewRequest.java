package com.abhijeet.airbnb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private Long propertyId;
    private String comment;
    private BigDecimal overallRating;
    private List<ComponentRatingRequest> componentRatings;
}
