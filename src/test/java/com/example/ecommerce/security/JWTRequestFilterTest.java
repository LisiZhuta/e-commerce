package com.example.ecommerce.security;

import com.example.ecommerce.pojo.entity.LocalUser;
import com.example.ecommerce.repository.LocalUserRepository;
import com.example.ecommerce.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing the JWTRequestFilter.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan("com.example.ecommerce.service")
public class JWTRequestFilterTest {

    /** Mocked MVC. */
    @Autowired
    private MockMvc mvc;
    /** The JWT Service. */
    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserRepository localUserRepository;
    private static final String AUTHENTICATED_PATH = "/auth/me";

    /**
     * Tests that unauthenticated requests are rejected.
     * @throws Exception
     */
    @Test
    public void testUnauthenticatedRequest() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH)).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }


    @Test
    public void testBadToken() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "BadTokenThatIsNotValid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer BadTokenThatIsNotValid"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    /**
     * Tests unverified users who somehow get a jwt are rejected.
     * @throws Exception
     */
    @Test
    public void testUnverifiedUser() throws Exception {
        LocalUser user = localUserRepository.findByUsernameIgnoreCase("UserB").get();
        String token = jwtService.generateJWT(user);
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }


    @Test
    public void testSuccessful() throws Exception {
        LocalUser user = localUserRepository.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateJWT(user);
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer " + token))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

}