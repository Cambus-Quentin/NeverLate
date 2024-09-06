package com.demo.neverlate.mapper;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre l'entité {@link User} et le DTO {@link UserDTO}.
 * Ce mapper permet de convertir les données utilisateur entrantes sous forme de DTO en entités pour la base de données,
 * et les entités en DTO pour la transmission des données.
 */
@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Convertit un {@link UserDTO} en une entité {@link User}.
     * Le mot de passe de l'utilisateur est encodé avant d'être sauvegardé.
     *
     * @param userDTO l'objet {@link UserDTO} à convertir
     * @return l'entité {@link User} correspondante
     */
    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Encodage du mot de passe
        user.setEmail(userDTO.getEmail());
        return user;
    }

    /**
     * Convertit une entité {@link User} en un DTO {@link UserDTO}.
     * Le mot de passe n'est pas renvoyé dans le DTO pour des raisons de sécurité.
     *
     * @param user l'entité {@link User} à convertir
     * @return l'objet {@link UserDTO} correspondant
     */
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        // Le mot de passe n'est pas renvoyé pour des raisons de sécurité
        return userDTO;
    }
}
