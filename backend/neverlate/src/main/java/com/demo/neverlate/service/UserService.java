package com.demo.neverlate.service;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.exception.DuplicateUserException;
import com.demo.neverlate.mapper.UserMapper;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer la création des utilisateurs.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

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
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new DuplicateUserException("Username " + userDTO.getUsername() + " already exists");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new DuplicateUserException("Email " + userDTO.getEmail() + " already exists");
        }

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encode the password

        userRepository.save(user);
    }
}
