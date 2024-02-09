package com.example.ecommerce.api.controller.product;

import com.example.ecommerce.api.model.ProductRequest;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.pojo.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    // Injecting ProductService dependency through constructor
    private ProductService productService;
    private ProductRepository productRepository;


    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
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

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
              @PathVariable Long productId)
    {


        Optional<Product> optionalProduct=productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product product=optionalProduct.get();
            productRepository.delete(product);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();

    }




    private boolean userHasPermission(LocalUser user,Long id){
        return user.getId()==id;
    }
}
