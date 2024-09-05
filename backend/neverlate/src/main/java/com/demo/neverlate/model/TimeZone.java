package com.demo.neverlate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "time_zones")
@Getter
@Setter
public class TimeZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String offset;

    // Avec Lombok, plus besoin de définir manuellement les getters et les setters !
}
