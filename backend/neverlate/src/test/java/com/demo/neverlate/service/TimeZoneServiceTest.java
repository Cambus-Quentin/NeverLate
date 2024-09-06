package com.demo.neverlate.service;

import com.demo.neverlate.dto.TimeZoneDTO;
import com.demo.neverlate.exception.TimeZoneNotFoundException;
import com.demo.neverlate.exception.UnauthorizedActionException;
import com.demo.neverlate.mapper.TimeZoneMapper;
import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.TimeZoneRepository;
import com.demo.neverlate.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TimeZoneServiceTest {

    @Mock
    private TimeZoneRepository timeZoneRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimeZoneMapper timeZoneMapper;

    @InjectMocks
    private TimeZoneService timeZoneService;

    private User mockUser;
    private TimeZone existingTimeZone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Créer un utilisateur mock avec @Builder
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        // Configurer le contexte de sécurité avec un utilisateur authentifié
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Simuler la récupération de l'utilisateur dans le UserRepository
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Créer un TimeZone existant avec @Builder
        existingTimeZone = TimeZone.builder()
                .name("Pacific Time")
                .city("Los Angeles")
                .offset("-08:00")
                .user(mockUser)
                .build();
    }

    // Test de la méthode saveTimeZone
    @Test
    void saveTimeZone() {
        // Arrange
        TimeZoneDTO timeZoneDTO = TimeZoneDTO.builder()
                .name("Pacific Time")
                .city("Los Angeles")
                .offset("-08:00")
                .build();

        TimeZone timeZoneToSave = TimeZone.builder()
                .name("Pacific Time")
                .city("Los Angeles")
                .offset("-08:00")
                .user(mockUser)
                .build();

        // Simuler les comportements des méthodes mapper et repository
        when(timeZoneMapper.toEntity(any(TimeZoneDTO.class), any(User.class))).thenReturn(timeZoneToSave);
        when(timeZoneRepository.save(any(TimeZone.class))).thenReturn(timeZoneToSave);
        when(timeZoneMapper.toDTO(any(TimeZone.class))).thenReturn(timeZoneDTO);

        // Act
        TimeZoneDTO savedTimeZoneDTO = timeZoneService.saveTimeZone(timeZoneDTO, mockUser);

        // Assert
        assertEquals("Pacific Time", savedTimeZoneDTO.getName());
        assertEquals("Los Angeles", savedTimeZoneDTO.getCity());
        assertEquals("-08:00", savedTimeZoneDTO.getOffset());
        verify(timeZoneRepository, times(1)).save(timeZoneToSave);
    }

    // Test de la méthode findById pour un TimeZone trouvé
    @Test
    void findById_Success() {
        // Arrange
        Long timeZoneId = 1L;

        // Simuler la récupération de l'entité par son ID
        when(timeZoneRepository.findById(timeZoneId)).thenReturn(Optional.of(existingTimeZone));

        // Simuler le mappage vers le DTO
        TimeZoneDTO timeZoneDTO = TimeZoneDTO.builder()
                .name("Pacific Time")
                .city("Los Angeles")
                .offset("-08:00")
                .build();
        when(timeZoneMapper.toDTO(existingTimeZone)).thenReturn(timeZoneDTO);

        // Act
        TimeZoneDTO result = timeZoneService.findById(timeZoneId);

        // Assert
        assertEquals("Pacific Time", result.getName());
        assertEquals("Los Angeles", result.getCity());
        assertEquals("-08:00", result.getOffset());
        verify(timeZoneRepository, times(1)).findById(timeZoneId);
    }

    // Test de la méthode findById pour un TimeZone introuvable
    @Test
    void findById_NotFound() {
        // Arrange
        Long timeZoneId = 1L;

        // Simuler un TimeZone introuvable
        when(timeZoneRepository.findById(timeZoneId)).thenReturn(Optional.empty());

        // Act & Assert
        TimeZoneNotFoundException exception = assertThrows(TimeZoneNotFoundException.class, () -> timeZoneService.findById(timeZoneId));

        assertEquals(String.format("Time zone not found with ID %d",timeZoneId), exception.getMessage());
        verify(timeZoneRepository, times(1)).findById(timeZoneId);
    }

    // Test de la méthode updateTimeZone
    @Test
    void updateTimeZone() {
        // Arrange
        Long timeZoneId = 1L;

        TimeZoneDTO updatedTimeZoneDTO = TimeZoneDTO.builder()
                .name("Eastern Time")
                .city("New York")
                .offset("-05:00")
                .build();

        // Simuler la récupération du TimeZone existant
        when(timeZoneRepository.findById(timeZoneId)).thenReturn(Optional.of(existingTimeZone));
        when(timeZoneRepository.save(existingTimeZone)).thenReturn(existingTimeZone);
        when(timeZoneMapper.toDTO(any(TimeZone.class))).thenReturn(updatedTimeZoneDTO);
        // Act
        TimeZoneDTO result = timeZoneService.updateTimeZone(timeZoneId, updatedTimeZoneDTO);

        // Assert
        assertEquals("Eastern Time", result.getName());
        assertEquals("New York", result.getCity());
        assertEquals("-05:00", result.getOffset());
        verify(timeZoneRepository, times(1)).save(existingTimeZone);
    }

    // Test de la méthode deleteById pour un TimeZone appartenant à l'utilisateur courant
    @Test
    void deleteById_Success() {
        // Arrange
        Long timeZoneId = 1L;

        // Simuler la récupération du TimeZone appartenant à l'utilisateur courant
        when(timeZoneRepository.findById(timeZoneId)).thenReturn(Optional.of(existingTimeZone));

        // Act
        timeZoneService.deleteById(timeZoneId);

        // Assert
        verify(timeZoneRepository, times(1)).deleteById(timeZoneId);
    }

    // Test de la méthode deleteById pour un TimeZone appartenant à un autre utilisateur
    @Test
    void deleteById_Unauthorized() {
        // Arrange
        Long timeZoneId = 1L;

        User anotherUser = User.builder()
                .id(2L)
                .username("anotheruser")
                .password("")
                .build();

        TimeZone timeZoneOwnedByAnotherUser = TimeZone.builder()
                .name("Eastern Time")
                .user(anotherUser)
                .offset("+03:00")
                .build();

        // Simuler la récupération du TimeZone appartenant à un autre utilisateur
        when(timeZoneRepository.findById(timeZoneId)).thenReturn(Optional.of(timeZoneOwnedByAnotherUser));

        // Act & Assert
        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, () -> {
            timeZoneService.deleteById(timeZoneId);
        });

        assertEquals("Unauthorized to delete this TimeZone", exception.getMessage());
        verify(timeZoneRepository, never()).deleteById(timeZoneId);
    }

    // Test de la méthode deleteById pour un TimeZone introuvable
    @Test
    void deleteById_NotFound() {
        // Arrange
        Long timeZoneId = 1L;

        // Simuler un TimeZone introuvable
        when(timeZoneRepository.findById(timeZoneId)).thenReturn(Optional.empty());

        // Act & Assert
        TimeZoneNotFoundException exception = assertThrows(TimeZoneNotFoundException.class, () -> timeZoneService.deleteById(timeZoneId));

        assertEquals(String.format("Time zone not found with ID %d",timeZoneId), exception.getMessage());
        verify(timeZoneRepository, times(1)).findById(timeZoneId);
        verify(timeZoneRepository, never()).deleteById(timeZoneId);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
