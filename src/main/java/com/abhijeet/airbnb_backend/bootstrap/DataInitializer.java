package com.abhijeet.airbnb_backend.bootstrap;

import com.abhijeet.airbnb_backend.entity.user.*;
import com.abhijeet.airbnb_backend.repository.user.RoleRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRoleRepository;
import com.abhijeet.airbnb_backend.repository.booking.BookingStatusRepository;
import com.abhijeet.airbnb_backend.repository.booking.GuestTypeRepository;
import com.abhijeet.airbnb_backend.entity.booking.BookingStatus;
import com.abhijeet.airbnb_backend.entity.booking.GuestType;
import com.abhijeet.airbnb_backend.repository.review.ComponentRepository;
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
    private final BookingStatusRepository bookingStatusRepository;
    private final GuestTypeRepository guestTypeRepository;
    private final ComponentRepository componentRepository;

    public DataInitializer(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            BookingStatusRepository bookingStatusRepository,
            GuestTypeRepository guestTypeRepository,
            ComponentRepository componentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookingStatusRepository = bookingStatusRepository;
        this.guestTypeRepository = guestTypeRepository;
        this.componentRepository = componentRepository;
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
        boolean alreadyAssigned = userRoleRepository.existsByUserIdAndRoleId(
                admin.getId(),
                adminRole.getId());

        if (!alreadyAssigned) {
            UserRolePkId pk = new UserRolePkId(
                    adminRole.getId(),
                    admin.getId());

            UserRole ur = new UserRole();
            ur.setUserRolePkId(pk);
            ur.setUser(admin);
            ur.setRole(adminRole);

            userRoleRepository.save(ur);
        }

        System.out.println("✅ Admin bootstrap completed");

        /* ------------------ BOOKING STATUSES ------------------ */
        bootstrapBookingStatus("CONFIRMED");
        bootstrapBookingStatus("PENDING");
        bootstrapBookingStatus("CANCELLED");

        /* ------------------ GUEST TYPES ------------------ */
        bootstrapGuestType("ADULT");
        bootstrapGuestType("CHILD");
        bootstrapGuestType("INFANT");
        bootstrapGuestType("PET");

        /* ------------------ REVIEW COMPONENTS ------------------ */
        bootstrapReviewComponent("Cleanliness");
        bootstrapReviewComponent("Accuracy");
        bootstrapReviewComponent("Communication");
        bootstrapReviewComponent("Location");
        bootstrapReviewComponent("Check-in");
        bootstrapReviewComponent("Value");
    }

    private void bootstrapBookingStatus(String statusName) {
        if (bookingStatusRepository.findByStatusName(statusName).isEmpty()) {
            BookingStatus status = new BookingStatus();
            status.setStatusName(statusName);
            bookingStatusRepository.save(status);
            System.out.println("Booking status '" + statusName + "' created");
        }
    }

    private void bootstrapGuestType(String typeName) {
        if (guestTypeRepository.findByTypeName(typeName).isEmpty()) {
            GuestType type = new GuestType();
            type.setTypeName(typeName);
            guestTypeRepository.save(type);
            System.out.println("Guest type '" + typeName + "' created");
        }
    }

    private void bootstrapReviewComponent(String componentName) {
        if (componentRepository.findByComponentName(componentName).isEmpty()) {
            com.abhijeet.airbnb_backend.entity.review.Component component = new com.abhijeet.airbnb_backend.entity.review.Component();
            component.setComponentName(componentName);
            componentRepository.save(component);
            System.out.println("Review component '" + componentName + "' created");
        }
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
