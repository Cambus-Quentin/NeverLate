package com.demo.neverlate.mapper;

import com.demo.neverlate.dto.TimeZoneDTO;
import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre l'entité {@link TimeZone} et le DTO {@link TimeZoneDTO}.
 * Ce mapper permet de convertir les données entrantes sous forme de DTO en entités pour la base de données,
 * et les entités en DTO pour la transmission des données.
 */
@Component
public class TimeZoneMapper {

    /**
     * Convertit un {@link TimeZoneDTO} en une entité {@link TimeZone}.
     * Associe également le fuseau horaire à l'utilisateur courant.
     *
     * @param timeZoneDTO l'objet {@link TimeZoneDTO} à convertir
     * @param user l'utilisateur associé au fuseau horaire
     * @return l'entité {@link TimeZone} correspondante
     */
    public TimeZone toEntity(TimeZoneDTO timeZoneDTO, User user) {
        TimeZone timeZone = new TimeZone();
        timeZone.setName(timeZoneDTO.getName());
        timeZone.setCity(timeZoneDTO.getCity());
        timeZone.setOffset(timeZoneDTO.getOffset());
        timeZone.setUser(user);  // Associer le TimeZone à l'utilisateur courant
        return timeZone;
    }

    /**
     * Convertit une entité {@link TimeZone} en un DTO {@link TimeZoneDTO}.
     *
     * @param timeZone l'entité {@link TimeZone} à convertir
     * @return l'objet {@link TimeZoneDTO} correspondant
     */
    public TimeZoneDTO toDTO(TimeZone timeZone) {
        TimeZoneDTO timeZoneDTO = new TimeZoneDTO();
        timeZoneDTO.setName(timeZone.getName());
        timeZoneDTO.setCity(timeZone.getCity());
        timeZoneDTO.setOffset(timeZone.getOffset());
        return timeZoneDTO;
    }
}
