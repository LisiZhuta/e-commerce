package com.example.ecommerce.service;

import com.example.ecommerce.api.model.RegistrationBody;
import com.example.ecommerce.exception.UserAlreadyExistsException;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.repository.LocalUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class UserService {

    private LocalUserRepository localUserRepository;

    public UserService(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
    }

    public LocalUser registerUser(RegistrationBody registrationBody)throws UserAlreadyExistsException {
        if(localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()||
                localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        LocalUser user =new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(registrationBody.getPassword());
        return localUserRepository.save(user);
    }

}
