package com.example.ecommerce.repository;

import com.example.ecommerce.pojo.entity.Address;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.pojo.entity.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderRepository extends ListCrudRepository<WebOrder,Long> {
    List<WebOrder> findByUser(LocalUser user);
    List<WebOrder> findByAddress(Address address);

}
