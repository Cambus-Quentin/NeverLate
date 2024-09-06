package com.demo.neverlate.controller;

import com.demo.neverlate.service.TimeZoneConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer la conversion d'heures entre différents fuseaux horaires.
 * Il expose un endpoint permettant de convertir l'heure d'un fuseau horaire source à un fuseau horaire cible.
 */
@RestController
@Tag(name = "TimeZone Converter", description = "Endpoint pour la conversion d'heures entre deux fuseaux horaires")
public class TimeZoneConverterController {

    @Autowired
    private TimeZoneConverterService timeZoneConverterService;

    /**
     * Convertit une heure donnée d'un fuseau horaire source à un fuseau horaire cible.
     *
     * @param sourceZone le nom du fuseau horaire source
     * @param targetZone le nom du fuseau horaire cible
     * @param time l'heure à convertir, au format ISO (ex : "2024-09-05T15:30:00")
     * @return une réponse contenant l'heure convertie dans le fuseau horaire cible
     */
    @Operation(summary = "Convertir l'heure entre deux fuseaux horaires")
    @GetMapping("/convert-time")
    public ResponseEntity<String> convertTime(
            @RequestParam String sourceZone,
            @RequestParam String targetZone,
            @RequestParam String time) {

        // Appelle le service de conversion d'heure
        String convertedTime = timeZoneConverterService.convertTime(sourceZone, targetZone, time);
        return ResponseEntity.ok(convertedTime);
    }
}
