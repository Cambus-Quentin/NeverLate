package com.demo.neverlate.service;

import com.demo.neverlate.exception.InvalidTimeFormatException;
import com.demo.neverlate.exception.TimeZoneNotFoundException;
import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.TimeZoneRepository;
import com.demo.neverlate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TimeZoneConverterServiceTest {

    @Mock
    private TimeZoneRepository timeZoneRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TimeZoneConverterService timeZoneConverterService;

    private User mockUser;
    private TimeZone sourceZone;
    private TimeZone targetZone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler un utilisateur courant en utilisant @Builder
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        // Simuler le contexte de sécurité avec un utilisateur authentifié
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Simuler la récupération de l'utilisateur dans le UserRepository
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Initialiser les fuseaux horaires avec @Builder
        sourceZone = TimeZone.builder()
                .name("Pacific Time")
                .offset("-08:00")
                .user(mockUser)
                .build();

        targetZone = TimeZone.builder()
                .name("Eastern Time")
                .offset("-05:00")
                .user(mockUser)
                .build();
    }

    // Test pour conversion réussie entre deux fuseaux horaires
    @Test
    void convertTime_Success() {
        // Arrange
        String time = "2024-09-05T10:00:00"; // 10h à Los Angeles

        // Simuler les retours des repositories
        when(timeZoneRepository.findByNameAndUser("Pacific Time", mockUser)).thenReturn(Optional.of(sourceZone));
        when(timeZoneRepository.findByNameAndUser("Eastern Time", mockUser)).thenReturn(Optional.of(targetZone));

        // Act
        String result = timeZoneConverterService.convertTime("Pacific Time", "Eastern Time", time);

        // Assert
        assertEquals("2024-09-05T13:00:00", result); // 10h à Los Angeles = 13h à New York
        verify(timeZoneRepository, times(1)).findByNameAndUser("Pacific Time", mockUser);
        verify(timeZoneRepository, times(1)).findByNameAndUser("Eastern Time", mockUser);
    }

    // Test pour fuseau horaire source non trouvé
    @Test
    void convertTime_SourceZoneNotFound() {
        // Arrange
        String time = "2024-09-05T10:00:00"; // 10h à Los Angeles

        // Simuler le cas où le fuseau horaire source est introuvable
        when(timeZoneRepository.findByNameAndUser("Invalid Time", mockUser)).thenReturn(Optional.empty());
        when(timeZoneRepository.findByNameAndUser("Eastern Time", mockUser)).thenReturn(Optional.of(targetZone));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                timeZoneConverterService.convertTime("Invalid Time", "Eastern Time", time)
        );

        assertEquals("Invalid time zone name provided or time zone not owned by the user.", exception.getMessage());
        verify(timeZoneRepository, times(1)).findByNameAndUser("Invalid Time", mockUser);
    }

    // Test pour fuseau horaire cible non trouvé
    @Test
    void convertTime_TargetZoneNotFound() {
        // Arrange
        String time = "2024-09-05T10:00:00"; // 10h à Los Angeles

        // Simuler le cas où le fuseau horaire cible est introuvable
        when(timeZoneRepository.findByNameAndUser("Pacific Time", mockUser)).thenReturn(Optional.of(sourceZone));
        when(timeZoneRepository.findByNameAndUser("Invalid Time", mockUser)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                timeZoneConverterService.convertTime("Pacific Time", "Invalid Time", time)
        );

        assertEquals("Invalid time zone name provided or time zone not owned by the user.", exception.getMessage());
        verify(timeZoneRepository, times(1)).findByNameAndUser("Pacific Time", mockUser);
        verify(timeZoneRepository, times(1)).findByNameAndUser("Invalid Time", mockUser);
    }

    // Test pour entrée de l'heure invalide (format incorrect)
    @Test
    void convertTime_InvalidTimeFormat() {
        // Arrange
        String time = "invalid-format"; // Format incorrect

        // Simuler la récupération des zones
        when(timeZoneRepository.findByNameAndUser("Pacific Time", mockUser)).thenReturn(Optional.of(sourceZone));
        when(timeZoneRepository.findByNameAndUser("Eastern Time", mockUser)).thenReturn(Optional.of(targetZone));

        // Act & Assert
        assertThrows(InvalidTimeFormatException.class, () ->
                timeZoneConverterService.convertTime("Pacific Time", "Eastern Time", time)
        );
    }

    // Test pour gestion du cas où l'utilisateur courant est introuvable
    @Test
    void convertTime_UserNotFound() {
        // Arrange
        String time = "2024-09-05T10:00:00";

        // Simuler un utilisateur introuvable
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                timeZoneConverterService.convertTime("Pacific Time", "Eastern Time", time)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    // Test pour entrée null
    @Test
    void convertTime_NullInputs() {
        // Act & Assert
        assertThrows(TimeZoneNotFoundException.class, () ->
                timeZoneConverterService.convertTime(null, "Eastern Time", "2024-09-05T10:00:00")
        );
        assertThrows(TimeZoneNotFoundException.class, () ->
                timeZoneConverterService.convertTime("Pacific Time", null, "2024-09-05T10:00:00")
        );
        assertThrows(TimeZoneNotFoundException.class, () ->
                timeZoneConverterService.convertTime("Pacific Time", "Eastern Time", null)
        );
    }
}
