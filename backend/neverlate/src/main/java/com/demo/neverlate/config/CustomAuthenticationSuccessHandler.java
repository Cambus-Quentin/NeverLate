package com.demo.neverlate.security;

import com.demo.neverlate.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestionnaire personnalisé de succès d'authentification.
 * Cette classe est appelée après une authentification réussie pour mettre à jour certaines informations
 * liées à l'utilisateur, comme la date de la dernière connexion, et rediriger l'utilisateur vers une page spécifique.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    /**
     * Méthode appelée lorsque l'authentification est réussie.
     * Elle met à jour le champ "LastLogin" de l'utilisateur et le redirige vers la page d'accueil.
     *
     * @param request l'objet {@link HttpServletRequest} de la requête
     * @param response l'objet {@link HttpServletResponse} de la réponse
     * @param authentication l'objet {@link Authentication} contenant les informations d'authentification de l'utilisateur
     * @throws IOException si une erreur d'entrée/sortie survient
     * @throws ServletException si une erreur de servlet survient
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        // Récupèration de l'utilisateur par son nom d'utilisateur
        userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        response.sendRedirect("/");
    }
}
