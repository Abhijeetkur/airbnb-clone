package com.abhijeet.airbnb_backend.entity.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "amenity")
@Getter
@Setter
@ToString(exclude = "propertyAmenities")
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
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private AmenityType amenityType;

    @JsonIgnore
    @OneToMany(mappedBy = "amenity")
    private Set<PropertyAmenity> propertyAmenities;

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
        Amenity amenity = (Amenity) o;
        return id != null && id.equals(amenity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
