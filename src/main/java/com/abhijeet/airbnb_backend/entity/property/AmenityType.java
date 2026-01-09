package com.abhijeet.airbnb_backend.entity.property;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "amenity_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amenity_name")
    private String amenityName;

    @OneToMany(mappedBy = "amenityType")
    private Set<Amenity> amenities;

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
