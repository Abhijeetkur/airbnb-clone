package com.abhijeet.airbnb_backend.repository.review;

import com.abhijeet.airbnb_backend.entity.review.ComponentRating;
import com.abhijeet.airbnb_backend.entity.review.ComponentRatingFkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRatingRepository extends JpaRepository<ComponentRating, ComponentRatingFkId> {
}
