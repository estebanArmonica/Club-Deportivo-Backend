package com.clubdeportivo.dtos.inscripcion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasInscripcionDTO {
    private Long total;
    private Long activas;
    private Long inactivas;
    private Long suspendidas;
    private Long finalizadas;
    private List<Object[]> inscripcionesPorEstado;
    private List<Object[]> inscripcionesPorMetodoPago;
    private List<Object[]> inscripcionesPorMes;
    private List<Object[]> inscripcionesPorAlumno;
    private List<Object[]> inscripcionesPorGrupo;
    private List<Object[]> inscripcionesActivasPorGrupo;
    private List<Object[]> inscripcionesPorCategoria;
    private List<Object[]> inscripcionesPorSucursal;
}
