package com.abhijeet.airbnb_backend.repository.booking;

import com.abhijeet.airbnb_backend.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Updated 'b.status' to 'b.bookingStatus.statusName'
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.property.id = :propertyId " +
            "AND b.bookingStatus.statusName = 'CONFIRMED' " +
            "AND (:startDate < b.checkOutDate AND :endDate > b.checkInDate)")
    boolean isBooked(@Param("propertyId") Long propertyId,
                     @Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate);
}