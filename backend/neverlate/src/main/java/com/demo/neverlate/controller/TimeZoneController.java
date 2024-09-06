package com.demo.neverlate.controller;

import com.demo.neverlate.dto.TimeZoneDTO;
import com.demo.neverlate.model.User;
import com.demo.neverlate.service.TimeZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les fuseaux horaires (TimeZones) de l'utilisateur.
 * Il permet à l'utilisateur de créer, modifier, supprimer et consulter ses fuseaux horaires.
 */
@RestController
@RequestMapping("/api/timezones")
@Tag(name = "TimeZone Controller", description = "Endpoints pour la gestion des fuseaux horaires des utilisateurs")
public class TimeZoneController {

    @Autowired
    private TimeZoneService timeZoneService;

    /**
     * Récupère tous les fuseaux horaires de l'utilisateur courant.
     *
     * @return une liste de {@link TimeZoneDTO} représentant les fuseaux horaires de l'utilisateur
     */
    @Operation(summary = "Récupérer tous les fuseaux horaires de l'utilisateur courant")
    @GetMapping("/user/timezones")
    public ResponseEntity<List<TimeZoneDTO>> getUserTimeZones() {
        List<TimeZoneDTO> timeZones = timeZoneService.findAllForCurrentUser(); // Renvoie les TimeZones en DTO
        return ResponseEntity.ok(timeZones);
    }

    /**
     * Récupère un fuseau horaire spécifique par son ID.
     *
     * @param id l'ID du fuseau horaire à récupérer
     * @return une réponse contenant le {@link TimeZoneDTO} du fuseau horaire
     */
    @Operation(summary = "Récupérer un fuseau horaire par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<TimeZoneDTO> getTimeZoneById(@PathVariable Long id) {
        return ResponseEntity.ok(timeZoneService.findById(id)); // Renvoie le TimeZone sous forme de DTO
    }

    /**
     * Crée un nouveau fuseau horaire pour l'utilisateur courant.
     *
     * @param timeZoneDTO le DTO contenant les informations du fuseau horaire à créer
     * @return une réponse confirmant la création du fuseau horaire
     */
    @Operation(summary = "Créer un nouveau fuseau horaire pour l'utilisateur courant")
    @PostMapping
    public ResponseEntity<String> createTimeZone(@Valid @RequestBody TimeZoneDTO timeZoneDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();  // Récupère l'utilisateur authentifié
        timeZoneService.saveTimeZone(timeZoneDTO, currentUser); // Sauvegarde le TimeZone
        return ResponseEntity.ok("TimeZone créé avec succès");
    }

    /**
     * Met à jour un fuseau horaire existant pour l'utilisateur courant.
     *
     * @param id l'ID du fuseau horaire à mettre à jour
     * @param updatedTimeZoneDTO le DTO contenant les nouvelles informations du fuseau horaire
     * @return le {@link TimeZoneDTO} mis à jour
     */
    @Operation(summary = "Mettre à jour un fuseau horaire existant")
    @PutMapping("/{id}")
    public ResponseEntity<TimeZoneDTO> updateTimeZone(
            @PathVariable Long id, @Valid @RequestBody TimeZoneDTO updatedTimeZoneDTO) {
        TimeZoneDTO updatedTimeZone = timeZoneService.updateTimeZone(id, updatedTimeZoneDTO);
        return ResponseEntity.ok(updatedTimeZone);
    }

    /**
     * Supprime un fuseau horaire par son ID.
     *
     * @param id l'ID du fuseau horaire à supprimer
     * @return une réponse de succès sans contenu
     */
    @Operation(summary = "Supprimer un fuseau horaire par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeZone(@PathVariable Long id) {
        timeZoneService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
