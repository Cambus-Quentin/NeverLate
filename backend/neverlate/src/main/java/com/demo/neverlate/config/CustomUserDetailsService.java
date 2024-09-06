package com.demo.neverlate.config;

import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.demo.neverlate.model.Role;

/**
 * Service personnalisé qui implémente {@link UserDetailsService}.
 * Ce service est utilisé par Spring Security pour récupérer les informations d'un utilisateur
 * à partir de la base de données lors de l'authentification.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge un utilisateur par son nom d'utilisateur (username).
     * Cette méthode est utilisée par Spring Security pour authentifier un utilisateur.
     *
     * @param username le nom d'utilisateur de l'utilisateur cherché
     * @return les détails de l'utilisateur, sous forme d'objet {@link UserDetails}
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convertir les rôles de l'utilisateur en authorities pour Spring Security
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new))
                .build();
    }
}
