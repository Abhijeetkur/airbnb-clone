package com.abhijeet.airbnb_backend.repository.user;

import com.abhijeet.airbnb_backend.entity.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
