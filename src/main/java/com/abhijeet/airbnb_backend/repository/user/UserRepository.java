package com.abhijeet.airbnb_backend.repository.user;

import com.abhijeet.airbnb_backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);

    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.roleName = :roleName")
    Page<User> findAllByRoleName(@Param("roleName") String roleName, Pageable pageable);
}
