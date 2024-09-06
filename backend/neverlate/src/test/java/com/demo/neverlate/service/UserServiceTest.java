package com.demo.neverlate.service;

import com.demo.neverlate.dto.UserDTO;
import com.demo.neverlate.exception.DuplicateUserException;
import com.demo.neverlate.mapper.UserMapper;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO validUserDTO;
    private User validUser;

    /**
     * Initialisation des objets avant chaque test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Utilisation de @Builder pour les objets User et UserDTO
        validUserDTO = UserDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .build();

        validUser = User.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .build();
    }

    /**
     * Test pour la sauvegarde d'un utilisateur avec succès.
     */
    @Test
    void saveUser_Success() {
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(validUser);
        when(passwordEncoder.encode(passwordCaptor.capture())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        userService.saveUser(validUserDTO);

        assertEquals("password123", passwordCaptor.getValue()); // Vérifie que le mot de passe non encodé est bien capturé
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(validUser);
        verify(userMapper, times(1)).toEntity(validUserDTO);
    }

    /**
     * Test pour vérifier que l'encodage du mot de passe est bien utilisé.
     */
    @Test
    void saveUser_PasswordEncoding() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(validUser);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.saveUser(validUserDTO);

        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(validUser);
    }

    /**
     * Test pour vérifier que la méthode save est appelée avec les bons arguments.
     */
    @Test
    void saveUser_CorrectArguments() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(validUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveUser(validUserDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("testuser", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals("test@example.com", capturedUser.getEmail());
    }

    /**
     * Test pour vérifier le comportement avec des entrées nulles.
     */
    @Test
    void saveUser_NullInput() {
        UserDTO nullUserDTO = UserDTO.builder()
                .username(null)
                .password(null)
                .email("test@example.com")
                .build();

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(new User());

        assertThrows(NullPointerException.class, () -> userService.saveUser(nullUserDTO));

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    /**
     * Test pour vérifier le comportement quand UserDTO est incomplet.
     */
    @Test
    void saveUser_IncompleteDTO() {
        UserDTO incompleteUserDTO = UserDTO.builder()
                .username("testuser")
                .password(null)
                .email("test@example.com")
                .build();

        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(new User());

        assertThrows(NullPointerException.class, () -> userService.saveUser(incompleteUserDTO));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Test pour vérifier que rien ne se passe si userMapper échoue.
     */
    @Test
    void saveUser_UserMapperFails() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> userService.saveUser(validUserDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Test pour vérifier qu'un utilisateur avec un email déjà utilisé ne peut être enregistré.
     */
    @Test
    void saveUser_EmailAlreadyExists() {
        when(userRepository.save(any(User.class))).thenThrow(new DuplicateUserException("Email already exists"));
        when(userMapper.toEntity(validUserDTO)).thenReturn(validUser);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        DuplicateUserException exception = assertThrows(DuplicateUserException.class, () -> userService.saveUser(validUserDTO));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
