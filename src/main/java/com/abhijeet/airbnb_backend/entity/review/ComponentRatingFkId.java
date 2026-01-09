package com.abhijeet.airbnb_backend.entity.review;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentRatingFkId implements Serializable {
    @Column(name = "component_id")
    private Long componentId;

    @Column(name = "review_id")
    private Long reviewId;
}
