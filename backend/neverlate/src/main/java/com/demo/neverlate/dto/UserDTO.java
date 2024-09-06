package com.demo.neverlate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) pour la gestion des utilisateurs.
 * Ce DTO est utilisé pour transférer les informations relatives aux utilisateurs lors de l'inscription ou de la mise à jour des données.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    /**
     * Le nom d'utilisateur.
     * Ce champ ne doit pas être vide et doit comporter entre 3 et 50 caractères.
     */
    @NotEmpty(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit comporter entre 3 et 50 caractères")
    private String username;

    /**
     * Le mot de passe de l'utilisateur.
     * Ce champ ne doit pas être vide et doit comporter au moins 6 caractères.
     */
    @NotEmpty(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit comporter au moins 6 caractères")
    private String password;

    /**
     * L'adresse e-mail de l'utilisateur.
     * Ce champ doit être un e-mail valide et ne doit pas être vide.
     */
    @Email(message = "Format d'e-mail invalide")
    @NotEmpty(message = "L'e-mail est obligatoire")
    private String email;

}
