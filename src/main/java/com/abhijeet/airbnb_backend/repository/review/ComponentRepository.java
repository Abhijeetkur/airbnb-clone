package com.abhijeet.airbnb_backend.repository.review;

import com.abhijeet.airbnb_backend.entity.review.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    Optional<Component> findByComponentName(String componentName);
}
