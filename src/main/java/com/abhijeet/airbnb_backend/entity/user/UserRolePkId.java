package com.abhijeet.airbnb_backend.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserRolePkId implements Serializable {
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "user_id")
    private Long userId;
}
