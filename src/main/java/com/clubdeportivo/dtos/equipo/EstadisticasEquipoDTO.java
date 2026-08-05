package com.clubdeportivo.dtos.equipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasEquipoDTO {
    private Long total;
    private Long activos;
    private Long inactivos;
    private List<Object[]> equiposPorGrupo;
    private List<Object[]> equiposActivosPorGrupo;
    private List<Object[]> equiposPorUsuario;
    private List<Object[]> equiposPorCategoria;
    private List<Object[]> equiposPorSucursal;
}
