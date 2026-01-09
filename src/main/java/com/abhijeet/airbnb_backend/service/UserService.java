package com.abhijeet.airbnb_backend.service;

import com.abhijeet.airbnb_backend.dto.LoginRequest;
import com.abhijeet.airbnb_backend.dto.UserResponse;
import com.abhijeet.airbnb_backend.entity.user.Role;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.entity.user.UserRole;
import com.abhijeet.airbnb_backend.entity.user.UserRolePkId;
import com.abhijeet.airbnb_backend.exception.EmailAlreadyExistsException;
import com.abhijeet.airbnb_backend.exception.UserNotFoundException;
import com.abhijeet.airbnb_backend.repository.user.RoleRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRepository;
import com.abhijeet.airbnb_backend.repository.user.UserRoleRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse registerNewUser(User user) {
        // 1. Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            // Trigger the specific exception
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        // 2. NEW: Check if phone number already exists
        if (user.getPhoneNumber() != null && userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already in use!");
        }

        // 2. Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Save the User
        User savedUser = userRepository.save(user);

        // 4. Assign the 'USER' Role
        Role defaultRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found - Check DataInitializer!"));

        UserRole userRole = new UserRole();
        // Setting up the composite key for your user_roles table
        UserRolePkId pkId = new UserRolePkId(savedUser.getId(), defaultRole.getId());
        userRole.setUserRolePkId(pkId);
        userRole.setUser(savedUser);
        userRole.setRole(defaultRole);

        userRoleRepository.save(userRole);
        // 5. Map Entity to DTO?
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setFname(savedUser.getFname());
        userResponse.setLname(savedUser.getLname());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPhoneNumber(savedUser.getPhoneNumber());
        userResponse.setDob(savedUser.getDob());
        userResponse.setBio(savedUser.getBio());
        return userResponse;
    }

    public UserResponse login(LoginRequest loginRequest) {
        try {
            // 1. Create a token with the credentials provided by the user
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    loginRequest.getIdentifier(), loginRequest.getPassword());

            // 2. The Manager checks the password against the UserDetailsService
            // If it fails, it throws an exception (which your GlobalHandler will catch)
            Authentication authentication = authenticationManager.authenticate(authRequest);

            // 3. Store the successful authentication in the Security Context
            // This is what makes the user "logged in" for the duration of the session
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // CRITICAL: Save to session so /me works!
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            // 4. Fetch the user to return the DTO
            User user = userRepository
                    .findByEmailOrPhoneNumber(loginRequest.getIdentifier(), loginRequest.getIdentifier())
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "No account found with identifier" + loginRequest.getIdentifier()));

            return mapToUserResponse(user);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials provided");
        }
    }

    public Page<UserResponse> getAllUsers(int page, int size, String sortBy, String role) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 100), Sort.by(sortBy));
        Page<User> userPage;
        if (role != null && !role.isEmpty()) {
            userPage = userRepository.findAllByRoleName(role.toUpperCase(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        return userPage.map(this::mapToUserResponse);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return mapToUserResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID " + id + " does not exist."));
        return mapToUserResponse(user);
    }

    // Helper method to keep logic clean
    private UserResponse mapToUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFname(user.getFname());
        dto.setLname(user.getLname());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBio(user.getBio());
        dto.setDob(user.getDob());
        return dto;
    }

    @Transactional
    public User assignRole(Long userId, String roleName) {
        // 1. Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // 2. Find the role
        Role role = roleRepository.findByRoleName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found in database"));

        // 3. stop duplicates
        boolean HasRole = userRoleRepository.existsByUserIdAndRoleId(userId, role.getId());
        if (HasRole)
            return user;

        // 4. Create the composite key object
        UserRolePkId pkId = new UserRolePkId();
        pkId.setUserId(userId);
        pkId.setRoleId(role.getId());

        // 5. Create the UserRole Entity (The Join Record)
        UserRole userRole = new UserRole();

        // This is the line you were stuck on:
        // You set the ID object into the UserRole entity
        userRole.setUserRolePkId(pkId);

        // Setting the actual objects (required by @MapsId)
        userRole.setUser(user);
        userRole.setRole(role);

        // 6. Save the record
        userRoleRepository.save(userRole);

        // 7. Update the user entity in memory so it's not stale
        if (user.getUserRoles() == null) {
            user.setUserRoles(new java.util.HashSet<>());
        }
        user.getUserRoles().add(userRole);

        return user;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }
}
