package com.abhijeet.airbnb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentRatingRequest {
    private Long componentId;
    private Integer rating;
}
