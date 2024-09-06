package com.demo.neverlate.repository;

import com.demo.neverlate.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Référentiel (Repository) pour gérer les opérations CRUD sur l'entité {@link Role}.
 * Cette interface hérite de {@link JpaRepository}, qui fournit des méthodes de base pour manipuler les rôles
 * dans la base de données, comme la sauvegarde, la suppression, et la recherche.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
