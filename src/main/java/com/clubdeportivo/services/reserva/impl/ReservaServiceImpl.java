package com.clubdeportivo.services.reserva.impl;

import com.clubdeportivo.dtos.reserva.EstadisticasReservaDTO;
import com.clubdeportivo.models.Cancha;
import com.clubdeportivo.models.Equipo;
import com.clubdeportivo.models.Reserva;
import com.clubdeportivo.repositories.ICanchaRepository;
import com.clubdeportivo.repositories.IEquipoRepository;
import com.clubdeportivo.repositories.IReservaRepository;
import com.clubdeportivo.services.reserva.IReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService {

    private final IReservaRepository reservaRepo;
    private final IEquipoRepository equipoRepository;
    private final ICanchaRepository canchaRepo;

    /**
     * Validaciones privadas
     */
    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null || horaFin == null) {
            throw new RuntimeException("La hora de inicio y fin son obligatorias");
        }

        if (horaInicio.isAfter(horaFin)) {
            throw new RuntimeException("La hora de inicio no puede ser después de la hora de fin");
        }

        if (horaInicio.equals(horaFin)) {
            throw new RuntimeException("La hora de inicio y fin no pueden ser iguales");
        }
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        String estadoUpper = estado.toUpperCase();
        if (!estadoUpper.equals("PENDIENTE") &&
                !estadoUpper.equals("CONFIRMADA") &&
                !estadoUpper.equals("CANCELADA") &&
                !estadoUpper.equals("COMPLETADA")) {
            throw new RuntimeException("Estado no válido. Debe ser: PENDIENTE, CONFIRMADA, CANCELADA o COMPLETADA");
        }
    }

    private Equipo validarEquipo(Long equipoId) {
        if (equipoId == null) {
            throw new RuntimeException("La reserva debe tener un equipo asociado");
        }

        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + equipoId));

        if (!equipo.getActivo()) {
            throw new RuntimeException("No se puede crear una reserva para un equipo inactivo");
        }

        return equipo;
    }

    private Cancha validarCancha(Long canchaId) {
        if (canchaId == null) {
            throw new RuntimeException("La reserva debe tener una cancha asociada");
        }

        Cancha cancha = canchaRepo.findById(canchaId)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + canchaId));

        if (!cancha.getDisponible()) {
            throw new RuntimeException("No se puede crear una reserva para una cancha no disponible");
        }

        return cancha;
    }

    /**
     * CRUD
     */

    @Override
    public Reserva create(Reserva reserva) {
        // Validar horario
        validarHorario(reserva.getHoraReserva(), reserva.getHoraFin());

        // Validar estado
        validarEstado(reserva.getEstado());

        // Validar equipo
        Long equipoId = reserva.getEquipo() != null ? reserva.getEquipo().getId() : null;
        Equipo equipo = validarEquipo(equipoId);

        // Validar cancha
        Long canchaId = reserva.getCancha() != null ? reserva.getCancha().getId() : null;
        Cancha cancha = validarCancha(canchaId);

        // Si no se especifica fecha de creación, usar la actual
        if (reserva.getFechCreacion() == null) {
            reserva.setFechCreacion(LocalDate.now());
        }

        // Verificar que la cancha esté disponible en el horario solicitado
        if (!reservaRepo.isCanchaDisponibleEnHorario(
                canchaId,
                reserva.getFechCreacion(),
                reserva.getHoraReserva(),
                reserva.getHoraFin())) {
            throw new RuntimeException("La cancha no está disponible en el horario solicitado");
        }

        // Validar observaciones (si es null, asignar vacío)
        if (reserva.getObservaciones() == null) {
            reserva.setObservaciones("");
        }

        reserva.setEquipo(equipo);
        reserva.setCancha(cancha);

        return reservaRepo.save(reserva);
    }

    @Override
    public Reserva update(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        // Si la reserva está cancelada o completada, no se puede modificar
        if (reservaExistente.getEstado().equalsIgnoreCase("CANCELADA") ||
                reservaExistente.getEstado().equalsIgnoreCase("COMPLETADA")) {
            throw new RuntimeException("No se puede modificar una reserva " +
                    reservaExistente.getEstado().toLowerCase());
        }

        // Validar horario
        validarHorario(reservaActualizada.getHoraReserva(), reservaActualizada.getHoraFin());

        // Validar estado
        validarEstado(reservaActualizada.getEstado());

        // Si se cambia la cancha, validar que exista y esté disponible
        Long nuevaCanchaId = reservaActualizada.getCancha() != null ?
                reservaActualizada.getCancha().getId() : null;
        Long canchaActualId = reservaExistente.getCancha().getId();

        if (nuevaCanchaId != null && !nuevaCanchaId.equals(canchaActualId)) {
            Cancha cancha = validarCancha(nuevaCanchaId);

            // Verificar disponibilidad en la nueva cancha
            LocalDate fecha = reservaActualizada.getFechCreacion() != null ?
                    reservaActualizada.getFechCreacion() :
                    reservaExistente.getFechCreacion();

            if (!reservaRepo.isCanchaDisponibleEnHorario(
                    nuevaCanchaId,
                    fecha,
                    reservaActualizada.getHoraReserva(),
                    reservaActualizada.getHoraFin())) {
                throw new RuntimeException("La cancha no está disponible en el horario solicitado");
            }

            reservaExistente.setCancha(cancha);
        }

        // Si se cambia la fecha, verificar disponibilidad
        if (reservaActualizada.getFechCreacion() != null &&
                !reservaActualizada.getFechCreacion().equals(reservaExistente.getFechCreacion())) {

            Long canchaIdFinal = nuevaCanchaId != null ? nuevaCanchaId : canchaActualId;

            if (!reservaRepo.isCanchaDisponibleEnHorario(
                    canchaIdFinal,
                    reservaActualizada.getFechCreacion(),
                    reservaActualizada.getHoraReserva(),
                    reservaActualizada.getHoraFin())) {
                throw new RuntimeException("La cancha no está disponible en la fecha solicitada");
            }

            reservaExistente.setFechCreacion(reservaActualizada.getFechCreacion());
        } else if (nuevaCanchaId != null && !nuevaCanchaId.equals(canchaActualId)) {
            // Si solo cambió la cancha, ya validamos arriba
        } else {
            // Verificar disponibilidad con los mismos datos (por si cambió horario)
            Long canchaIdFinal = nuevaCanchaId != null ? nuevaCanchaId : canchaActualId;
            LocalDate fecha = reservaActualizada.getFechCreacion() != null ?
                    reservaActualizada.getFechCreacion() :
                    reservaExistente.getFechCreacion();

            if (!reservaRepo.isCanchaDisponibleEnHorario(
                    canchaIdFinal,
                    fecha,
                    reservaActualizada.getHoraReserva(),
                    reservaActualizada.getHoraFin())) {
                throw new RuntimeException("La cancha no está disponible en el horario solicitado");
            }
        }

        // Si se cambia el equipo, validar que exista y esté activo
        Long nuevoEquipoId = reservaActualizada.getEquipo() != null ?
                reservaActualizada.getEquipo().getId() : null;
        Long equipoActualId = reservaExistente.getEquipo().getId();

        if (nuevoEquipoId != null && !nuevoEquipoId.equals(equipoActualId)) {
            Equipo equipo = validarEquipo(nuevoEquipoId);
            reservaExistente.setEquipo(equipo);
        }

        reservaExistente.setHoraReserva(reservaActualizada.getHoraReserva());
        reservaExistente.setHoraFin(reservaActualizada.getHoraFin());
        reservaExistente.setEstado(reservaActualizada.getEstado());
        reservaExistente.setObservaciones(reservaActualizada.getObservaciones());

        return reservaRepo.save(reservaExistente);
    }

    @Override
    public Reserva cambiarEstado(Long id, String nuevoEstado) {
        Reserva reserva = reservaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        validarEstado(nuevoEstado);

        // Si la reserva ya está cancelada o completada, no se puede cambiar
        if (reserva.getEstado().equalsIgnoreCase("CANCELADA") ||
                reserva.getEstado().equalsIgnoreCase("COMPLETADA")) {
            throw new RuntimeException("No se puede cambiar el estado de una reserva " +
                    reserva.getEstado().toLowerCase());
        }

        reserva.setEstado(nuevoEstado.toUpperCase());
        return reservaRepo.save(reserva);
    }

    @Override
    public Reserva cancelar(Long id) {
        return cambiarEstado(id,"CANCELADA");
    }

    @Override
    public Reserva completar(Long id) {
        return cambiarEstado(id,"COMPLETADA");
    }

    @Override
    public void deleteReserva(Long id) {
        if(!reservaRepo.existsById(id)) {
            throw new RuntimeException("Rserva no econtrada con ID: " + id);
        }
        reservaRepo.deleteById(id);
    }

    @Override
    public Reserva findById(Long id) {
        return reservaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public Reserva findByIdWithEquipoAndCancha(Long id) {
        return reservaRepo.findByIdWithEquipoAndCancha(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public Reserva findByIdWithAllRelations(Long id) {
        return reservaRepo.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public List<Reserva> findAll() {
        return reservaRepo.findAllOrderByFechaHora();
    }

    @Override
    public List<Reserva> findAllWithEquipoAndCancha() {
        return reservaRepo.findAllWithEquipoAndCancha();
    }

    @Override
    public List<Reserva> findByEstado(String estado) {
        return reservaRepo.findByEstadoOrderByFechaDesc(estado);
    }

    @Override
    public List<Reserva> findByEstadoWithEquipoAndCancha(String estado) {
        return reservaRepo.findByEstadoWithEquipoAndCancha(estado);
    }

    @Override
    public List<Reserva> findByEquipo(Long equipoId) {
        return reservaRepo.findByEquipoIdOrderByFechaHora(equipoId);
    }

    @Override
    public List<Reserva> findActivasByEquipo(Long equipoId) {
        return reservaRepo.findActivasByEquipoId(equipoId);
    }

    @Override
    public List<Reserva> findByCancha(Long canchaId) {
        return reservaRepo.findByCanchaIdOrderByFechaHora(canchaId);
    }

    @Override
    public List<Reserva> findActivasByCancha(Long canchaId) {
        return reservaRepo.findActivasByCanchaId(canchaId);
    }

    @Override
    public List<Reserva> findByCanchaAndFecha(Long canchaId, LocalDate fecha) {
        return reservaRepo.findByCanchaIdAndFecha(canchaId, fecha);
    }

    @Override
    public List<Reserva> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return reservaRepo.findByFechCreacionBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Reserva> findBySucursal(Long sucursalId) {
        return reservaRepo.findBySucursalId(sucursalId);
    }

    @Override
    public List<Reserva> findByClub(Long clubId) {
        return reservaRepo.findByClubId(clubId);
    }

    @Override
    public boolean isCanchaDisponible(Long canchaId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        validarHorario(horaInicio, horaFin);
        return reservaRepo.isCanchaDisponibleEnHorario(canchaId, fecha, horaInicio, horaFin);
    }

    @Override
    public boolean hasReservasActivas(Long equipoId) {
        return reservaRepo.hasReservasActivas(equipoId);
    }

    @Override
    public EstadisticasReservaDTO getEstadisticas() {
        return EstadisticasReservaDTO.builder()
                .total(reservaRepo.count())
                .reservasPorEstado(reservaRepo.countReservasByEstado())
                .reservasPorCancha(reservaRepo.countReservasByCancha())
                .reservasPorEquipo(reservaRepo.countReservasByEquipo())
                .reservasPorMes(reservaRepo.countReservasByMes())
                .build();
    }
}
