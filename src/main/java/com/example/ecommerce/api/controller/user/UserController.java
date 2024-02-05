package com.example.ecommerce.api.controller.user;

import com.example.ecommerce.pojo.entity.Address;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.pojo.entity.WebOrder;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.WebOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private AddressRepository addressRepository;
    private WebOrderRepository webOrderRepository;

    public UserController(AddressRepository addressRepository, WebOrderRepository webOrderRepository) {
        this.addressRepository = addressRepository;
        this.webOrderRepository = webOrderRepository;
    }

    // Endpoint to get addresses for a specific user
    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId) {
        // Check if the authenticated user has permission to access addresses for the given userId
        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // Return addresses associated with the specified userId
        return ResponseEntity.ok(addressRepository.findByUserId(userId));
    }

    // Endpoint to add a new address for a specific user
    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @RequestBody Address address) {
        // Check if the authenticated user has permission to add an address for the given userId
        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Set the user and save the new address
        address.setId(null);
        LocalUser refUser = new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));
    }

    // Endpoint to update an existing address for a specific user
    @PatchMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId,
            @PathVariable Long addressId, @RequestBody Address address) {
        // Check if the authenticated user has permission to update the address for the given userId
        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Check if the provided addressId matches the one in the path
        if (address.getId().equals(addressId)) {
            // Retrieve the original address from the database
            Optional<Address> opOriginalAddress = addressRepository.findById(addressId);
            if (opOriginalAddress.isPresent()) {
                LocalUser originalUser = opOriginalAddress.get().getUser();
                // Check if the original address belongs to the specified userId
                if (originalUser.getId().equals(userId)) {
                    // Set the user and save the updated address
                    address.setUser(originalUser);
                    return ResponseEntity.ok(addressRepository.save(address));
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // Endpoint to delete an address for a specific user
    @DeleteMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal LocalUser user, @PathVariable Long userId, @PathVariable Long addressId) {
        // Check if the authenticated user has permission to delete the address for the given userId
        if (!userHasPermission(user, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Retrieve the address from the database
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            // Check if the address belongs to the specified userId
            if (address.getUser().getId().equals(userId)) {
                // Remove the address from associated web orders
                List<WebOrder> webOrders = webOrderRepository.findByAddress(address);
                webOrders.forEach(webOrder -> webOrder.setAddress(null));

                // Delete the address
                addressRepository.delete(address);
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    // Helper method to check if the user has permission based on user id
    private boolean userHasPermission(LocalUser user, Long id) {
        return user.getId().equals(id);
    }
}
