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

import java.util.Set;

/**
 * Classe utilisée pour peupler la base de données avec des utilisateurs, des rôles et des fuseaux horaires d'exemple lors du démarrage de l'application.
 * Elle implémente l'interface {@link CommandLineRunner} pour exécuter du code au lancement.
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
     *
     * @param args arguments de la ligne de commande
     * @throws Exception si une erreur survient lors de l'exécution
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Créer des rôles
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);

        // Ajouter un administrateur
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Set.of(adminRole)); // Assigner le rôle ADMIN à admin
        userRepository.save(admin);

        // Ajouter un utilisateur classique
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRoles(Set.of(userRole)); // Assigner le rôle USER à user
        userRepository.save(user);

        // Ajouter un utilisateur avec rôle USER (john_doe)
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setRoles(Set.of(userRole)); // Assigner le rôle USER à john_doe
        userRepository.save(user1);

        // Ajouter un utilisateur avec rôles ADMIN et USER (jane_doe)
        User user2 = new User();
        user2.setUsername("jane_doe");
        user2.setEmail("jane.doe@example.com");
        user2.setPassword(passwordEncoder.encode("password456"));
        user2.setRoles(Set.of(adminRole, userRole)); // Assigner les rôles ADMIN et USER à jane_doe
        userRepository.save(user2);

        // Ajouter des fuseaux horaires pour john_doe
        TimeZone timeZone1 = new TimeZone();
        timeZone1.setName("Pacific Time");
        timeZone1.setCity("Los Angeles");
        timeZone1.setOffset("-08:00");
        timeZone1.setUser(user1);
        timeZoneRepository.save(timeZone1);

        TimeZone timeZone2 = new TimeZone();
        timeZone2.setName("Eastern Time");
        timeZone2.setCity("New York");
        timeZone2.setOffset("-05:00");
        timeZone2.setUser(user1);
        timeZoneRepository.save(timeZone2);

        // Ajouter un fuseau horaire pour jane_doe
        TimeZone timeZone3 = new TimeZone();
        timeZone3.setName("GMT");
        timeZone3.setCity("London");
        timeZone3.setOffset("+00:00");
        timeZone3.setUser(user2);
        timeZoneRepository.save(timeZone3);

        System.out.println("Utilisateurs, rôles et fuseaux horaires ajoutés dans la base de données.");
    }
}
