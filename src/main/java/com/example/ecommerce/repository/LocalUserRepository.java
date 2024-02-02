package com.example.ecommerce.repository;

import com.example.ecommerce.pojo.entity.LocalUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalUserRepository extends CrudRepository<LocalUser,Long> {

    Optional<LocalUser> findByUsernameIgnoreCase(String username);
    Optional<LocalUser> findByEmailIgnoreCase(String email);


}
