package com.abhijeet.airbnb_backend.service;

import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // org.springframework.transaction.annotation.Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrPhoneNumber(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Forces initialization of roles while the session is open
        user.getAuthorities().size();

        return user;
    }
}
