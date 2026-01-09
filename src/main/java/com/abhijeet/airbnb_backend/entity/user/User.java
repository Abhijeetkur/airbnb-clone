package com.abhijeet.airbnb_backend.entity.user;

import com.abhijeet.airbnb_backend.entity.property.Property;
import com.abhijeet.airbnb_backend.entity.review.UserReview;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@ToString(exclude = {"userRoles", "properties", "userReviews"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(length = 100, nullable = false)
    private String fname;

    @Column(length = 100, nullable = false)
    private String lname;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    private LocalDate dob;

    @Column(length = 200)
    private String bio;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "host")
    private Set<Property> properties;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserReview> userReviews;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

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

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("CheckingRole:" + userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(
                        "ROLE_" + userRole.getRole().getRoleName()))
                .toList());
        if (userRoles == null) {
            return Collections.emptyList();
        }
        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(
                        "ROLE_" + userRole.getRole().getRoleName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
