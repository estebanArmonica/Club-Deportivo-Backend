package com.clubdeportivo.dtos.grupo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasGrupoDTO {
    private Long total;
    private Long activos;
    private Long inactivos;
    private List<Object[]> gruposPorCategoria;
    private List<Object[]> gruposActivosPorCategoria;
    private List<Object[]> gruposPorSucursal;
    private List<Object[]> gruposActivosPorSucursal;
    private BigDecimal precioPromedio;
    private Integer capacidadTotal;
}
