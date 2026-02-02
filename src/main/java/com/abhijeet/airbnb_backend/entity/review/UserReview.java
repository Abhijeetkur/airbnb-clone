package com.abhijeet.airbnb_backend.entity.review;

import com.abhijeet.airbnb_backend.entity.property.Property;
import com.abhijeet.airbnb_backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_review")
@Getter
@Setter
@ToString(exclude = { "componentRatings", "property", "user" })
@AllArgsConstructor
@NoArgsConstructor
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "overall_rating")
    private BigDecimal overallRating;

    @Column(name = "review_column")
    private LocalDateTime reviewDate;

    @OneToMany(mappedBy = "userReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComponentRating> componentRatings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
        UserReview that = (UserReview) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
