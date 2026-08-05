package com.clubdeportivo.dtos.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasClubDTO {
    private Long total;
    private Long activos;
    private Long inactivos;
    private List<Object[]> clubesPorMesCreacion;
}
