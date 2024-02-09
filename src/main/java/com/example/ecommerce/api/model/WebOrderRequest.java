package com.example.ecommerce.api.model;

import com.example.ecommerce.pojo.entity.Address;
import com.example.ecommerce.pojo.entity.WebOrder;
import com.example.ecommerce.pojo.entity.WebOrderQuantities;

import java.util.ArrayList;
import java.util.List;

public class WebOrderRequest {
    private WebOrder webOrder;
    private Long productId;
    private Address address;

    private List<WebOrderQuantities> quantities = new ArrayList<>();

    public List<WebOrderQuantities> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<WebOrderQuantities> quantities) {
        this.quantities = quantities;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public WebOrder getWebOrder() {
        return webOrder;
    }

    public void setWebOrder(WebOrder webOrder) {
        this.webOrder = webOrder;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
