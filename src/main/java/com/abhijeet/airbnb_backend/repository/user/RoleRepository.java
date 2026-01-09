package com.abhijeet.airbnb_backend.repository.user;

import com.abhijeet.airbnb_backend.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
