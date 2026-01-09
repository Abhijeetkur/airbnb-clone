package com.abhijeet.airbnb_backend.entity.property;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "property_amenity")
@Data
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
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
