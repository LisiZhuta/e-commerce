package com.example.ecommerce.repository;

import com.example.ecommerce.pojo.entity.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ProductRepository extends ListCrudRepository<Product,Long> {
    Optional<Product> findById(Long productId);
}
