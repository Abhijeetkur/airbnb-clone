package com.abhijeet.airbnb_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyRequest {
    private String propertyName;
    private Long propertyTypeId;
    private Long placeTypeId;
    private Long locationId;
    private BigDecimal price;
    private int numGuests;
    private int numBedRooms;
    private int numBeds;
    private int numWashRooms;
    private String description;
    private List<Long> amenityIds;

    @jakarta.validation.constraints.NotNull(message = "Images are required.")
    @jakarta.validation.constraints.Size(min = 5, max = 15, message = "At least 5 and at most 15 images must be uploaded.")
    private List<String> imageUrls;
}
