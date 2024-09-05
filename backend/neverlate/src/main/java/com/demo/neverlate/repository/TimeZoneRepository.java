package com.demo.neverlate.repository;

import com.demo.neverlate.model.TimeZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends JpaRepository<TimeZone, Long> {
    // Vous pouvez ajouter d'autres méthodes spécifiques ici, si nécessaire
}
