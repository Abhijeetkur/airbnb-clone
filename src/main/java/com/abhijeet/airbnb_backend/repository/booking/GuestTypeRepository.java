package com.abhijeet.airbnb_backend.repository.booking;

import com.abhijeet.airbnb_backend.entity.booking.GuestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestTypeRepository extends JpaRepository<GuestType, Long> {
    Optional<GuestType> findByTypeName(String typeName);
}
