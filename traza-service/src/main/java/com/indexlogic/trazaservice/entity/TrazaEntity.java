
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

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "traza_ruta", nullable = false)
    private String trazaRuta;

    @Column(name = "tam_linea", nullable = false)
    private int tamLinea;

    @Column(name = "num_conjuntos", nullable = false)
    private int numConjuntos;

    @Column(name = "vias", nullable = false)
    private int vias;

    @Column(name = "politica", nullable = false)
    private int politica;

    @Column(name = "hits", nullable = false)
    private Integer hits;

    @Column(name = "misses", nullable = false)
    private Integer misses;

    @Column(name = "aciertos", nullable = false)
    private long aciertos;

    @Column(name = "fallos", nullable = false)
    private long fallos;

    @Column(name = "reemplazos", nullable = false)
    private long reemplazos;

    @Column(name = "tiempo_total", nullable = false)
    private long tiempoTotal;

    @Column(name = "tasa_aciertos", nullable = false)
    private double tasaAciertos;

    @Column(name = "fecha_ejecucion")
    private LocalDateTime fechaEjecucion;

    @PrePersist
    public void antesDeGuardar() {
        if (fechaEjecucion == null) {
            fechaEjecucion = LocalDateTime.now();
        }
    }
}
