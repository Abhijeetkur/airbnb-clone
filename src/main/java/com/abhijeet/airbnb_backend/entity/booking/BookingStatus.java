package com.abhijeet.airbnb_backend.entity.booking;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking_status")
@Getter @Setter
public class BookingStatus {
    public static final BookingStatus CONFIRMED = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name")
    private String statusName;

    @OneToMany(mappedBy = "bookingStatus")
    private List<Booking> bookings;

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

    public String name() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'name'");
    }

}
