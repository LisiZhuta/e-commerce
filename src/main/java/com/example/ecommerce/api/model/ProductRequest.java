package com.example.ecommerce.api.model;

import com.example.ecommerce.pojo.entity.Product;

public class ProductRequest {

    private Product product;
    private Integer quantity;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    // getters and setters
}
