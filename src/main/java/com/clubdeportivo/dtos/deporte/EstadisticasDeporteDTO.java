package com.clubdeportivo.dtos.deporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasDeporteDTO {
    private Long total;
    private Long activos;
    private Long inactivos;
}
