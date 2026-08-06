package com.clubdeportivo.dtos.clase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasClaseDTO {
    private Long total;
    private Long activas;
    private Long inactivas;
    private List<Object[]> clasesPorEstado;
    private List<Object[]> clasesPorGrupo;
    private List<Object[]> clasesActivasPorGrupo;
    private List<Object[]> clasesPorCategoria;
    private List<Object[]> clasesPorSucursal;
    private List<Object[]> clasesPorMes;
    private List<Object[]> clasesPorDiaSemana;
}
