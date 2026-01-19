package com.abhijeet.airbnb_backend.dto;

import lombok.Data;

@Data
public class LocationRequest {
    private String name;
    private Long countryId;
}
