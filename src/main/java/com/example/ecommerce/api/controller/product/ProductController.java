package com.example.ecommerce.api.controller.product;

import com.example.ecommerce.api.model.ProductRequest;
import com.example.ecommerce.pojo.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    // Injecting ProductService dependency through constructor
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Endpoint to retrieve a list of all products
    @GetMapping
    public List<Product> getProducts() {
        // Delegate to ProductService to get the list of products
        return productService.getProducts();
    }

    // Endpoint to create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        // Delegate to ProductService to create a new product with the given details
        Product createdProduct = productService.createProduct(productRequest.getProduct(), productRequest.getQuantity());
        // Return a response with the created product and HTTP status code 201 (CREATED)
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
}
