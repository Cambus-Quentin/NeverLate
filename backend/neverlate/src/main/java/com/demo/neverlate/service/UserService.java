package com.demo.neverlate.service;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.exception.DuplicateUserException;
import com.demo.neverlate.mapper.UserMapper;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service pour gérer la création des utilisateurs.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param userRepository Le repository des utilisateurs
     * @param passwordEncoder L'encodeur de mots de passe
     * @param userMapper Le mapper pour convertir entre UserDTO et User
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Sauvegarde un nouvel utilisateur.
     *
     * @param userDTO Le DTO contenant les informations de l'utilisateur.
     * @throws DuplicateUserException Si le nom d'utilisateur ou l'email existe déjà.
     */
    @Operation(summary = "Save a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User saved successfully"),
            @ApiResponse(responseCode = "409", description = "Duplicate username or email")
    })
    public void saveUser(UserDTO userDTO) {
        // Vérification si le nom d'utilisateur existe déjà
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new DuplicateUserException("Username " + userDTO.getUsername() + " already exists");
        }

        // Vérification si l'email existe déjà
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new DuplicateUserException("Email " + userDTO.getEmail() + " already exists");
        }

        // Convertir le DTO en entité User et encoder le mot de passe
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Sauvegarder l'utilisateur dans la base de données
        userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à rechercher.
     * @return Un Optional contenant l'utilisateur s'il existe, sinon un Optional vide.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Met à jour la date de dernière connexion de l'utilisateur.
     *
     * @param user L'utilisateur pour lequel mettre à jour le champ lastLogin.
     */
    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now()); // Met à jour le champ lastLogin avec l'heure actuelle
        userRepository.save(user); // Enregistre les modifications
    }

    /**
     * Enregistre un nouvel utilisateur en mettant à jour la date de dernière connexion.
     *
     * @param user L'entité utilisateur à enregistrer.
     * @return L'utilisateur enregistré avec la date de dernière connexion mise à jour.
     */
    public User registerNewUser(User user) {
        // Vérification si le nom d'utilisateur existe déjà
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException("Username " + user.getUsername() + " already exists");
        }

        // Vérification si l'email existe déjà
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateUserException("Email " + user.getEmail() + " already exists");
        }
        user.setLastLogin(LocalDateTime.now()); // Définit lastLogin lors de l'enregistrement
        return userRepository.save(user);
    }
}
