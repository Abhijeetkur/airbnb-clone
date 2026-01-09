package com.abhijeet.airbnb_backend.entity.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BookingGuestFkId implements Serializable {
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "guest_type_id")
    private Long guestTypeId;
}
