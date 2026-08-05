package com.clubdeportivo.dtos.reserva;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasReservaDTO {
    private Long total;
    private List<Object[]> reservasPorEstado;
    private List<Object[]> reservasPorCancha;
    private List<Object[]> reservasPorEquipo;
    private List<Object[]> reservasPorMes;
}
