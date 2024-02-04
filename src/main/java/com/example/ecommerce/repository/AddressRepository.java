package com.example.ecommerce.repository;

import com.example.ecommerce.pojo.entity.Address;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface AddressRepository extends ListCrudRepository<Address,Long> {

    List<Address> findByUserId(Long id);
}
