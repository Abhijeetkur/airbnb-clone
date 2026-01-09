package com.abhijeet.airbnb_backend.entity.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "component_rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentRating {
    @EmbeddedId
    private ComponentRatingFkId componentRatingFkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("componentId")
    @JoinColumn(name = "component_id")
    private Component component;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reviewId")
    @JoinColumn(name = "review_id")
    private UserReview userReview;

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
