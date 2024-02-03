package com.example.ecommerce.repository;

import com.example.ecommerce.pojo.entity.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product,Long> {
}
