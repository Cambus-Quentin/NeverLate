package com.demo.neverlate.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) pour la gestion des fuseaux horaires.
 * Ce DTO est utilisé pour transférer les informations relatives aux fuseaux horaires entre la couche front-end et back-end.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeZoneDTO {

    /**
     * L'id' du fuseau horaire.
     */
    private Long id;

    /**
     * Le nom du fuseau horaire.
     * Ce champ ne doit pas être vide et doit comporter entre 3 et 100 caractères.
     */
    @NotEmpty(message = "Le nom est obligatoire")
    @Size(min = 3, max = 100, message = "Le nom doit comporter entre 3 et 100 caractères")
    private String label;

    /**
     * La ville associée au fuseau horaire.
     * Ce champ peut etre vide
     */
    private String city;

    /**
     * Le décalage horaire du fuseau par rapport à UTC.
     * Ce champ ne doit pas être vide et doit respecter le format +/-HH:mm (exemple : "+02:00").
     */
    @NotEmpty(message = "Le décalage horaire est obligatoire")
    @Pattern(regexp = "^(\\+|-)[0-9]{2}:[0-9]{2}$", message = "Le décalage horaire doit être au format +/-HH:mm")
    private String offset;
}
