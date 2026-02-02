package com.abhijeet.airbnb_backend.repository.property;

import com.abhijeet.airbnb_backend.entity.property.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @EntityGraph(attributePaths = {
            "host",
            "propertyType",
            "placeType",
            "location",
            "location.country",
            "propertyAmenities",
            "propertyAmenities.amenity"
    })
    Page<Property> findAll(Pageable pageable);
}
