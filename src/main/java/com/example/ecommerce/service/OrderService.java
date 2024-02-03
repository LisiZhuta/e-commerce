package com.example.ecommerce.service;

import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.pojo.entity.WebOrder;
import com.example.ecommerce.repository.WebOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private WebOrderRepository webOrderRepository;

    public OrderService(WebOrderRepository webOrderRepository) {
        this.webOrderRepository = webOrderRepository;
    }

    public List<WebOrder> getOrders(LocalUser user){
      return webOrderRepository.findByUser(user);
    }


}
