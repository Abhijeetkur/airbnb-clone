package com.abhijeet.airbnb_backend.entity.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking_guest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingGuest {
    @EmbeddedId
    private BookingGuestFkId bookingGuestFkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("guestTypeId")
    @JoinColumn(name = "guest_type_id")
    private GuestType guestType;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
