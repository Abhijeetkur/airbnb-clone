package com.abhijeet.airbnb_backend.entity.location;

import com.abhijeet.airbnb_backend.entity.property.Property;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> propertyList;

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
