package com.demo.neverlate.utils;

import com.demo.neverlate.model.Role;
import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.model.User;
import com.demo.neverlate.repository.RoleRepository;
import com.demo.neverlate.repository.TimeZoneRepository;
import com.demo.neverlate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Classe utilisée pour peupler la base de données avec des utilisateurs, des rôles et des fuseaux horaires d'exemple lors du démarrage de l'application.
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Méthode exécutée au démarrage de l'application pour insérer des données d'exemple dans la base de données.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Créer des rôles
        Role adminRole = Role.builder().name("ADMIN").build();
        Role userRole = Role.builder().name("USER").build();
        roleRepository.saveAll(List.of(adminRole, userRole));

        // Créer les utilisateurs avec leurs fuseaux horaires
        createUsersWithTimeZones(adminRole, userRole);
    }

    private void createUsersWithTimeZones(Role adminRole, Role userRole) {
        // Créer les utilisateurs de test

        // Utilisateur 1: Admin avec un fuseau horaire
        User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin"))
                .roles(Set.of(adminRole))
                .build();
        userRepository.save(admin);

        timeZoneRepository.save(TimeZone.builder().label("UTC").city("London").offset("+00:00").user(admin).build());

        // Utilisateur 2: Utilisateur classique avec deux fuseaux horaires
        User user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user"))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);

        timeZoneRepository.save(TimeZone.builder().label("Eastern Time").city("New York").offset("-05:00").user(user).build());
        timeZoneRepository.save(TimeZone.builder().label("Central European Time").city("Paris").offset("+01:00").user(user).build());

        // Utilisateur 3
        User user3 = User.builder()
                .username("john_doe")
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("password123"))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user3);

        timeZoneRepository.save(TimeZone.builder().label("Pacific Time").city("Los Angeles").offset("-08:00").user(user3).build());
        timeZoneRepository.save(TimeZone.builder().label("Mountain Time").city("Denver").offset("-07:00").user(user3).build());
        timeZoneRepository.save(TimeZone.builder().label("Eastern Time").city("New York").offset("-05:00").user(user3).build());

        // Utilisateur 4
        User user4 = User.builder()
                .username("jane_doe")
                .email("jane.doe@example.com")
                .password(passwordEncoder.encode("password456"))
                .roles(Set.of(adminRole, userRole))
                .build();
        userRepository.save(user4);

        timeZoneRepository.save(TimeZone.builder().label("Greenwich Mean Time").city("London").offset("+00:00").user(user4).build());
        timeZoneRepository.save(TimeZone.builder().label("Central Standard Time").city("Chicago").offset("-06:00").user(user4).build());
        timeZoneRepository.save(TimeZone.builder().label("China Standard Time").city("Beijing").offset("+08:00").user(user4).build());

        // Utilisateur 5 à 10 : Ajout de plus d'utilisateurs avec des fuseaux horaires différents
        for (int i = 5; i <= 10; i++) {
            User userX = User.builder()
                    .username("user" + i)
                    .email("user" + i + "@example.com")
                    .password(passwordEncoder.encode("password" + i))
                    .roles(Set.of(userRole))
                    .build();
            userRepository.save(userX);

            timeZoneRepository.save(TimeZone.builder().label("UTC").city("London").offset("+00:00").user(userX).build());
            timeZoneRepository.save(TimeZone.builder().label("Central European Time").city("Berlin").offset("+01:00").user(userX).build());
            timeZoneRepository.save(TimeZone.builder().label("Eastern Time").city("New York").offset("-05:00").user(userX).build());
        }

        System.out.println("Utilisateurs, rôles et fuseaux horaires ajoutés dans la base de données.");
    }

}
