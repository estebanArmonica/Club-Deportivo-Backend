package com.clubdeportivo.services.reserva;

import com.clubdeportivo.dtos.reserva.EstadisticasReservaDTO;
import com.clubdeportivo.models.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IReservaService {
    /**
     * Crud
     */
    /**
     * Crear una nueva reserva
     */
    Reserva create(Reserva reserva);

    /**
     * Actualizar una reserva existente
     */
    Reserva update(Long id, Reserva reservaActualizada);

    /**
     * Cambiar estado de una reserva
     */
    Reserva cambiarEstado(Long id, String nuevoEstado);

    /**
     * Cancelar una reserva
     */
    Reserva cancelar(Long id);

    /**
     * Completar una reserva
     */
    Reserva completar(Long id);

    /**
     * Eliminar una reserva (borrado físico)
     */
    void deleteReserva(Long id);

    /**
     * Busquedas
     */

    /**
     * Buscar reserva por ID
     */
    Reserva findById(Long id);

    /**
     * Buscar reserva por ID con su equipo y cancha
     */
    Reserva findByIdWithEquipoAndCancha(Long id);

    /**
     * Buscar reserva por ID con todas sus relaciones
     */
    Reserva findByIdWithAllRelations(Long id);

    /**
     * Buscar todas las reservas
     */
    List<Reserva> findAll();

    /**
     * Buscar todas las reservas con su equipo y cancha
     */
    List<Reserva> findAllWithEquipoAndCancha();

    /**
     * Buscar reservas por estado
     */
    List<Reserva> findByEstado(String estado);

    /**
     * Buscar reservas por estado con su equipo y cancha
     */
    List<Reserva> findByEstadoWithEquipoAndCancha(String estado);

    /**
     * Buscar reservas por equipo
     */
    List<Reserva> findByEquipo(Long equipoId);

    /**
     * Buscar reservas activas por equipo
     */
    List<Reserva> findActivasByEquipo(Long equipoId);

    /**
     * Buscar reservas por cancha
     */
    List<Reserva> findByCancha(Long canchaId);

    /**
     * Buscar reservas activas por cancha
     */
    List<Reserva> findActivasByCancha(Long canchaId);

    /**
     * Buscar reservas por cancha y fecha
     */
    List<Reserva> findByCanchaAndFecha(Long canchaId, LocalDate fecha);

    /**
     * Buscar reservas por rango de fechas
     */
    List<Reserva> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar reservas por sucursal
     */
    List<Reserva> findBySucursal(Long sucursalId);

    /**
     * Buscar reservas por club
     */
    List<Reserva> findByClub(Long clubId);

    /**
     * Validaciones y utilidades
     */

    /**
     * Verificar si una cancha está disponible en un horario específico
     */
    boolean isCanchaDisponible(Long canchaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

    /**
     * Verificar si un equipo tiene reservas activas
     */
    boolean hasReservasActivas(Long equipoId);

    /**
     * Estadisticas
     */

    /**
     * Obtener estadísticas de reservas
     */
    EstadisticasReservaDTO getEstadisticas();
}
