package com.clubdeportivo.dtos.profesorgrupo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class GrupoResumenProfesorDTO {
    private Long idGrupo;
    private String nombreGrupo;
    private String horaInicio;
    private String horaFin;
    private String diasSemana;
    private Long alumnosInscritos;
    private Integer capacidadMaxima;
}
