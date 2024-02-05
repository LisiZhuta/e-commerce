package com.example.ecommerce.api.controller.order;

import com.example.ecommerce.pojo.entity.Address;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.pojo.entity.WebOrder;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.WebOrderRepository;
import com.example.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private WebOrderRepository webOrderRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testGetOrders() throws Exception {
        // Setup
        LocalUser user = new LocalUser();
        when(orderService.getOrders(any(LocalUser.class))).thenReturn(Collections.emptyList());

        // Test
        mockMvc.perform(MockMvcRequestBuilders.get("/order").with(user("testUser")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void testPutOrder() throws Exception {
        // Setup
        LocalUser user = new LocalUser();
        user.setId(1L);
        Address address = new Address();
        address.setId(1L);
        WebOrder webOrder = new WebOrder();
        webOrder.setAddress(address);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(webOrderRepository.save(any(WebOrder.class))).thenReturn(webOrder);

        // Test
        mockMvc.perform(MockMvcRequestBuilders.post("/order/1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(webOrder))
                        .with(user("testUser")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.id").value(1L));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // Setup
        LocalUser user = new LocalUser();
        user.setId(1L);
        WebOrder webOrder = new WebOrder();
        webOrder.setId(1L);
        webOrder.setUser(user);

        when(webOrderRepository.findById(1L)).thenReturn(Optional.of(webOrder));

        // Test
        mockMvc.perform(MockMvcRequestBuilders.delete("/order/1/order/1").with(user("testUser")))
                .andExpect(status().isNoContent());
    }

    // Helper method to convert objects to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    // Helper method to create a UserDetails instance for authentication

}
