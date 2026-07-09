package com.clubdeportivo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "inscripcion")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long id;

    @Column(name = "fech_inscripcion", nullable = false)
    private LocalDate fechInscripcion;

    @Column(name = "fech_inicio", nullable = false)
    private LocalDate fechInicio;

    @Column(name = "fech_fin", nullable = false)
    private LocalDate fechFin;

    @Column(name = "estado", nullable = false, length = 35)
    private String estado;

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("id_grupo")
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty("id_alumno")
    private Alumno alumno;
}
