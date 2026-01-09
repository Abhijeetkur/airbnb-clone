    package com.abhijeet.airbnb_backend.repository.user;

    import com.abhijeet.airbnb_backend.entity.user.Role;
    import com.abhijeet.airbnb_backend.entity.user.UserRole;
    import com.abhijeet.airbnb_backend.entity.user.UserRolePkId;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;

    public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePkId> {
        List<UserRole> findByUserId(Long userId);
        boolean existsByUserIdAndRoleId(Long userId, Long roleId);
        void deleteByUserId(Long userId);
    }
