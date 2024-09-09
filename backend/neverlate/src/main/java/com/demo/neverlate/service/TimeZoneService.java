package com.demo.neverlate.service;

import com.demo.neverlate.dto.TimeZoneDTO;
import com.demo.neverlate.exception.TimeZoneNotFoundException;
import com.demo.neverlate.exception.UnauthorizedActionException;
import com.demo.neverlate.mapper.TimeZoneMapper;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les fuseaux horaires (TimeZone) pour les utilisateurs.
 */
@Service
public class TimeZoneService {

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeZoneMapper timeZoneMapper;

    /**
     * Récupère tous les fuseaux horaires associés à l'utilisateur courant.
     *
     * @return Liste de TimeZoneDTO pour l'utilisateur courant.
     */
    @Operation(summary = "Retrieve all timezones for the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time zones retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<TimeZoneDTO> findAllForCurrentUser() {
        User currentUser = getCurrentUser();
        List<TimeZone> timeZones = timeZoneRepository.findByUser(currentUser);
        return timeZones.stream().map(timeZoneMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Récupère un fuseau horaire par son identifiant.
     *
     * @param id L'identifiant du fuseau horaire.
     * @return Le TimeZoneDTO associé.
     * @throws TimeZoneNotFoundException si le fuseau horaire n'est pas trouvé.
     */
    @Operation(summary = "Find a timezone by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time zone found"),
            @ApiResponse(responseCode = "404", description = "Time zone not found")
    })
    public TimeZoneDTO findById(Long id) {
        TimeZone timeZone = timeZoneRepository.findById(id)
                .orElseThrow(() -> new TimeZoneNotFoundException("Time zone not found with ID " + id));
        return timeZoneMapper.toDTO(timeZone);
    }

    /**
     * Crée et sauvegarde un fuseau horaire pour l'utilisateur courant.
     *
     * @param timeZoneDTO Le DTO contenant les informations du fuseau horaire.
     * @param currentUser L'utilisateur courant.
     * @return Le TimeZoneDTO du fuseau horaire créé.
     */
    @Operation(summary = "Create a new timezone for the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Time zone created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public TimeZoneDTO saveTimeZone(TimeZoneDTO timeZoneDTO, User currentUser) {
        TimeZone timeZone = timeZoneMapper.toEntity(timeZoneDTO, currentUser);
        TimeZone savedTimeZone = timeZoneRepository.save(timeZone);
        return timeZoneMapper.toDTO(savedTimeZone);
    }

    /**
     * Met à jour un fuseau horaire existant.
     *
     * @param id L'identifiant du fuseau horaire à mettre à jour.
     * @param updatedTimeZoneDTO Le DTO contenant les nouvelles informations.
     * @return Le TimeZoneDTO mis à jour.
     * @throws TimeZoneNotFoundException si le fuseau horaire n'est pas trouvé.
     * @throws UnauthorizedActionException si l'utilisateur courant n'est pas autorisé à modifier ce fuseau horaire.
     */
    @Operation(summary = "Update an existing timezone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time zone updated successfully"),
            @ApiResponse(responseCode = "404", description = "Time zone not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to update this time zone")
    })
    public TimeZoneDTO updateTimeZone(Long id, TimeZoneDTO updatedTimeZoneDTO) {
        TimeZone existingTimeZone = timeZoneRepository.findById(id)
                .orElseThrow(() -> new TimeZoneNotFoundException("Time zone not found with ID " + id));

        User currentUser = getCurrentUser();
        if (!existingTimeZone.getUser().equals(currentUser)) {
            throw new UnauthorizedActionException("Unauthorized to update this TimeZone");
        }

        existingTimeZone.setLabel(updatedTimeZoneDTO.getLabel());
        existingTimeZone.setCity(updatedTimeZoneDTO.getCity());
        existingTimeZone.setOffset(updatedTimeZoneDTO.getOffset());

        TimeZone updatedTimeZone = timeZoneRepository.save(existingTimeZone);
        return timeZoneMapper.toDTO(updatedTimeZone);
    }

    /**
     * Supprime un fuseau horaire par son identifiant.
     *
     * @param id L'identifiant du fuseau horaire à supprimer.
     * @throws TimeZoneNotFoundException si le fuseau horaire n'est pas trouvé.
     * @throws UnauthorizedActionException si l'utilisateur courant n'est pas autorisé à supprimer ce fuseau horaire.
     */
    @Operation(summary = "Delete a timezone by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time zone deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Time zone not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to delete this time zone")
    })
    public void deleteById(Long id) {
        TimeZone timeZone = timeZoneRepository.findById(id)
                .orElseThrow(() -> new TimeZoneNotFoundException("Time zone not found with ID " + id));

        User currentUser = getCurrentUser();
        if (!timeZone.getUser().equals(currentUser)) {
            throw new UnauthorizedActionException("Unauthorized to delete this TimeZone");
        }

        timeZoneRepository.deleteById(id);
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
