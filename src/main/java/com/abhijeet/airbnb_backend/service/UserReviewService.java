package com.abhijeet.airbnb_backend.service;

import com.abhijeet.airbnb_backend.dto.ComponentRatingRequest;
import com.abhijeet.airbnb_backend.dto.ReviewRequest;
import com.abhijeet.airbnb_backend.entity.booking.Booking;
import com.abhijeet.airbnb_backend.entity.property.Property;
import com.abhijeet.airbnb_backend.entity.review.Component;
import com.abhijeet.airbnb_backend.entity.review.ComponentRating;
import com.abhijeet.airbnb_backend.entity.review.ComponentRatingFkId;
import com.abhijeet.airbnb_backend.entity.review.UserReview;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.repository.booking.BookingRepository;
import com.abhijeet.airbnb_backend.repository.property.PropertyRepository;
import com.abhijeet.airbnb_backend.repository.review.ComponentRepository;
import com.abhijeet.airbnb_backend.repository.review.UserReviewRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ComponentRepository componentRepository;

    @Transactional
    public UserReview createReview(ReviewRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Validation: Check if user has a confirmed booking for this property that has
        // already ended
        List<Booking> bookings = bookingRepository.findByPropertyIdAndGuestId(property.getId(), user.getId());

        boolean hasConfirmedBooking = bookings.stream()
                .anyMatch(b -> b.getBookingStatus().getStatusName().equals("CONFIRMED"));

        if (!hasConfirmedBooking) {
            throw new RuntimeException("You can only review a property if you have a CONFIRMED booking.");
        }

        boolean stayEnded = bookings.stream()
                .filter(b -> b.getBookingStatus().getStatusName().equals("CONFIRMED"))
                .anyMatch(b -> b.getCheckOutDate().isBefore(LocalDate.now()));

        if (!stayEnded) {
            throw new RuntimeException(
                    "You can only review a property after your stay has ended (Check-out date must be in the past).");
        }

        UserReview review = new UserReview();
        review.setUser(user);
        review.setProperty(property);
        review.setComment(request.getComment());
        review.setOverallRating(request.getOverallRating());
        review.setReviewDate(LocalDateTime.now());

        List<ComponentRating> componentRatings = new ArrayList<>();
        if (request.getComponentRatings() != null) {
            for (ComponentRatingRequest crRequest : request.getComponentRatings()) {
                Component component = componentRepository.findById(crRequest.getComponentId())
                        .orElseThrow(() -> new RuntimeException("Component not found: " + crRequest.getComponentId()));

                ComponentRating componentRating = new ComponentRating();
                componentRating.setComponent(component);
                componentRating.setUserReview(review);
                componentRating.setRating(crRequest.getRating());

                ComponentRatingFkId fkId = new ComponentRatingFkId(component.getId(), null); // reviewId will be set by
                                                                                             // Hibernate
                componentRating.setComponentRatingFkId(fkId);

                componentRatings.add(componentRating);
            }
        }
        review.setComponentRatings(componentRatings);

        return userReviewRepository.save(review);
    }

    public List<UserReview> getReviewsByProperty(Long propertyId) {
        return userReviewRepository.findByPropertyId(propertyId);
    }
}
