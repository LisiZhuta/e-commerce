package com.example.ecommerce.api.controller.auth;

import com.example.ecommerce.api.model.LoginBody;
import com.example.ecommerce.api.model.LoginResponse;
import com.example.ecommerce.api.model.RegistrationBody;
import com.example.ecommerce.exception.UserAlreadyExistsException;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    // Injecting UserService dependency through constructor
    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        try {
            // Attempt to register the user
            userService.registerUser(registrationBody);
            // Return success response if registration is successful
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            // Return conflict status if user already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
        // Attempt to login user and generate JWT token
        String jwt = userService.loginUser(loginBody);
        if (jwt == null) {
            // Return bad request status if login is unsuccessful
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            // Return JWT token in a response if login is successful
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.ok(response);
        }
    }

    // Endpoint to get the profile of the currently logged-in user
    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        // Return the currently logged-in user's profile
        return user;
    }
}
