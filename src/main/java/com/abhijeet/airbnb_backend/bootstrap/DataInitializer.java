package com.abhijeet.airbnb_backend.bootstrap;

import com.abhijeet.airbnb_backend.entity.user.*;
import com.abhijeet.airbnb_backend.repository.user.RoleRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        /* ------------------ ROLES ------------------ */
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName("ADMIN");
                    return roleRepository.save(r);
                });

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName("USER");
                    return roleRepository.save(r);
                });

        /* ------------------ ADMIN USER ------------------ */
        User admin = userRepository.findByEmail("admin@airbnb.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail("admin@airbnb.com");
                    u.setFname("Admin");
                    u.setLname("User");
                    u.setPassword(passwordEncoder.encode("admin123"));
                    return userRepository.save(u);
                });

        /* ------------------ ASSIGN ROLE ------------------ */
        boolean alreadyAssigned =
                userRoleRepository.existsByUserIdAndRoleId(
                        admin.getId(),
                        adminRole.getId()
                );

        if (!alreadyAssigned) {
            UserRolePkId pk = new UserRolePkId(
                    adminRole.getId(),
                    admin.getId()
            );

            UserRole ur = new UserRole();
            ur.setUserRolePkId(pk);
            ur.setUser(admin);
            ur.setRole(adminRole);

            userRoleRepository.save(ur);
        }

        System.out.println("✅ Admin bootstrap completed");
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if USER role exists, if not, create it
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
            System.out.println("User role created");
        }

        // Check if ADMIN role exists
        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByRoleName("HOST").isEmpty()) {
            Role hostRole = new Role();
            hostRole.setRoleName("HOST");
            roleRepository.save(hostRole);
            System.out.println("HOST role created");
        }
    }
}
