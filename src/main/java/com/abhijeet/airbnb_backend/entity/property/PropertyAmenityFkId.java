package com.abhijeet.airbnb_backend.entity.property;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PropertyAmenityFkId implements Serializable {
    @Column(name = "amenity_id")
    private Long amenityId;

    @Column(name = "property_id")
    private Long propertyId;
}
