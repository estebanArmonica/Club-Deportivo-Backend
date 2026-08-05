package com.clubdeportivo.services.pago;

import com.clubdeportivo.dtos.pago.EstadisticasPagoDTO;
import com.clubdeportivo.models.Pago;

import java.time.LocalDate;
import java.util.List;

public interface IPagoService {
    /**
     * Crear un nuevo pago
     */
    Pago create(Pago pago);

    /**
     * Actualizar un pago existente
     */
    Pago update(Long id, Pago pagoActualizado);

    /**
     * Cambiar estado de un pago
     */
    Pago cambiarEstado(Long id, String nuevoEstado);

    /**
     * Marcar un pago como pagado
     */
    Pago marcarComoPagado(Long id);

    /**
     * Marcar un pago como cancelado
     */
    Pago marcarComoCancelado(Long id);

    /**
     * Marcar un pago como reembolsado
     */
    Pago marcarComoReembolsado(Long id);

    /**
     * Eliminar un pago (borrado físico)
     */
    void delete(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar pago por ID
     */
    Pago findById(Long id);

    /**
     * Buscar todos los pagos
     */
    List<Pago> findAll();

    /**
     * Buscar pagos por estado
     */
    List<Pago> findByEstado(String estado);

    /**
     * Buscar pagos por método de pago
     */
    List<Pago> findByMetodoPago(String metodoPago);

    /**
     * Buscar pagos por fecha
     */
    List<Pago> findByFecha(LocalDate fecha);

    /**
     * Buscar pagos por rango de fechas
     */
    List<Pago> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar pagos por rango de montos
     */
    List<Pago> findByMontoBetween(int montoMin, int montoMax);

    /**
     * Buscar pagos por estado y método de pago
     */
    List<Pago> findByEstadoAndMetodoPago(String estado, String metodoPago);

    /**
     * Buscar pagos del día
     */
    List<Pago> findPagosDelDia();

    /**
     * Buscar pagos de la semana
     */
    List<Pago> findPagosDeLaSemana();

    /**
     * Buscar pagos del mes
     */
    List<Pago> findPagosDelMes();

    // ============================================================
    // ESTADÍSTICAS Y REPORTES
    // ============================================================

    /**
     * Obtener estadísticas de pagos
     */
    EstadisticasPagoDTO getEstadisticas();

    /**
     * Obtener resumen de pagos por día en un rango de fechas
     */
    List<Object[]> getResumenDiario(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtener total de pagos en un rango de fechas
     */
    Long getTotalPagosByFechas(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtener total de pagos por estado en un rango de fechas
     */
    Long getTotalPagosByEstadoAndFechas(String estado, LocalDate fechaInicio, LocalDate fechaFin);

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si un pago existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si un pago está pagado
     */
    boolean isPagado(Long id);

    /**
     * Verificar si un pago está pendiente
     */
    boolean isPendiente(Long id);
}
