package com.abhijeet.airbnb_backend.service;

import com.abhijeet.airbnb_backend.dto.PropertyRequest;
import com.abhijeet.airbnb_backend.dto.PropertyResponse;
import com.abhijeet.airbnb_backend.entity.location.Location;
import com.abhijeet.airbnb_backend.entity.property.*;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.repository.property.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PlaceTypeRepository placeTypeRepository;
    @Autowired
    private PropertyAmenityRepository propertyAmenityRepository;
    @Autowired
    private PropertyTypeRepository propertyTypeRepository;
    @Autowired
    private LocationTypeRepository locationRepository;
    @Autowired
    private AmenityRepository amenityRepository;

    @Transactional
    public PropertyResponse addProperty(PropertyRequest propertyRequest, User host) {
        PropertyType propertyType = propertyTypeRepository.findById(propertyRequest.getPropertyTypeId())
                .orElseThrow(() -> new RuntimeException("PropertyType not found"));
        PlaceType placeType = placeTypeRepository.findById(propertyRequest.getPlaceTypeId())
                .orElseThrow(() -> new RuntimeException("Place Type not found"));
        Location location = locationRepository.findById(propertyRequest.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Property property = new Property();
        property.setPropertyName(propertyRequest.getPropertyName());
        property.setPropertyType(propertyType);
        property.setPlaceType(placeType);
        property.setLocation(location);
        property.setPrice(propertyRequest.getPrice());
        property.setNumBedrooms(propertyRequest.getNumBedRooms());
        property.setNumGuest(propertyRequest.getNumGuests());
        property.setNumBeds(propertyRequest.getNumBeds());
        property.setNumWashrooms(propertyRequest.getNumWashRooms());
        property.setDescription(propertyRequest.getDescription());
        property.setHost(host);

        Property savedProperty = propertyRepository.save(property);
        List<String> amenityNamesForResponse = new ArrayList<>();

        if (propertyRequest.getAmenityIds() != null) {
            if (savedProperty.getPropertyAmenities() == null) {
                savedProperty.setPropertyAmenities(new HashSet<>());
            }
            for (Long amenityId : propertyRequest.getAmenityIds()) {
                Amenity amenity = amenityRepository.findById(amenityId)
                        .orElseThrow(() -> new RuntimeException("Amenity not found " + amenityId));
                PropertyAmenity propertyAmenity = new PropertyAmenity();
                propertyAmenity.setPropertyAmenityFkId(new PropertyAmenityFkId(amenityId, savedProperty.getId()));
                propertyAmenity.setProperty(savedProperty);
                propertyAmenity.setAmenity(amenity);
                propertyAmenityRepository.save(propertyAmenity);
                amenityNamesForResponse.add(amenity.getAmenityName());
            }
        }
        PropertyResponse response = mapToResponse(savedProperty);
        response.setAmenities(amenityNamesForResponse);
        return response;
    }

    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));
        return mapToResponse(property);
    }
    public Page<PropertyResponse> getAllProperties(Pageable pageable) {
        // 1. Fetch the paginated entities from DB
        Page<Property> propertyPage = propertyRepository.findAll(pageable);

        // 2. Convert (map) the entities to DTOs
        return propertyPage.map(this::mapToResponse);
    }

    private PropertyResponse mapToResponse(Property property) {
        PropertyResponse dto = new PropertyResponse();

        dto.setId(property.getId());
        dto.setPropertyName(property.getPropertyName());
        dto.setPrice(property.getPrice());
        dto.setNumGuests(property.getNumGuest());
        dto.setNumBedrooms(property.getNumBedrooms());
        dto.setNumBeds(property.getNumBeds());
        dto.setNumWashrooms(property.getNumWashrooms());
        dto.setDescription(property.getDescription());
        dto.setCreatedAt(property.getCreatedAt());

        if (property.getHost() != null) {
            dto.setHostName(property.getHost().getFname() + " " + property.getHost().getLname());
        }

        if (property.getPropertyType() != null) {
            dto.setPropertyType(property.getPropertyType().getTypeName());
        }

        if (property.getPlaceType() != null) {
            dto.setPlaceType(property.getPlaceType().getTypeName());
        }

        if (property.getLocation() != null) {
            String city = property.getLocation().getLocationName();
            String country = property.getLocation().getCountry().getCountryName();
            dto.setLocationDisplay(city + ", " + country);
        }

//        if (property.getPropertyAmenities() != null) {
//            List<String> amenityNames = property.getPropertyAmenities().stream()
//                    .map(pa -> pa.getAmenity().getAmenityName())
//                    .toList();
//            dto.setAmenities(amenityNames);
//        }

        if (property.getPropertyAmenities() != null && !property.getPropertyAmenities().isEmpty()) {
            List<String> names = new ArrayList<>();

            // Use a simple for-each loop instead of a Stream on the proxy collection
            for (PropertyAmenity pa : property.getPropertyAmenities()) {
                if (pa.getAmenity() != null) {
                    names.add(pa.getAmenity().getAmenityName());
                }
            }
            dto.setAmenities(names);
        } else {
            dto.setAmenities(new ArrayList<>()); // Return empty list instead of null
        }
        return dto;
    }
}
