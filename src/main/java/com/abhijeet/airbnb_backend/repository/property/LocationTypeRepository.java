package com.abhijeet.airbnb_backend.repository.property;

import com.abhijeet.airbnb_backend.entity.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationTypeRepository extends JpaRepository<Location, Long> {

}
