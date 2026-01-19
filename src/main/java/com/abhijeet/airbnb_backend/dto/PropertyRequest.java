package com.abhijeet.airbnb_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropertyRequest {
    private  String propertyName;
    private  Long propertyTypeId;
    private  Long placeTypeId;
    private Long locationId;
    private BigDecimal price;
    private int numGuests;
    private int numBedRooms;
    private int numBeds;
    private int numWashRooms;
    private String description;
    private List<Long> amenityIds;
}
