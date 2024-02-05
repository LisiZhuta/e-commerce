package com.example.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF and CORS for simplicity (customize as needed for your application)
        http.csrf().disable().cors().disable();

        // Add the custom JWTRequestFilter before the default Spring Security AuthorizationFilter
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);

        // Configure authorization rules
        http.authorizeHttpRequests()
                // Allow access to certain endpoints without authentication
                .requestMatchers("/product", "/auth/register", "/auth/login").permitAll()
                // Require authentication for any other request
                .anyRequest().authenticated();

        // Return the configured HttpSecurity instance
        return http.build();
    }
}
