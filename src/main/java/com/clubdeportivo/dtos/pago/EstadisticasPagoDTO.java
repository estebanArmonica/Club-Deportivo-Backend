package com.clubdeportivo.dtos.pago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasPagoDTO {
    private Long total;
    private Long pendientes;
    private Long pagados;
    private Long cancelados;
    private Long reembolsados;
    private List<Object[]> pagosPorEstado;
    private List<Object[]> pagosPorMetodoPago;
    private List<Object[]> pagosPorMes;
    private List<Object[]> sumPagosPorEstado;
    private List<Object[]> sumPagosPorMetodoPago;
    private Integer montoMaximo;
    private Integer montoMinimo;
}
