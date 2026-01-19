package com.abhijeet.airbnb_backend.dto;

import lombok.Data;

@Data
public class CountryRequest {
    private String name;
    private Long regionId;
}
