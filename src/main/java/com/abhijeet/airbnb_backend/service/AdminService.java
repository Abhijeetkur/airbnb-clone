package com.abhijeet.airbnb_backend.service;

import com.abhijeet.airbnb_backend.entity.location.Country;
import com.abhijeet.airbnb_backend.entity.location.Location;
import com.abhijeet.airbnb_backend.entity.location.Region;
import com.abhijeet.airbnb_backend.entity.property.Amenity;
import com.abhijeet.airbnb_backend.entity.property.AmenityType;
import com.abhijeet.airbnb_backend.entity.property.PlaceType;
import com.abhijeet.airbnb_backend.entity.property.PropertyType;
import com.abhijeet.airbnb_backend.repository.location.CountryRepository;
import com.abhijeet.airbnb_backend.repository.location.LocationRepository;
import com.abhijeet.airbnb_backend.repository.location.RegionRepository;
import com.abhijeet.airbnb_backend.repository.property.AmenityRepository;
import com.abhijeet.airbnb_backend.repository.property.AmenityTypeRepository;
import com.abhijeet.airbnb_backend.repository.property.PlaceTypeRepository;
import com.abhijeet.airbnb_backend.repository.property.PropertyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AmenityTypeRepository amenityTypeRepository;
    @Autowired
    private PropertyTypeRepository propertyTypeRepository;
    @Autowired
    private PlaceTypeRepository placeTypeRepository;
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public Country addCountry(String name, Long regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        Country country = new Country();
        country.setCountryName(name);
        country.setRegion(region);
        return countryRepository.save(country);
    }

    @Transactional
    public Location addLocation(String name, Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        Location location = new Location();
        location.setLocationName(name);
        location.setCountry(country);
        return locationRepository.save(location);
    }

    public Region addRegion(String name) {
        Region region = new Region();
        region.setRegionName(name);
        return regionRepository.save(region);
    }

    public AmenityType addAmenityType(String name) {
        AmenityType type = new AmenityType();
        type.setAmenityName(name);
        return amenityTypeRepository.save(type);
    }

    public PropertyType addPropertyType(String name) {
        PropertyType pt = new PropertyType();
        pt.setTypeName(name);
        return propertyTypeRepository.save(pt);
    }

    public PlaceType addPlaceType(String name) {
        PlaceType pt = new PlaceType();
        pt.setTypeName(name);
        return placeTypeRepository.save(pt);
    }

    @Transactional
    public Amenity addAmenity(String name, Long typeId) {
        AmenityType type = amenityTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Amenity Type not found"));
        Amenity amenity = new Amenity();
        amenity.setAmenityName(name);
        amenity.setAmenityType(type);
        return amenityRepository.save(amenity);
    }

    public Page<Region> getAllRegions(int page, int size, String sortBy, String direction, String name) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        if (name != null && !name.isEmpty()) {
            return regionRepository.findByRegionNameContainingIgnoreCase(name, pageable);
        }

        return regionRepository.findAll(pageable);
    }

    public Page<PropertyType> getAllPropertyTypes(int page, int size, String sortBy, String direction, String typeName) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (typeName != null && !typeName.isEmpty()) {
            return propertyTypeRepository.findByTypeNameContainingIgnoreCase(typeName, pageable);
        }

        return propertyTypeRepository.findAll(pageable);
    }

    public Page<PlaceType> getAllPlaceTypes(int page, int size, String sortBy, String direction, String typeName) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (typeName != null && !typeName.isEmpty()) {
            return placeTypeRepository.findByTypeNameContainingIgnoreCase(typeName, pageable);
        }

        return placeTypeRepository.findAll(pageable);
    }

    public Page<Amenity> getAllAmenities(int page, int size, String sortBy, String direction, String name, Long typeId) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (name != null && typeId != null) {
            return amenityRepository.findByAmenityNameContainingIgnoreCaseAndAmenityTypeId(name, typeId, pageable);
        } else if (name != null) {
            return amenityRepository.findByAmenityNameContainingIgnoreCase(name, pageable);
        } else if (typeId != null) {
            return amenityRepository.findByAmenityTypeId(typeId, pageable);
        }

        return amenityRepository.findAll(pageable);
    }

    public Page<AmenityType> getAllAmenityTypes(int page, int size, String sortBy, String direction, String name) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (name != null && !name.isEmpty()) {
            return amenityTypeRepository.findByAmenityNameContainingIgnoreCase(name, pageable);
        }

        return amenityTypeRepository.findAll(pageable);
    }
}
