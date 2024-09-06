package com.demo.neverlate.service;

import com.demo.neverlate.exception.InvalidTimeFormatException;
import com.demo.neverlate.exception.TimeZoneNotFoundException;
import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.TimeZoneRepository;
import com.demo.neverlate.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Service pour convertir l'heure d'un fuseau horaire source vers un fuseau horaire cible.
 */
@Service
public class TimeZoneConverterService {

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Convertit une heure d'un fuseau horaire source à un fuseau horaire cible pour l'utilisateur courant.
     *
     * @param sourceZoneName Le nom du fuseau horaire source.
     * @param targetZoneName Le nom du fuseau horaire cible.
     * @param time           L'heure à convertir.
     * @return L'heure convertie dans le fuseau horaire cible.
     * @throws TimeZoneNotFoundException  Si le fuseau horaire n'est pas trouvé.
     * @throws InvalidTimeFormatException Si le format de l'heure est invalide.
     */
    @Operation(summary = "Convert time from one timezone to another for the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time converted successfully"),
            @ApiResponse(responseCode = "404", description = "Time zone not found"),
            @ApiResponse(responseCode = "400", description = "Invalid time format")
    })
    public String convertTime(String sourceZoneName, String targetZoneName, String time) {
        User currentUser = getCurrentUser();

        Optional<TimeZone> sourceZone = timeZoneRepository.findByNameAndUser(sourceZoneName, currentUser);
        Optional<TimeZone> targetZone = timeZoneRepository.findByNameAndUser(targetZoneName, currentUser);

        if (sourceZone.isEmpty() || targetZone.isEmpty()) {
            throw new TimeZoneNotFoundException("Invalid time zone name provided or time zone not owned by the user.");
        }

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            ZoneOffset sourceOffset = ZoneOffset.of(sourceZone.get().getOffset());
            ZoneOffset targetOffset = ZoneOffset.of(targetZone.get().getOffset());

            ZonedDateTime sourceDateTime = localDateTime.atOffset(sourceOffset).toZonedDateTime();
            ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(targetOffset);

            return targetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new InvalidTimeFormatException("The provided time format is invalid: " + time);
        }
    }

    /**
     * Récupère l'utilisateur courant à partir du contexte de sécurité.
     *
     * @return L'utilisateur courant.
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
