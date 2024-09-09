package com.demo.neverlate.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un rôle dans le système de gestion des utilisateurs.
 * Un rôle est associé à plusieurs utilisateurs et est utilisé pour déterminer les droits d'accès dans l'application.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    /**
     * L'ID unique du rôle, généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Le nom du rôle, comme "USER" ou "ADMIN".
     * Ce champ est unique et ne peut pas être nul.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * La liste des utilisateurs associés à ce rôle.
     * Cette relation Many-to-Many est mappée par le champ "roles" de l'entité {@link User}.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
