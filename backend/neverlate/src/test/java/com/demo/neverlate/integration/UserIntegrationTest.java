package com.demo.neverlate.integration;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    // Nettoyage de la base de données après chaque test
    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testUserRegistration_Success() {

        // Préparation de l'utilisateur à enregistrer
        UserDTO userDTO = UserDTO.builder()
                .username("newuser")
                .password("password123")
                .email("newuser@example.com")
                .build();

        // Envoi de la requête POST pour enregistrer un nouvel utilisateur
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userDTO, String.class);

        // Vérification du statut HTTP (201 Created attendu pour une création)
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUserRegistration_EmailAlreadyExists() {

        // Sauvegarde d'un utilisateur existant avec un email spécifique
        userRepository.save(User.builder()
                .username("existinguser")
                .email("existing@example.com")
                .password("password123")
                .build());

        // Préparation de l'utilisateur à enregistrer avec un email déjà utilisé
        UserDTO userDTO = UserDTO.builder()
                .username("newuser")
                .password("password123")
                .email("existing@example.com")  // Email déjà utilisé
                .build();

        // Envoi de la requête POST pour tenter l'enregistrement
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userDTO, String.class);

        // Vérification du statut HTTP (409 Conflict attendu)
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        // Vérification du message d'erreur dans le corps de la réponse
        String expectedMessage = "Email existing@example.com already exists";
        assertTrue(response.getBody().contains(expectedMessage), "Expected message not found in response body");
    }
}
