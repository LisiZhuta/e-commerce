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

    public UserService(LocalUserRepository localUserRepository, EncryptionService encryptionService, JWTService  jwtService) {
        this.localUserRepository = localUserRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
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

        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        return localUserRepository.save(user);
    }

    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> opUser= localUserRepository.findByUsernameIgnoreCase((loginBody.getUsername()));
        if(opUser.isPresent()){
        LocalUser user=opUser.get();
        if(encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
            return jwtService.generateJWT(user);
        }
        }
        return null;
    }


}
