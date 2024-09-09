package com.demo.neverlate.controller;

import com.demo.neverlate.dto.AuthenticationRequest;
import com.demo.neverlate.dto.AuthenticationResponse;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import com.demo.neverlate.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

    @PostMapping("/register")
    public AuthenticationResponse registerUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // Vérifier si le nom d'utilisateur ou l'email existe déjà
        if (userRepository.findByUsername(authenticationRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Création de l'utilisateur et encodage du mot de passe
        User newUser = new User();
        newUser.setUsername(authenticationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(authenticationRequest.getPassword()));
        newUser.setEmail(authenticationRequest.getEmail());
        userRepository.save(newUser);

        // Authentification automatique après l'inscription
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // Charger les détails de l'utilisateur pour le token JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }
}
