package com.abhijeet.airbnb_backend.repository.property;

import com.abhijeet.airbnb_backend.entity.property.PropertyAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyAmenityRepository extends JpaRepository<PropertyAmenity, Long> {
}
