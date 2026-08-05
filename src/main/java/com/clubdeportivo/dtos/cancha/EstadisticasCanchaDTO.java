package com.clubdeportivo.dtos.cancha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasCanchaDTO {
    private Long total;
    private Long disponibles;
    private Long noDisponibles;
    private List<Object[]> canchasPorTipo;
    private List<Object[]> canchasDisponiblesPorTipo;
    private List<Object[]> canchasPorSucursal;
    private List<Object[]> canchasDisponiblesPorSucursal;
    private Double capacidadPromedio;
    private Integer capacidadTotal;
}
