package com.clubdeportivo.dtos.alumno;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechNacimiento;
    private String rut;
    private String saludObservaciones;
    private Boolean activo;
    private LocalDate fechInscripcion;
    private Set<Long> apoderadosIds;
}
