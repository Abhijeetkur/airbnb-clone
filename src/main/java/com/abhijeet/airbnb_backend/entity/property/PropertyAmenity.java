package com.abhijeet.airbnb_backend.entity.property;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "property_amenity")
@Getter
@Setter
@ToString(exclude = { "property", "amenity" })
@AllArgsConstructor
@NoArgsConstructor
public class PropertyAmenity {

    @EmbeddedId
    private PropertyAmenityFkId propertyAmenityFkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("amenityId")
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("propertyId")
    @JoinColumn(name = "property_id")
    private Property property;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PropertyAmenity that = (PropertyAmenity) o;
        return propertyAmenityFkId != null && propertyAmenityFkId.equals(that.propertyAmenityFkId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
