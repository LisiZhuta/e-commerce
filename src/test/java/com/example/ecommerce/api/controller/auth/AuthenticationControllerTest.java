package com.example.ecommerce.api.controller.auth;

import com.example.ecommerce.api.model.LoginBody;
import com.example.ecommerce.api.model.RegistrationBody;
import com.example.ecommerce.exception.UserAlreadyExistsException;
import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testRegisterUser() throws Exception {
        // Setup
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("testusername");
        registrationBody.setPassword("testpassword");
        registrationBody.setEmail("testusername@hotmail.com");
        registrationBody.setFirstName("testuser");
        registrationBody.setLastName("test-user");

        when(userService.registerUser(any(RegistrationBody.class)));

        // Test
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserConflict() throws Exception {
        // Setup
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("testusername");
        registrationBody.setPassword("testpassword");
        registrationBody.setEmail("testusername@hotmail.com");
        registrationBody.setFirstName("testuser");
        registrationBody.setLastName("test-user");;
        when(userService.registerUser(any(RegistrationBody.class)))
                .thenThrow(new UserAlreadyExistsException());

        // Test
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registrationBody)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testLoginUser() throws Exception {
        // Setup
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testuser");
        loginBody.setPassword("testpassword1");
        when(userService.loginUser(any(LoginBody.class))).thenReturn("dummyJWT");

        // Test
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").value("dummyJWT"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetLoggedInUserProfile() throws Exception {
        // Setup
        LocalUser user = new LocalUser();
                user.setUsername("username");
                user.setPassword("password");

        // Test
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testUser"));
    }

    // Helper method to convert objects to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
