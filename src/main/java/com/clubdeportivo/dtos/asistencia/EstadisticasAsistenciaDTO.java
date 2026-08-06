package com.clubdeportivo.dtos.asistencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasAsistenciaDTO {
    private Long total;
    private Long presentes;
    private Long ausentes;
    private List<Object[]> asistenciasPorClase;
    private List<Object[]> asistenciasPorGrupo;
    private List<Object[]> asistenciasPorMes;
    private List<Object[]> porcentajeAsistenciaPorClase;
    private List<Object[]> porcentajeAsistenciaPorGrupo;
}
