package com.abhijeet.airbnb_backend.repository.property;

import com.abhijeet.airbnb_backend.entity.property.AmenityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityTypeRepository extends JpaRepository<AmenityType, Long> {
    Page<AmenityType> findByAmenityNameContainingIgnoreCase(String amenityName, Pageable pageable);
}
