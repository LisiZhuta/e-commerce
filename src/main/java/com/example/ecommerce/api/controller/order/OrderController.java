package com.example.ecommerce.api.controller.order;

import com.example.ecommerce.pojo.entity.Address;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.pojo.entity.WebOrder;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.WebOrderRepository;
import com.example.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private WebOrderRepository webOrderRepository;
    private AddressRepository addressRepository;

    public OrderController(OrderService orderService, WebOrderRepository webOrderRepository, AddressRepository addressRepository) {
        this.orderService = orderService;
        this.webOrderRepository = webOrderRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user) {
        return orderService.getOrders(user);
    }

    @PostMapping("{userId}/order")
    public ResponseEntity<WebOrder> putOrder(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @RequestBody WebOrder webOrder) {
        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Assuming the addressId is provided in the JSON request
        Long addressId = webOrder.getAddress().getId();


        // Fetch the existing address from the database
        Optional<Address> existingAddress = addressRepository.findById(addressId);
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            LocalUser refUser = new LocalUser();
            refUser.setId(userId);

            // Set user and existing address on the WebOrder
            webOrder.setId(null);
            webOrder.setUser(refUser);
            webOrder.setAddress(address);

            //need to set the order quantities to get the product,inventory and quantity info ?????

            // Save the webOrder with the existing address
            WebOrder savedOrder = webOrderRepository.save(webOrder);

            // Fetch the saved order from the database to ensure the quantities are loaded
            WebOrder populatedOrder = webOrderRepository.findById(savedOrder.getId()).orElse(null);

            return ResponseEntity.ok(populatedOrder);
        } else {
            // Handle the case where the specified address does not exist
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("{userId}/order/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long orderId) {
        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Check if the order exists
        Optional<WebOrder> optionalOrder = webOrderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            // Fetch the order
            WebOrder order = optionalOrder.get();

            // Check if the order belongs to the specified user
            if (!order.getUser().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Delete the order
            webOrderRepository.delete(order);

            return ResponseEntity.noContent().build();
        } else {
            // Handle the case where the order does not exist
            return ResponseEntity.notFound().build();
        }
    }

    private boolean userHasPermission(LocalUser user,Long id){
        return user.getId()==id;
    }

}
