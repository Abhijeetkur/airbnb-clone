package com.abhijeet.airbnb_backend.entity.property;

import com.abhijeet.airbnb_backend.entity.location.Location;
import com.abhijeet.airbnb_backend.entity.review.UserReview;
import com.abhijeet.airbnb_backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_name", length = 100, nullable = false)
    private String propertyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type_id", nullable = false)
    private PropertyType propertyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_type_id", nullable = false)
    private PlaceType placeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PropertyAmenity> propertyAmenities;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserReview> userReviews;

    private BigDecimal price;

    @Column(name = "num_guest")
    private int numGuest;

    @Column(name = "num_bedrooms")
    private int numBedrooms;

    @Column(name = "num_beds")
    private int numBeds;

    @Column(name = "num_washrooms")
    private int numWashrooms;

    private String description;

    @Column(name = "is_guests_favorite")
    private boolean isGuestsFavorite;

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
