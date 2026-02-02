package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.PropertyResponse;
import com.abhijeet.airbnb_backend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PagedResourcesAssembler<PropertyResponse> pagedResourcesAssembler;

    /**
     * GET /api/v1/properties?page=0&size=10
     * Publicly accessible list of all properties
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PropertyResponse>>> getAllProperties(Pageable pageable) {
        Page<PropertyResponse> properties = propertyService.getAllProperties(pageable);

        // This adds the "self", "next", and "previous" links automatically
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(properties));
    }

    /**
     * GET /api/v1/properties/{id}
     * Get detailed info for a specific property
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }
}