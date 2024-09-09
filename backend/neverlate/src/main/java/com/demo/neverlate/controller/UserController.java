package com.demo.neverlate.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer l'inscription des utilisateurs.
 * Permet de créer de nouveaux utilisateurs en passant par un formulaire d'inscription.
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "Endpoints pour la gestion des utilisateurs")
public class UserController {
}
