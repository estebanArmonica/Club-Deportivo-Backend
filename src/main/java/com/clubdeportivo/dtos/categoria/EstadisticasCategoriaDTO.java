package com.clubdeportivo.dtos.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasCategoriaDTO {
    private Long total;
    private Long activas;
    private Long inactivas;
    private List<Object[]> categoriasPorDeporte;
    private List<Object[]> categoriasActivasPorDeporte;
}
