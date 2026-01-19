package com.abhijeet.airbnb_backend.repository.location;

import com.abhijeet.airbnb_backend.entity.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
