package com.clubdeportivo.services.clase.impl;

import com.clubdeportivo.dtos.clase.EstadisticasClaseDTO;
import com.clubdeportivo.models.Clase;
import com.clubdeportivo.models.Grupo;
import com.clubdeportivo.repositories.IClaseRepository;
import com.clubdeportivo.repositories.IGrupoRepository;
import com.clubdeportivo.services.clase.IClaseService;
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
public class ClaseServiceImpl implements IClaseService {
    private IClaseRepository claseRepository;
    private IGrupoRepository grupoRepository;

    // Validaciones privadas
    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        String estadoLower = estado.toLowerCase();
        if (!estadoLower.equals("programada") &&
                !estadoLower.equals("en_curso") &&
                !estadoLower.equals("completada") &&
                !estadoLower.equals("cancelada")) {
            throw new RuntimeException("Estado no válido. Debe ser: programada, en_curso, completada o cancelada");
        }
    }

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

    private Grupo validarGrupo(Long grupoId) {
        if (grupoId == null) {
            throw new RuntimeException("La clase debe tener un grupo asociado");
        }

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + grupoId));

        if (!grupo.getActivo()) {
            throw new RuntimeException("No se puede crear una clase para un grupo inactivo");
        }

        return grupo;
    }

    private void validarHorarioDisponible(Long grupoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Long claseId) {
        if (claseRepository.existsClaseEnHorario(grupoId, fecha, horaInicio, horaFin, claseId)) {
            throw new RuntimeException("Ya existe una clase programada en el mismo horario para este grupo");
        }
    }
    @Override
    public Clase create(Clase clase) {
        // Validar grupo
        Long grupoId = clase.getGrupo() != null ? clase.getGrupo().getId() : null;
        Grupo grupo = validarGrupo(grupoId);

        // Validar fecha
        if (clase.getFechClase() == null) {
            throw new RuntimeException("La fecha de la clase es obligatoria");
        }

        // Validar horario
        validarHorario(clase.getHoraInicio(), clase.getHoraFin());

        // Validar estado
        validarEstado(clase.getEstado());

        // Verificar que no exista otra clase en el mismo horario para el grupo
        validarHorarioDisponible(grupoId, clase.getFechClase(), clase.getHoraInicio(), clase.getHoraFin(), null);

        clase.setGrupo(grupo);

        return claseRepository.save(clase);
    }

    @Override
    public Clase update(Long id, Clase claseActualizada) {
        Clase claseExistente = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));

        // Validar fecha
        if (claseActualizada.getFechClase() == null) {
            throw new RuntimeException("La fecha de la clase es obligatoria");
        }

        // Validar horario
        validarHorario(claseActualizada.getHoraInicio(), claseActualizada.getHoraFin());

        // Validar estado
        validarEstado(claseActualizada.getEstado());

        // Si la clase ya está completada o cancelada, no se puede modificar
        if (claseExistente.getEstado().equalsIgnoreCase("completada") ||
                claseExistente.getEstado().equalsIgnoreCase("cancelada")) {
            throw new RuntimeException("No se puede modificar una clase " +
                    claseExistente.getEstado().toLowerCase());
        }

        // Si se cambia el grupo, validar que exista y esté activo
        Long nuevoGrupoId = claseActualizada.getGrupo() != null ?
                claseActualizada.getGrupo().getId() : null;
        Long grupoActualId = claseExistente.getGrupo().getId();

        if (nuevoGrupoId != null && !nuevoGrupoId.equals(grupoActualId)) {
            Grupo grupo = validarGrupo(nuevoGrupoId);

            // Verificar que no exista otra clase en el mismo horario para el nuevo grupo
            validarHorarioDisponible(
                    nuevoGrupoId,
                    claseActualizada.getFechClase(),
                    claseActualizada.getHoraInicio(),
                    claseActualizada.getHoraFin(),
                    id
            );

            claseExistente.setGrupo(grupo);
        } else {
            // Verificar que no exista otra clase en el mismo horario para el mismo grupo
            validarHorarioDisponible(
                    grupoActualId,
                    claseActualizada.getFechClase(),
                    claseActualizada.getHoraInicio(),
                    claseActualizada.getHoraFin(),
                    id
            );
        }

        claseExistente.setFechClase(claseActualizada.getFechClase());
        claseExistente.setHoraInicio(claseActualizada.getHoraInicio());
        claseExistente.setHoraFin(claseActualizada.getHoraFin());
        claseExistente.setEstado(claseActualizada.getEstado());
        claseExistente.setActivo(claseActualizada.getActivo());

        return claseRepository.save(claseExistente);
    }

    @Override
    public Clase cambiarEstado(Long id, String nuevoEstado) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));

        validarEstado(nuevoEstado);

        String nuevoEstadoLower = nuevoEstado.toLowerCase();

        // Si la clase ya está completada o cancelada, no se puede cambiar
        if (clase.getEstado().equalsIgnoreCase("completada") ||
                clase.getEstado().equalsIgnoreCase("cancelada")) {
            throw new RuntimeException("No se puede cambiar el estado de una clase " +
                    clase.getEstado().toLowerCase());
        }

        clase.setEstado(nuevoEstadoLower);
        return claseRepository.save(clase);
    }

    @Override
    public Clase iniciar(Long id) {
        return cambiarEstado(id, "en_curso");
    }

    @Override
    public Clase completar(Long id) {
        return cambiarEstado(id, "completada");
    }

    @Override
    public Clase cancelar(Long id) {
        return cambiarEstado(id, "cancelada");
    }

    @Override
    public void deleteLogical(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));

        // Verificar si tiene asistencias asociadas
        if (claseRepository.hasAsistenciasAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar la clase porque tiene asistencias asociadas");
        }

        clase.setActivo(false);
        claseRepository.save(clase);
    }

    @Override
    public Clase findById(Long id) {
        return claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));
    }

    @Override
    public Clase findByIdWithGrupo(Long id) {
        return claseRepository.findByIdWithGrupo(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));
    }

    @Override
    public Clase findByIdWithAllRelations(Long id) {
        return claseRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));
    }

    @Override
    public List<Clase> findAll() {
        return claseRepository.findAllOrderByFechaHora();
    }

    @Override
    public List<Clase> findAllWithGrupo() {
        return claseRepository.findAllWithGrupo();
    }

    @Override
    public List<Clase> findActivas() {
        return claseRepository.findByActivoTrue();
    }

    @Override
    public List<Clase> findActivasWithGrupo() {
        return claseRepository.findAllActivasWithGrupo();
    }

    @Override
    public List<Clase> findByEstado(String estado) {
        return claseRepository.findByEstadoOrderByFechaHora(estado);
    }

    @Override
    public List<Clase> findByGrupo(Long grupoId) {
        return claseRepository.findByGrupoIdOrderByFechaHora(grupoId);
    }

    @Override
    public List<Clase> findActivasByGrupo(Long grupoId) {
        return claseRepository.findActivasByGrupoIdOrderByFechaHora(grupoId);
    }

    @Override
    public List<Clase> findByGrupoAndFecha(Long grupoId, LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return claseRepository.findByGrupoIdAndFecha(grupoId, fecha);
    }

    @Override
    public List<Clase> findByGrupoAndFechasBetween(Long grupoId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return claseRepository.findByGrupoIdAndFechasBetween(grupoId, fechaInicio, fechaFin);
    }

    @Override
    public List<Clase> findByFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return claseRepository.findByFechClase(fecha);
    }

    @Override
    public List<Clase> findByFechaWithGrupo(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return claseRepository.findByFechaWithGrupo(fecha);
    }

    @Override
    public List<Clase> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return claseRepository.findByFechClaseBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Clase> findByCategoria(Long categoriaId) {
        return claseRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Clase> findActivasByCategoria(Long categoriaId) {
        return claseRepository.findActivasByCategoriaId(categoriaId);
    }

    @Override
    public List<Clase> findBySucursal(Long sucursalId) {
        return claseRepository.findBySucursalId(sucursalId);
    }

    @Override
    public List<Clase> findActivasBySucursal(Long sucursalId) {
        return claseRepository.findActivasBySucursalId(sucursalId);
    }

    @Override
    public List<Clase> findByClub(Long clubId) {
        return claseRepository.findByClubId(clubId);
    }

    @Override
    public List<Clase> findActivasByClub(Long clubId) {
        return claseRepository.findActivasByClubId(clubId);
    }

    // ============================================================
    // VALIDACIONES Y UTILIDADES
    // ============================================================

    @Override
    public boolean existsById(Long id) {
        return claseRepository.existsById(id);
    }

    @Override
    public boolean isActiva(Long id) {
        Boolean activo = claseRepository.isClaseActiva(id);
        if (activo == null) {
            throw new RuntimeException("Clase no encontrada con ID: " + id);
        }
        return activo;
    }

    @Override
    public boolean existsClaseEnHorario(Long grupoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        validarHorario(horaInicio, horaFin);
        return claseRepository.existsClaseEnHorario(grupoId, fecha, horaInicio, horaFin, null);
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    @Override
    public EstadisticasClaseDTO getEstadisticas() {
        return EstadisticasClaseDTO.builder()
                .total(claseRepository.count())
                .activas(claseRepository.countClasesActivas())
                .inactivas(claseRepository.countClasesInactivas())
                .clasesPorEstado(claseRepository.countClasesByEstado())
                .clasesPorGrupo(claseRepository.countClasesByGrupo())
                .clasesActivasPorGrupo(claseRepository.countClasesActivasByGrupo())
                .clasesPorCategoria(claseRepository.countClasesByCategoria())
                .clasesPorSucursal(claseRepository.countClasesBySucursal())
                .clasesPorMes(claseRepository.countClasesByMes())
                .clasesPorDiaSemana(claseRepository.countClasesByDiaSemana())
                .build();
    }
}
