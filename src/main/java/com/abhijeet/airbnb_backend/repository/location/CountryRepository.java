package com.abhijeet.airbnb_backend.repository.location;

import com.abhijeet.airbnb_backend.entity.location.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
