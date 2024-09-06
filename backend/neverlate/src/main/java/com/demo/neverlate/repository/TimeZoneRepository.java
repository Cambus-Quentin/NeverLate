package com.demo.neverlate.repository;

import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Référentiel (Repository) pour gérer les opérations CRUD sur l'entité {@link TimeZone}.
 * Ce repository fournit également des méthodes personnalisées pour rechercher des fuseaux horaires par utilisateur.
 */
@Repository
public interface TimeZoneRepository extends JpaRepository<TimeZone, Long> {

    /**
     * Trouve tous les fuseaux horaires associés à un utilisateur donné.
     *
     * @param user l'utilisateur dont on veut récupérer les fuseaux horaires
     * @return une liste de fuseaux horaires associés à l'utilisateur
     */
    List<TimeZone> findByUser(User user);

    /**
     * Trouve un fuseau horaire par son nom et l'utilisateur associé.
     *
     * @param name le nom du fuseau horaire
     * @param user l'utilisateur associé à ce fuseau horaire
     * @return un objet {@link Optional} contenant le fuseau horaire s'il existe
     */
    Optional<TimeZone> findByNameAndUser(String name, User user);
}
