package com.example.ecommerce.service;

import com.example.ecommerce.api.model.LoginBody;
import com.example.ecommerce.api.model.RegistrationBody;
import com.example.ecommerce.exception.UserAlreadyExistsException;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.repository.LocalUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserRepository localUserRepository;
    private EncryptionService encryptionService;
    private JWTService jwtService;

    // Constructor injection of dependencies
    public UserService(LocalUserRepository localUserRepository, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserRepository = localUserRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    // Method to register a new user
    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        // Check if user with the same email or username already exists
        if (localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        // Create a new LocalUser entity and set its properties
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());

        // Encrypt the password before saving it to the database
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        // Save the user to the repository
        return localUserRepository.save(user);
    }

    // Method to log in a user and generate a JWT
    public String loginUser(LoginBody loginBody) {
        // Check if the user with the given username exists
        Optional<LocalUser> opUser = localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            // Verify the password
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                // Generate and return a JWT for the user
                return jwtService.generateJWT(user);
            }
        }
        // Return null if login fails
        return null;
    }
}
