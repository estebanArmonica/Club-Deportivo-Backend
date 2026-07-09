package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPagoRepository extends JpaRepository<Pago, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar pagos por estado
     */
    List<Pago> findByEstado(String estado);

    /**
     * Buscar pagos por estado (case insensitive)
     */
    List<Pago> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar pagos por método de pago
     */
    List<Pago> findByMetodoPago(String metodoPago);

    /**
     * Buscar pagos por método de pago (case insensitive)
     */
    List<Pago> findByMetodoPagoIgnoreCase(String metodoPago);

    /**
     * Buscar pagos por fecha de pago
     */
    List<Pago> findByFechPago(LocalDate fechPago);

    /**
     * Buscar pagos por rango de fechas
     */
    List<Pago> findByFechPagoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar pagos por fecha después de
     */
    List<Pago> findByFechPagoAfter(LocalDate fecha);

    /**
     * Buscar pagos por fecha antes de
     */
    List<Pago> findByFechPagoBefore(LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR MONTO
    // ============================================================

    /**
     * Buscar pagos por monto exacto
     */
    List<Pago> findByMonto(int monto);

    /**
     * Buscar pagos con monto mayor a
     */
    List<Pago> findByMontoGreaterThan(int monto);

    /**
     * Buscar pagos con monto menor a
     */
    List<Pago> findByMontoLessThan(int monto);

    /**
     * Buscar pagos con monto entre dos valores
     */
    List<Pago> findByMontoBetween(int montoMin, int montoMax);

    /**
     * Buscar pagos por estado y rango de montos
     */
    List<Pago> findByEstadoAndMontoBetween(String estado, int montoMin, int montoMax);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (ESTADO + FECHA + METODO_PAGO)
    // ============================================================

    /**
     * Buscar pagos por estado y método de pago
     */
    List<Pago> findByEstadoAndMetodoPago(String estado, String metodoPago);

    /**
     * Buscar pagos por estado y rango de fechas
     */
    List<Pago> findByEstadoAndFechPagoBetween(String estado, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar pagos por método de pago y rango de fechas
     */
    List<Pago> findByMetodoPagoAndFechPagoBetween(String metodoPago, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar pagos por estado, método de pago y rango de fechas
     */
    List<Pago> findByEstadoAndMetodoPagoAndFechPagoBetween(String estado, String metodoPago,
                                                           LocalDate fechaInicio, LocalDate fechaFin);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todos los pagos ordenados por fecha de pago (más recientes primero)
     */
    List<Pago> findAllByOrderByFechPagoDesc();

    /**
     * Buscar pagos por estado ordenados por fecha de pago
     */
    List<Pago> findByEstadoOrderByFechPagoDesc(String estado);

    /**
     * Buscar pagos por estado ordenados por monto (mayor a menor)
     */
    List<Pago> findByEstadoOrderByMontoDesc(String estado);

    /**
     * Buscar pagos por método de pago ordenados por fecha
     */
    List<Pago> findByMetodoPagoOrderByFechPagoDesc(String metodoPago);

    /**
     * Buscar pagos por rango de fechas ordenados por fecha
     */
    List<Pago> findByFechPagoBetweenOrderByFechPagoDesc(LocalDate fechaInicio, LocalDate fechaFin);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar pagos por estado
     */
    @Query("SELECT p.estado, COUNT(p) FROM Pago p GROUP BY p.estado")
    List<Object[]> countPagosByEstado();

    /**
     * Contar pagos por método de pago
     */
    @Query("SELECT p.metodoPago, COUNT(p) FROM Pago p GROUP BY p.metodoPago")
    List<Object[]> countPagosByMetodoPago();

    /**
     * Contar pagos por mes
     */
    @Query("SELECT EXTRACT(YEAR FROM p.fechPago) AS anio, " +
            "EXTRACT(MONTH FROM p.fechPago) AS mes, " +
            "COUNT(p) AS total " +
            "FROM Pago p " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countPagosByMes();

    /**
     * Suma total de pagos por estado
     */
    @Query("SELECT p.estado, SUM(p.monto) FROM Pago p GROUP BY p.estado")
    List<Object[]> sumPagosByEstado();

    /**
     * Suma total de pagos por método de pago
     */
    @Query("SELECT p.metodoPago, SUM(p.monto) FROM Pago p GROUP BY p.metodoPago")
    List<Object[]> sumPagosByMetodoPago();

    /**
     * Suma total de pagos en un rango de fechas
     */
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fechPago BETWEEN :fechaInicio AND :fechaFin")
    Long sumPagosByFechasBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                 @Param("fechaFin") LocalDate fechaFin);

    /**
     * Suma total de pagos por estado en un rango de fechas
     */
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = :estado AND p.fechPago BETWEEN :fechaInicio AND :fechaFin")
    Long sumPagosByEstadoAndFechasBetween(@Param("estado") String estado,
                                          @Param("fechaInicio") LocalDate fechaInicio,
                                          @Param("fechaFin") LocalDate fechaFin);

    /**
     * Promedio de monto de pagos por estado
     */
    @Query("SELECT p.estado, AVG(p.monto) FROM Pago p GROUP BY p.estado")
    List<Object[]> avgPagosByEstado();

    /**
     * Monto máximo de pago
     */
    @Query("SELECT MAX(p.monto) FROM Pago p")
    Integer findMaxMonto();

    /**
     * Monto mínimo de pago
     */
    @Query("SELECT MIN(p.monto) FROM Pago p")
    Integer findMinMonto();

    /**
     * Contar pagos pendientes
     */
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.estado = 'pendiente'")
    Long countPagosPendientes();

    /**
     * Contar pagos pagados
     */
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.estado = 'pagado'")
    Long countPagosPagados();

    /**
     * Contar pagos cancelados
     */
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.estado = 'cancelado'")
    Long countPagosCancelados();

    /**
     * Contar pagos reembolsados
     */
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.estado = 'reembolsado'")
    Long countPagosReembolsados();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si existe un pago con el mismo monto, fecha y método en el mismo día
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Pago p " +
            "WHERE p.monto = :monto " +
            "AND p.fechPago = :fecha " +
            "AND p.metodoPago = :metodoPago " +
            "AND p.id != :id")
    Boolean existsPagoDuplicado(@Param("monto") int monto,
                                @Param("fecha") LocalDate fecha,
                                @Param("metodoPago") String metodoPago,
                                @Param("id") Long id);

    /**
     * Verificar si un pago está pagado
     */
    @Query("SELECT p.estado = 'pagado' FROM Pago p WHERE p.id = :id")
    Boolean isPagoPagado(@Param("id") Long id);

    /**
     * Verificar si un pago está pendiente
     */
    @Query("SELECT p.estado = 'pendiente' FROM Pago p WHERE p.id = :id")
    Boolean isPagoPendiente(@Param("id") Long id);

    // ============================================================
    // CONSULTAS PARA REPORTES
    // ============================================================

    /**
     * Obtener pagos del día actual
     */
    @Query("SELECT p FROM Pago p WHERE p.fechPago = CURRENT_DATE")
    List<Pago> findPagosDelDia();

    /**
     * Obtener pagos de la semana actual
     */
    @Query("SELECT p FROM Pago p WHERE p.fechPago >= CURRENT_DATE - 7")
    List<Pago> findPagosDeLaSemana();

    /**
     * Obtener pagos del mes actual
     */
    @Query("SELECT p FROM Pago p WHERE EXTRACT(MONTH FROM p.fechPago) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM p.fechPago) = EXTRACT(YEAR FROM CURRENT_DATE)")
    List<Pago> findPagosDelMes();

    /**
     * Obtener resumen de pagos por día
     */
    @Query("SELECT p.fechPago, COUNT(p), SUM(p.monto) " +
            "FROM Pago p " +
            "WHERE p.fechPago BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY p.fechPago " +
            "ORDER BY p.fechPago DESC")
    List<Object[]> getResumenDiario(@Param("fechaInicio") LocalDate fechaInicio,
                                    @Param("fechaFin") LocalDate fechaFin);
}
