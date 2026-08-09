package com.clubdeportivo.dtos.encargadosucursal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasEncargadoSucursalDTO {
    private Long total;
    private Long activos;
    private Long inactivos;
    private List<Object[]> asignacionesPorTurno;
    private List<Object[]> asignacionesActivasPorTurno;
    private List<Object[]> asignacionesPorSucursal;
    private List<Object[]> asignacionesActivasPorSucursal;
    private List<Object[]> asignacionesPorUsuario;
    private List<Object[]> asignacionesPorMes;
}
