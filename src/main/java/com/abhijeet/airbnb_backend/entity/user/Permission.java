package com.abhijeet.airbnb_backend.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "permissions",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "permission_name")
})
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_name", unique = true, length = 50)
    private String  permissionName;

    @Column(length = 250)
    private String bio;

    @OneToMany(mappedBy = "permission")
    private Set<RolePermission> rolePermission;

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
