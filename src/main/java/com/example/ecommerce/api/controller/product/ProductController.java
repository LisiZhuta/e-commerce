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

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public List<Product> getProducts()
    {
        return productService.getProducts();
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        Product createdProduct = productService.createProduct(productRequest.getProduct(), productRequest.getQuantity());
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

}
