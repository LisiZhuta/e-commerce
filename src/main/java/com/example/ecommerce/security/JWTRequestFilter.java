package com.example.ecommerce.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.repository.LocalUserRepository;
import com.example.ecommerce.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private LocalUserRepository localUserRepository;

    public JWTRequestFilter(JWTService jwtService, LocalUserRepository localUserRepository) {
        this.jwtService = jwtService;
        this.localUserRepository = localUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract the token from the Authorization header
        String tokenHeader = request.getHeader("Authorization");

        // Check if the token is present and has the expected format
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            // Extract the token from the header
            String token = tokenHeader.substring(7);

            try {
                // Decode the token to get the username
                String username = jwtService.getUsername(token);

                // Retrieve user details from the repository based on the username
                Optional<LocalUser> opUser = localUserRepository.findByUsernameIgnoreCase(username);

                // If user details are present, create an authentication token and set it in the security context
                if (opUser.isPresent()) {
                    LocalUser user = opUser.get();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (JWTDecodeException e) {
                // Handle any JWT decoding exceptions if they occur
                // Note: You might want to log or handle this exception based on your application's requirements
            }

        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
