package com.demo.neverlate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Classe utilitaire pour la gestion des JWT.
 */
@Component
public class JwtTokenUtil {

    // Générer une clé sécurisée pour HS256
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    /**
     * Récupère le nom d'utilisateur (subject) à partir du token JWT.
     *
     * @param token le token JWT
     * @return le nom d'utilisateur contenu dans le token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Récupère la date d'expiration du token JWT.
     *
     * @param token le token JWT
     * @return la date d'expiration du token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Récupère une revendication spécifique du token JWT.
     *
     * @param token          le token JWT
     * @param claimsResolver la fonction permettant de récupérer une revendication
     * @param <T>            le type de la revendication
     * @return la valeur de la revendication
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Récupère toutes les revendications (claims) du token JWT.
     *
     * @param token le token JWT
     * @return les revendications contenues dans le token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le token JWT a expiré.
     *
     * @param token le token JWT
     * @return true si le token est expiré, false sinon
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Génère un token JWT pour un utilisateur donné.
     *
     * @param username le nom d'utilisateur pour lequel le token est généré
     * @return le token JWT généré
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Génère un token JWT pour les détails d'un utilisateur.
     *
     * @param userDetails les détails de l'utilisateur
     * @return le token JWT généré
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    /**
     * Crée un token JWT avec les revendications et le sujet donnés.
     *
     * @param claims   les revendications à inclure dans le token
     * @param subject  le sujet (nom d'utilisateur)
     * @return le token JWT généré
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Valide un token JWT en vérifiant l'utilisateur et la date d'expiration.
     *
     * @param token       le token JWT à valider
     * @param userDetails les détails de l'utilisateur
     * @return true si le token est valide, false sinon
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
