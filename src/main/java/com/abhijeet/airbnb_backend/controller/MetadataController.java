package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.entity.location.Region;
import com.abhijeet.airbnb_backend.entity.property.Amenity;
import com.abhijeet.airbnb_backend.entity.property.AmenityType;
import com.abhijeet.airbnb_backend.entity.property.PlaceType;
import com.abhijeet.airbnb_backend.entity.property.PropertyType;
import com.abhijeet.airbnb_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/metadata")
public class MetadataController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/regions")
    public ResponseEntity<Page<Region>> getAllRegions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String name
    ) {
        Page<Region> regions = adminService.getAllRegions(page, size, sortBy, direction, name);
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/property-types")
    public ResponseEntity<Page<PropertyType>> getAllPropertyTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String typeName // Filter by name
    ) {
        Page<PropertyType> propertyTypes = adminService.getAllPropertyTypes(page, size, sortBy, direction, typeName);
        return ResponseEntity.ok(propertyTypes);
    }

    @GetMapping("/place-types")
    public ResponseEntity<Page<PlaceType>> getAllPlaceTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String typeName
    ) {
        Page<PlaceType> placeTypes = adminService.getAllPlaceTypes(page, size, sortBy, direction, typeName);
        return ResponseEntity.ok(placeTypes);
    }

    @GetMapping("/amenities")
    public ResponseEntity<Page<Amenity>> getAllAmenities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long typeId
    ) {
        Page<Amenity> amenities = adminService.getAllAmenities(page, size, sortBy, direction, name, typeId);
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/amenity-types")
    public ResponseEntity<Page<AmenityType>> getAllAmenityTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(required = false) String name
    ) {
        Page<AmenityType> types = adminService.getAllAmenityTypes(page, size, sortBy, direction, name);
        return ResponseEntity.ok(types);
    }
}
