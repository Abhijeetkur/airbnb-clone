package com.abhijeet.airbnb_backend.entity.property;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "amenity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amenity_name")
    private String amenityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_type_id")
    private AmenityType amenityType;

    @OneToMany(mappedBy = "amenity")
    private Set<PropertyAmenity> propertyAmenities;

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
