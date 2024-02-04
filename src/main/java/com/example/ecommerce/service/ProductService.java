package com.example.ecommerce.service;

import com.example.ecommerce.pojo.entity.Inventory;
import com.example.ecommerce.pojo.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        // Create a new inventory for the product
        Inventory inventory = new Inventory();
        // Set the quantity for the inventory (you can set it based on your requirements)
        inventory.setQuantity(10);

        // Associate the product with the inventory
        product.setInventory(inventory);

        // Save both the product and inventory
        return productRepository.save(product);
    }
}
