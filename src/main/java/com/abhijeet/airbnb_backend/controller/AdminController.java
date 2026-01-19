package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.*;
import com.abhijeet.airbnb_backend.entity.location.Country;
import com.abhijeet.airbnb_backend.entity.location.Location;
import com.abhijeet.airbnb_backend.entity.location.Region;
import com.abhijeet.airbnb_backend.entity.property.Amenity;
import com.abhijeet.airbnb_backend.entity.property.AmenityType;
import com.abhijeet.airbnb_backend.entity.property.PlaceType;
import com.abhijeet.airbnb_backend.entity.property.PropertyType;
import com.abhijeet.airbnb_backend.service.AdminService;
import com.abhijeet.airbnb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public String getAdminStats() {
        return "Welcome to the Admin Dashboard, " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String role
    ){
        Page<UserResponse> users = userService.getAllUsers(page, size, sortBy, role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/countries")
    public ResponseEntity<Country> addCountry(@RequestBody CountryRequest countryRequest) {
        return ResponseEntity.ok(adminService.addCountry(countryRequest.getName(), countryRequest.getRegionId()));
    }

    @PostMapping("/locations")
    public ResponseEntity<Location> addLocation(@RequestBody LocationRequest locationRequest) {
        return ResponseEntity.ok(adminService.addLocation(locationRequest.getName(), locationRequest.getCountryId()));
    }

    @PostMapping("/regions")
    public ResponseEntity<Region> addRegion(@RequestBody RegionRequest regionRequest){
        return ResponseEntity.ok(adminService.addRegion(regionRequest.getName()));
    }

    @PostMapping("/amenity-types")
    public ResponseEntity<AmenityType> addAmenityType(@RequestBody AmenityTypeRequest request){
        return ResponseEntity.ok(adminService.addAmenityType(request.getName()));
    }

    @PostMapping("/property-types")
    public ResponseEntity<PropertyType> addPropertyType(@RequestBody NameRequest request) {
        return ResponseEntity.ok(adminService.addPropertyType(request.getName()));
    }

    @PostMapping("/place-types")
    public ResponseEntity<PlaceType> addPlaceType(@RequestBody NameRequest request) {
        return ResponseEntity.ok(adminService.addPlaceType(request.getName()));
    }

    @PostMapping("/amenities")
    public ResponseEntity<Amenity> addAmenity(@RequestBody AmenityRequest amenityRequest) {
        return ResponseEntity.ok(adminService.addAmenity(amenityRequest.getName(), amenityRequest.getTypeId()));
    }
}
