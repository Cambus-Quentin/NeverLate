package com.demo.neverlate.repository;

import com.demo.neverlate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Référentiel (Repository) pour gérer les opérations CRUD sur l'entité {@link User}.
 * Ce repository fournit également une méthode personnalisée pour rechercher un utilisateur par son nom d'utilisateur.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur de l'utilisateur recherché
     * @return un objet {@link Optional} contenant l'utilisateur s'il existe, sinon un {@link Optional} vide
     */
    Optional<User> findByUsername(String username);

    /**
     * Trouve un utilisateur par son nom email.
     *
     * @param email l'email de l'utilisateur recherché
     * @return un objet {@link Optional} contenant l'utilisateur s'il existe, sinon un {@link Optional} vide
     */
    Optional<User> findByEmail(String email);
}
