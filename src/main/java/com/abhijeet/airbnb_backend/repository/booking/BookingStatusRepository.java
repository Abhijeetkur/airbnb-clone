package com.abhijeet.airbnb_backend.repository.booking;

import com.abhijeet.airbnb_backend.entity.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {
    // This allows you to find the status object by its name (e.g., "CONFIRMED")
    Optional<BookingStatus> findByStatusName(String statusName);
}