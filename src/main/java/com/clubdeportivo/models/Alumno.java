package com.clubdeportivo.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "alumno")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 35)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 35)
    private String apellido;

    @Column(name = "fech_nacimiento", nullable = false)
    private LocalDate fechNacimiento;

    @Column(name = "rut", nullable = false, length = 12)
    private String rut;

    @Column(name = "salud_observaciones", nullable = false, columnDefinition = "TEXT")
    private String saludObservaciones;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "fech_inscripcion", nullable = false)
    private LocalDate fechInscripcion;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "alumnos_tutores", joinColumns = @JoinColumn(name = "alumno_id", referencedColumnName = "id_alumno")
            , inverseJoinColumns = @JoinColumn(name = "apoderado_id", referencedColumnName = "id_apoderado"))
    @Builder.Default
    private Set<Apoderado> apoderados = new HashSet<>();
}