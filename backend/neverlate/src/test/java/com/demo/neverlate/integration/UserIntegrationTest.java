package com.demo.neverlate.integration;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // Permet de charger une configuration spécifique aux tests
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    

    @Test
    public void testUserRegistration_Success() {

        UserDTO userDTO = UserDTO.builder()
                .username("newuser")
                .password("password123")
                .email("newuser@example.com")
                .build();


        ResponseEntity<String> response = restTemplate.postForEntity("/register", userDTO, String.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    public void testUserRegistration_EmailAlreadyExists() {

        userRepository.save(User.builder()
                .username("existinguser")
                .email("existing@example.com")
                .password("password123")
                .build());

        UserDTO userDTO = UserDTO.builder()
                .username("newuser")
                .password("password123")
                .email("existing@example.com")  // Email déjà utilisé
                .build();


        ResponseEntity<String> response = restTemplate.postForEntity("/register", userDTO, String.class);


        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        String expectedMessage = "Email existing@example.com already exists";
        assertTrue(response.getBody().contains(expectedMessage), "Expected message not found in response body");
    }
}
