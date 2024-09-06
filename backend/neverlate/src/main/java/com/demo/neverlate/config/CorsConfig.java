package com.demo.neverlate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration du partage de ressources entre origines multiples (CORS).
 * Cette configuration permet à l'application d'accepter des requêtes cross-origin
 * provenant d'une origine spécifiée (http://localhost:8080) et autorise certaines méthodes HTTP.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configure les règles CORS pour autoriser les requêtes cross-origin.
     *
     * @param registry le {@link CorsRegistry} dans lequel la configuration CORS est ajoutée
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
