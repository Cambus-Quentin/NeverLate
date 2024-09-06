package com.demo.neverlate.controller;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer l'inscription des utilisateurs.
 * Permet de créer de nouveaux utilisateurs en passant par un formulaire d'inscription.
 */
@RestController
@Tag(name = "User Controller", description = "Endpoints pour la gestion des utilisateurs")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint pour enregistrer un nouvel utilisateur.
     * Reçoit les informations de l'utilisateur sous forme de {@link UserDTO}, les valide et sauvegarde l'utilisateur.
     *
     * @param userDTO le DTO contenant les informations de l'utilisateur à enregistrer
     * @return une réponse confirmant l'enregistrement de l'utilisateur
     */
    @Operation(summary = "Enregistrer un nouvel utilisateur")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO); // Sauvegarde l'utilisateur à partir du DTO
        return ResponseEntity.ok("User registered successfully");
    }
}
