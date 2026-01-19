package com.abhijeet.airbnb_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private String propertyName;
    private String description;
    private BigDecimal price;

    // Counts
    private int numGuests;
    private int numBedrooms;
    private int numBeds;
    private int numWashrooms;

    // Flattened names from related entities
    private String hostName;
    private String propertyType;
    private String placeType;
    private String locationDisplay; // e.g., "New York, USA"

    // List of amenity names
    private List<String> amenities;

    private LocalDateTime createdAt;
}