package com.clubdeportivo.dtos.sucursal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasSucursalDTO {
    private Long total;
    private Long activas;
    private Long inactivas;
    private List<Object[]> sucursalesPorClub;
    private List<Object[]> sucursalesActivasPorClub;
}
