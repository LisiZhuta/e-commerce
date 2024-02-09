package com.example.ecommerce.repository;

import com.example.ecommerce.pojo.entity.WebOrderQuantities;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderQuantitiesRepository extends CrudRepository<WebOrderQuantities,Long> {
}
