package com.clubdeportivo.dtos.profesorgrupo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasProfesorGrupoDTO {
    private Long total;
    private Long activos;
    private Long inactivos;
    private List<Object[]> asignacionesPorGrupo;
    private List<Object[]> asignacionesActivasPorGrupo;
    private List<Object[]> asignacionesPorUsuario;
    private List<Object[]> asignacionesActivasPorUsuario;
    private List<Object[]> asignacionesPorCategoria;
    private List<Object[]> asignacionesPorSucursal;
    private List<Object[]> asignacionesPorMes;
}
