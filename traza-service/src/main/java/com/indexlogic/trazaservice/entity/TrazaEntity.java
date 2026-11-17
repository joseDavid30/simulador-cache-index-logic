package com.indexlogic.trazaservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "trazas_simulacion")
@Data
public class TrazaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trazaRuta;

    @Column(nullable = false)
    private int capacidad;

    private int hits;
    private int misses;
    private long tiempoTotal;
    private double tasaAciertos;

    private LocalDateTime fechaEjecucion = LocalDateTime.now();
}