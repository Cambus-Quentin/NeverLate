package com.demo.neverlate.controller;

import com.demo.neverlate.dto.AuthenticationRequest;
import com.demo.neverlate.dto.AuthenticationResponse;
import com.demo.neverlate.model.User;
import com.demo.neverlate.security.JwtTokenUtil;
import com.demo.neverlate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // Authentification
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // Charger les détails de l'utilisateur
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        // Mettre à jour le champ lastLogin pour l'utilisateur
        Optional<User> userOpt = userService.findByUsername(authenticationRequest.getUsername());
        userOpt.ifPresent(userService::updateLastLogin);

        return new AuthenticationResponse(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // Vérifier si le nom d'utilisateur existe déjà
        if (userService.findByUsername(authenticationRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Création du nouvel utilisateur
        User newUser = new User();
        newUser.setUsername(authenticationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(authenticationRequest.getPassword()));
        newUser.setEmail(authenticationRequest.getEmail());

        // Enregistrer l'utilisateur et mettre à jour lastLogin
        userService.registerNewUser(newUser);

        // Authentification automatique après l'inscription
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // Générer le token JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.CREATED);
    }
}
