package com.demo.neverlate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un utilisateur dans le système.
 * Chaque utilisateur possède un ensemble de rôles et peut avoir plusieurs fuseaux horaires personnalisés.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * L'ID unique de l'utilisateur, généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Le nom d'utilisateur, obligatoire et unique.
     */
    @NonNull
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Le mot de passe de l'utilisateur, obligatoire.
     */
    @NonNull
    @Column(nullable = false)
    private String password;

    /**
     * L'adresse e-mail de l'utilisateur, obligatoire et unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * La date de création du compte utilisateur.
     * Ce champ est automatiquement initialisé lors de la création de l'utilisateur et ne peut pas être modifié.
     */
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private final Date createdAt = new Date();

    /**
     * La date de la dernière connexion de l'utilisateur.
     * Ce champ est mis à jour à chaque fois que l'utilisateur se connecte.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastLogin;

    /**
     * Les rôles attribués à l'utilisateur, comme "USER" ou "ADMIN".
     * Relation Many-to-Many avec l'entité {@link Role}.
     */
    @ManyToMany(fetch = FetchType.EAGER)

    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Les fuseaux horaires personnalisés associés à l'utilisateur.
     * Relation One-to-Many avec l'entité {@link TimeZone}, un utilisateur peut avoir plusieurs fuseaux horaires.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TimeZone> timeZones = new HashSet<>();

}
