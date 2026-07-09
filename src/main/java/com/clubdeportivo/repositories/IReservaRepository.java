package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar reservas por estado
     */
    List<Reserva> findByEstado(String estado);

    /**
     * Buscar reservas por estado (case insensitive)
     */
    List<Reserva> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar reservas por fecha de creación
     */
    List<Reserva> findByFechCreacion(LocalDate fechCreacion);

    /**
     * Buscar reservas por rango de fechas de creación
     */
    List<Reserva> findByFechCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar reservas creadas después de una fecha
     */
    List<Reserva> findByFechCreacionAfter(LocalDate fecha);

    /**
     * Buscar reservas creadas antes de una fecha
     */
    List<Reserva> findByFechCreacionBefore(LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR HORARIO
    // ============================================================

    /**
     * Buscar reservas por hora de inicio
     */
    List<Reserva> findByHoraReserva(LocalTime horaReserva);

    /**
     * Buscar reservas por hora de fin
     */
    List<Reserva> findByHoraFin(LocalTime horaFin);

    /**
     * Buscar reservas que inicien después de una hora específica
     */
    List<Reserva> findByHoraReservaAfter(LocalTime hora);

    /**
     * Buscar reservas que inicien antes de una hora específica
     */
    List<Reserva> findByHoraReservaBefore(LocalTime hora);

    /**
     * Buscar reservas por rango de horario
     */
    @Query("SELECT r FROM Reserva r WHERE r.horaReserva >= :horaInicio AND r.horaFin <= :horaFin")
    List<Reserva> findByHorarioBetween(@Param("horaInicio") LocalTime horaInicio,
                                       @Param("horaFin") LocalTime horaFin);

    // ============================================================
    // BÚSQUEDAS POR EQUIPO
    // ============================================================

    /**
     * Buscar reservas por ID de equipo
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.id = :equipoId")
    List<Reserva> findByEquipoId(@Param("equipoId") Long equipoId);

    /**
     * Buscar reservas por ID de equipo y estado
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.id = :equipoId AND r.estado = :estado")
    List<Reserva> findByEquipoIdAndEstado(@Param("equipoId") Long equipoId,
                                          @Param("estado") String estado);

    /**
     * Buscar reservas por nombre de equipo
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.nombre = :nombreEquipo")
    List<Reserva> findByEquipoNombre(@Param("nombreEquipo") String nombreEquipo);

    /**
     * Buscar reservas activas (no canceladas) por equipo
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.id = :equipoId AND r.estado != 'cancelada'")
    List<Reserva> findActivasByEquipoId(@Param("equipoId") Long equipoId);

    // ============================================================
    // BÚSQUEDAS POR CANCHA
    // ============================================================

    /**
     * Buscar reservas por ID de cancha
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId")
    List<Reserva> findByCanchaId(@Param("canchaId") Long canchaId);

    /**
     * Buscar reservas por ID de cancha y estado
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId AND r.estado = :estado")
    List<Reserva> findByCanchaIdAndEstado(@Param("canchaId") Long canchaId,
                                          @Param("estado") String estado);

    /**
     * Buscar reservas por nombre de cancha
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.nombre = :nombreCancha")
    List<Reserva> findByCanchaNombre(@Param("nombreCancha") String nombreCancha);

    /**
     * Buscar reservas activas (no canceladas) por cancha
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId AND r.estado != 'cancelada'")
    List<Reserva> findActivasByCanchaId(@Param("canchaId") Long canchaId);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (EQUIPO + CANCHA + FECHA + HORARIO)
    // ============================================================

    /**
     * Buscar reservas por equipo y cancha
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.id = :equipoId AND r.cancha.id = :canchaId")
    List<Reserva> findByEquipoIdAndCanchaId(@Param("equipoId") Long equipoId,
                                            @Param("canchaId") Long canchaId);

    /**
     * Buscar reservas por cancha y fecha
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId AND r.fechCreacion = :fecha")
    List<Reserva> findByCanchaIdAndFecha(@Param("canchaId") Long canchaId,
                                         @Param("fecha") LocalDate fecha);

    /**
     * Buscar reservas por cancha, fecha y estado
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId AND r.fechCreacion = :fecha AND r.estado = :estado")
    List<Reserva> findByCanchaIdAndFechaAndEstado(@Param("canchaId") Long canchaId,
                                                  @Param("fecha") LocalDate fecha,
                                                  @Param("estado") String estado);

    /**
     * Buscar reservas por equipo, fecha y estado
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.id = :equipoId AND r.fechCreacion = :fecha AND r.estado = :estado")
    List<Reserva> findByEquipoIdAndFechaAndEstado(@Param("equipoId") Long equipoId,
                                                  @Param("fecha") LocalDate fecha,
                                                  @Param("estado") String estado);

    /**
     * Buscar reservas que se superpongan en horario en una cancha específica
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId " +
            "AND r.fechCreacion = :fecha " +
            "AND r.estado != 'cancelada' " +
            "AND ((r.horaReserva <= :horaFin AND r.horaFin >= :horaInicio))")
    List<Reserva> findReservasQueSeSuperpongan(@Param("canchaId") Long canchaId,
                                               @Param("fecha") LocalDate fecha,
                                               @Param("horaInicio") LocalTime horaInicio,
                                               @Param("horaFin") LocalTime horaFin);

    /**
     * Buscar reservas por sucursal (a través de cancha)
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.sucursal.id = :sucursalId")
    List<Reserva> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar reservas por club (a través de cancha -> sucursal -> club)
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.sucursal.club.id = :clubId")
    List<Reserva> findByClubId(@Param("clubId") Long clubId);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todas las reservas ordenadas por fecha y hora
     */
    @Query("SELECT r FROM Reserva r ORDER BY r.fechCreacion DESC, r.horaReserva ASC")
    List<Reserva> findAllOrderByFechaHora();

    /**
     * Buscar reservas por estado ordenadas por fecha
     */
    @Query("SELECT r FROM Reserva r WHERE r.estado = :estado ORDER BY r.fechCreacion DESC")
    List<Reserva> findByEstadoOrderByFechaDesc(@Param("estado") String estado);

    /**
     * Buscar reservas por cancha ordenadas por fecha
     */
    @Query("SELECT r FROM Reserva r WHERE r.cancha.id = :canchaId ORDER BY r.fechCreacion DESC, r.horaReserva ASC")
    List<Reserva> findByCanchaIdOrderByFechaHora(@Param("canchaId") Long canchaId);

    /**
     * Buscar reservas por equipo ordenadas por fecha
     */
    @Query("SELECT r FROM Reserva r WHERE r.equipo.id = :equipoId ORDER BY r.fechCreacion DESC, r.horaReserva ASC")
    List<Reserva> findByEquipoIdOrderByFechaHora(@Param("equipoId") Long equipoId);

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar reserva por ID con su equipo y cancha (evita N+1)
     */
    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.equipo LEFT JOIN FETCH r.cancha WHERE r.id = :id")
    Optional<Reserva> findByIdWithEquipoAndCancha(@Param("id") Long id);

    /**
     * Buscar todas las reservas con su equipo y cancha
     */
    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.equipo LEFT JOIN FETCH r.cancha")
    List<Reserva> findAllWithEquipoAndCancha();

    /**
     * Buscar reservas por estado con su equipo y cancha
     */
    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.equipo LEFT JOIN FETCH r.cancha WHERE r.estado = :estado")
    List<Reserva> findByEstadoWithEquipoAndCancha(@Param("estado") String estado);

    /**
     * Buscar reservas por cancha con su equipo y cancha
     */
    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.equipo LEFT JOIN FETCH r.cancha WHERE r.cancha.id = :canchaId")
    List<Reserva> findByCanchaIdWithEquipoAndCancha(@Param("canchaId") Long canchaId);

    /**
     * Buscar reservas por equipo con su equipo y cancha
     */
    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.equipo LEFT JOIN FETCH r.cancha WHERE r.equipo.id = :equipoId")
    List<Reserva> findByEquipoIdWithEquipoAndCancha(@Param("equipoId") Long equipoId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar reservas por estado
     */
    @Query("SELECT r.estado, COUNT(r) FROM Reserva r GROUP BY r.estado")
    List<Object[]> countReservasByEstado();

    /**
     * Contar reservas por cancha
     */
    @Query("SELECT r.cancha.nombre, COUNT(r) FROM Reserva r GROUP BY r.cancha.nombre")
    List<Object[]> countReservasByCancha();

    /**
     * Contar reservas por equipo
     */
    @Query("SELECT r.equipo.nombre, COUNT(r) FROM Reserva r GROUP BY r.equipo.nombre")
    List<Object[]> countReservasByEquipo();

    /**
     * Contar reservas por mes
     */
    @Query("SELECT EXTRACT(YEAR FROM r.fechCreacion) AS anio, " +
            "EXTRACT(MONTH FROM r.fechCreacion) AS mes, " +
            "COUNT(r) AS total " +
            "FROM Reserva r " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countReservasByMes();

    /**
     * Contar reservas activas (no canceladas) por cancha en una fecha específica
     */
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cancha.id = :canchaId AND r.fechCreacion = :fecha AND r.estado != 'cancelada'")
    Long countReservasActivasByCanchaAndFecha(@Param("canchaId") Long canchaId,
                                              @Param("fecha") LocalDate fecha);

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si una cancha está disponible en un horario específico
     */
    @Query("SELECT CASE WHEN COUNT(r) = 0 THEN true ELSE false END " +
            "FROM Reserva r " +
            "WHERE r.cancha.id = :canchaId " +
            "AND r.fechCreacion = :fecha " +
            "AND r.estado != 'cancelada' " +
            "AND ((r.horaReserva < :horaFin AND r.horaFin > :horaInicio))")
    Boolean isCanchaDisponibleEnHorario(@Param("canchaId") Long canchaId,
                                        @Param("fecha") LocalDate fecha,
                                        @Param("horaInicio") LocalTime horaInicio,
                                        @Param("horaFin") LocalTime horaFin);

    /**
     * Verificar si un equipo tiene reservas activas
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reserva r " +
            "WHERE r.equipo.id = :equipoId AND r.estado != 'cancelada'")
    Boolean hasReservasActivas(@Param("equipoId") Long equipoId);
}
