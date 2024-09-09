package com.demo.neverlate.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un fuseau horaire personnalisé associé à un utilisateur.
 * Chaque utilisateur peut avoir plusieurs fuseaux horaires associés, et ces fuseaux horaires
 * sont utilisés pour gérer les conversions d'heures dans différentes villes.
 */
@Entity
@Table(name = "time_zones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class TimeZone {

    /**
     * L'ID unique du fuseau horaire, généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Le nom du fuseau horaire, qui ne peut pas être nul.
     * Par exemple, "Pacific Standard Time" ou "Europe/Paris".
     */
    @NonNull
    @Column(nullable = false)
    private String label;

    /**
     * La ville associée au fuseau horaire.
     * Ce champ est facultatif (nullable = true).
     */
    @Column(nullable = true)
    private String city;

    /**
     * Le décalage horaire par rapport à UTC (format +/-HH:mm), qui ne peut pas être nul.
     * Par exemple, "+02:00" pour un décalage de 2 heures par rapport à UTC.
     */
    @NonNull
    @Column(name = "timezone_offset", nullable = false)
    private String offset;

    /**
     * L'utilisateur auquel ce fuseau horaire est associé.
     * Ce champ est obligatoire et utilise une relation Many-to-One (un utilisateur peut avoir plusieurs fuseaux horaires).
     */
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
