package com.demo.neverlate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité pour l'application.
 * Cette classe configure les aspects de sécurité tels que l'authentification, la gestion des utilisateurs,
 * le chiffrement des mots de passe et les règles d'autorisation.
 */
@Configuration
public class SecurityConfig {

    /**
     * Définit la chaîne de filtres de sécurité pour l'application.
     * Cette méthode configure les endpoints accessibles sans authentification, désactive la protection CSRF
     * et configure la gestion des frames pour la console H2.
     *
     * @param http l'objet {@link HttpSecurity} utilisé pour configurer les règles de sécurité
     * @return l'objet {@link SecurityFilterChain} représentant la configuration de la chaîne de filtres de sécurité
     * @throws Exception en cas d'erreur lors de la configuration de la sécurité
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Désactiver la protection CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/register", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()  // Permettre l'accès à Swagger et à la console H2
                        .anyRequest().authenticated()  // Requérir l'authentification pour toutes les autres requêtes
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)  // Autoriser l'accès à la page de connexion pour tous
                .logout(LogoutConfigurer::permitAll)  // Autoriser la déconnexion pour tous
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));  // Désactiver les options de frame pour permettre l'accès à H2

        return http.build();
    }

    /**
     * Fournit un service pour charger les informations d'utilisateur via {@link UserDetailsService}.
     * Ce service est utilisé par Spring Security pour authentifier les utilisateurs.
     *
     * @return une instance de {@link UserDetailsService} qui charge les utilisateurs
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    /**
     * Fournit un encodeur de mots de passe utilisant l'algorithme BCrypt.
     * Cet encodeur est utilisé pour sécuriser les mots de passe dans la base de données.
     *
     * @return une instance de {@link PasswordEncoder} utilisant BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Crée et configure un fournisseur d'authentification DAO.
     * Ce fournisseur d'authentification utilise {@link UserDetailsService} et {@link PasswordEncoder}
     * pour authentifier les utilisateurs.
     *
     * @return une instance de {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Fournit un gestionnaire d'authentification pour gérer les processus d'authentification.
     * Il est récupéré à partir de la configuration d'authentification existante.
     *
     * @param authConfig l'objet {@link AuthenticationConfiguration} pour configurer l'authentification
     * @return une instance d'{@link AuthenticationManager}
     * @throws Exception en cas d'erreur lors de la récupération du gestionnaire d'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
