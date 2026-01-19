package com.abhijeet.airbnb_backend.repository.property;

import com.abhijeet.airbnb_backend.entity.property.PlaceType;
import com.abhijeet.airbnb_backend.entity.property.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceTypeRepository extends JpaRepository<PlaceType, Long> {
    Page<PlaceType> findByTypeNameContainingIgnoreCase(String typeName, Pageable pageable);
}
