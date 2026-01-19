package com.abhijeet.airbnb_backend.repository.location;

import com.abhijeet.airbnb_backend.entity.location.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Page<Region> findByRegionNameContainingIgnoreCase(String regionName, Pageable pageable);
}
