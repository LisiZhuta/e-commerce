package com.example.ecommerce.api.controller.user;

import com.example.ecommerce.pojo.entity.Address;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.repository.AddressRepository;
import org.apache.coyote.Response;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
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

    public UserController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping("/{userId}/address")
    public ResponseEntity<List<Address>> getAddress(
            @AuthenticationPrincipal LocalUser user,@PathVariable Long userId){
        if(!userHasPermission(user,userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(addressRepository.findByUserId(userId));
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<Address> putAddress(
            @AuthenticationPrincipal LocalUser user,@PathVariable Long userId,@RequestBody Address address){
        if(!userHasPermission(user,userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        address.setId(null);
        LocalUser refUser=new LocalUser();
        refUser.setId(userId);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));

    }

    @PatchMapping("/{userId}/address/{addressId}")
    public ResponseEntity<Address> patchAddress(
            @AuthenticationPrincipal LocalUser user,@PathVariable Long userId,
            @PathVariable Long addressId,@RequestBody Address address){
        if(!userHasPermission(user,userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    if(address.getId()==(addressId)){
        Optional<Address> opOriginalAddress=addressRepository.findById(addressId);
        if(opOriginalAddress.isPresent()){
            LocalUser originalUser =opOriginalAddress.get().getUser();
            if(originalUser.getId()==userId){
                address.setUser(originalUser);
                return ResponseEntity.ok(addressRepository.save(address));
            }
        }
    }
    return ResponseEntity.badRequest().build();

    }




    private boolean userHasPermission(LocalUser user,Long id){
        return user.getId()==id;
    }

}
