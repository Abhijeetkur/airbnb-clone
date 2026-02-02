package com.abhijeet.airbnb_backend.repository.booking;

import com.abhijeet.airbnb_backend.entity.booking.BookingGuest;
import com.abhijeet.airbnb_backend.entity.booking.BookingGuestFkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingGuestRepository extends JpaRepository<BookingGuest, BookingGuestFkId> {
}
