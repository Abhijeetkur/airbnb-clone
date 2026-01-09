package com.abhijeet.airbnb_backend.config;

import com.abhijeet.airbnb_backend.dto.ApiResponse;
import com.abhijeet.airbnb_backend.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    // 1. Constructor Injection
    public SecurityConfig(CustomUserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 2. Define the Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/register", "/api/v1/users/login","/error").permitAll()
                        .requestMatchers("/api/v1/users/me").authenticated()
                        .requestMatchers("/api/v1/users/become-host").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/host/**").hasRole("HOST")
                        .anyRequest().authenticated()
                )
                // 2. KEEP THIS FOR LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/api/v1/users/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 1. Set the content type to JSON
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_OK);

                            // 2. Create the custom response body
                            ApiResponse logoutResponse = new ApiResponse(
                                    "Logout successful. Session invalidated.",
                                    true,
                                    LocalDateTime.now().toString()
                            );

                            // 3. Write JSON to the response
                            String jsonResponse = objectMapper.writeValueAsString(logoutResponse);
                            response.getWriter().write(jsonResponse);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        // Use the authentication provider we defined above
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}