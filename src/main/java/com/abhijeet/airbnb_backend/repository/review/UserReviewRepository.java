package com.abhijeet.airbnb_backend.repository.review;

import com.abhijeet.airbnb_backend.entity.review.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByPropertyId(Long propertyId);

    List<UserReview> findByUserId(Long userId);
}
