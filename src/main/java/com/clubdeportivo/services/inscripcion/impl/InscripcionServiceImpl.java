package com.clubdeportivo.services.inscripcion.impl;

import com.clubdeportivo.dtos.inscripcion.EstadisticasInscripcionDTO;
import com.clubdeportivo.models.Alumno;
import com.clubdeportivo.models.Grupo;
import com.clubdeportivo.models.Inscripcion;
import com.clubdeportivo.repositories.IAlumnoRepository;
import com.clubdeportivo.repositories.IGrupoRepository;
import com.clubdeportivo.repositories.IInscripcionRepository;
import com.clubdeportivo.services.inscripcion.IInscripcionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InscripcionServiceImpl implements IInscripcionService {
    private final IInscripcionRepository inscripcionRepository;
    private final IAlumnoRepository alumnoRepository;
    private final IGrupoRepository grupoRepository;

    /**
     * Validaciones privadas
     */
    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new RuntimeException("El estado es obligatorio");
        }

        String estadoLower = estado.toLowerCase();
        if (!estadoLower.equals("activa") &&
                !estadoLower.equals("inactiva") &&
                !estadoLower.equals("suspendida") &&
                !estadoLower.equals("finalizada")) {
            throw new RuntimeException("Estado no válido. Debe ser: activa, inactiva, suspendida o finalizada");
        }
    }

    private void validarMetodoPago(String metodoPago) {
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new RuntimeException("El método de pago es obligatorio");
        }
    }

    private void validarFechas(LocalDate fechInicio, LocalDate fechFin) {
        if (fechInicio == null || fechFin == null) {
            throw new RuntimeException("Las fechas de inicio y fin son obligatorias");
        }

        if (fechInicio.isAfter(fechFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }
    }

    private Alumno validarAlumno(Long alumnoId) {
        if (alumnoId == null) {
            throw new RuntimeException("La inscripción debe tener un alumno asociado");
        }

        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + alumnoId));

        if (!alumno.getActivo()) {
            throw new RuntimeException("No se puede crear una inscripción para un alumno inactivo");
        }

        return alumno;
    }

    private Grupo validarGrupo(Long grupoId) {
        if (grupoId == null) {
            throw new RuntimeException("La inscripción debe tener un grupo asociado");
        }

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + grupoId));

        if (!grupo.getActivo()) {
            throw new RuntimeException("No se puede crear una inscripción para un grupo inactivo");
        }

        return grupo;
    }

    private void validarCuposDisponibles(Long grupoId) {
        if (!grupoRepository.hasCuposDisponibles(grupoId)) {
            throw new RuntimeException("El grupo no tiene cupos disponibles");
        }
    }

    /**
     * Crud
     */
    @Override
    public Inscripcion create(Inscripcion inscripcion) {
        // Validar alumno
        Long alumnoId = inscripcion.getAlumno() != null ? inscripcion.getAlumno().getId() : null;
        Alumno alumno = validarAlumno(alumnoId);

        // Validar grupo
        Long grupoId = inscripcion.getGrupo() != null ? inscripcion.getGrupo().getId() : null;
        Grupo grupo = validarGrupo(grupoId);

        // Validar que el alumno no esté ya inscrito en el grupo
        if (inscripcionRepository.isAlumnoInscritoEnGrupo(alumnoId, grupoId)) {
            throw new RuntimeException("El alumno ya está inscrito en este grupo");
        }

        // Validar fechas
        validarFechas(inscripcion.getFechInicio(), inscripcion.getFechFin());

        // Validar estado
        validarEstado(inscripcion.getEstado());

        // Validar método de pago
        validarMetodoPago(inscripcion.getMetodoPago());

        // Verificar cupos disponibles en el grupo
        validarCuposDisponibles(grupoId);

        // Si no se especifica fecha de inscripción, usar la actual
        if (inscripcion.getFechInscripcion() == null) {
            inscripcion.setFechInscripcion(LocalDate.now());
        }

        inscripcion.setAlumno(alumno);
        inscripcion.setGrupo(grupo);

        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public Inscripcion update(Long id, Inscripcion inscripcionActualizada) {
        Inscripcion inscripcionExistente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));

        // Validar fechas
        validarFechas(inscripcionActualizada.getFechInicio(), inscripcionActualizada.getFechFin());

        // Validar estado
        validarEstado(inscripcionActualizada.getEstado());

        // Validar método de pago
        validarMetodoPago(inscripcionActualizada.getMetodoPago());

        // Si se cambia el alumno, validar que exista y esté activo
        Long nuevoAlumnoId = inscripcionActualizada.getAlumno() != null ?
                inscripcionActualizada.getAlumno().getId() : null;
        Long alumnoActualId = inscripcionExistente.getAlumno().getId();

        if (nuevoAlumnoId != null && !nuevoAlumnoId.equals(alumnoActualId)) {
            Alumno alumno = validarAlumno(nuevoAlumnoId);

            // Validar que el nuevo alumno no esté ya inscrito en el grupo
            Long grupoId = inscripcionActualizada.getGrupo() != null &&
                    inscripcionActualizada.getGrupo().getId() != null ?
                    inscripcionActualizada.getGrupo().getId() :
                    inscripcionExistente.getGrupo().getId();

            if (inscripcionRepository.isAlumnoInscritoEnGrupo(nuevoAlumnoId, grupoId)) {
                throw new RuntimeException("El alumno ya está inscrito en este grupo");
            }

            inscripcionExistente.setAlumno(alumno);
        }

        // Si se cambia el grupo, validar que exista y esté activo
        Long nuevoGrupoId = inscripcionActualizada.getGrupo() != null ?
                inscripcionActualizada.getGrupo().getId() : null;
        Long grupoActualId = inscripcionExistente.getGrupo().getId();

        if (nuevoGrupoId != null && !nuevoGrupoId.equals(grupoActualId)) {
            Grupo grupo = validarGrupo(nuevoGrupoId);

            // Verificar cupos disponibles en el nuevo grupo
            validarCuposDisponibles(nuevoGrupoId);

            // Validar que el alumno no esté ya inscrito en el nuevo grupo
            Long alumnoId = inscripcionActualizada.getAlumno() != null &&
                    inscripcionActualizada.getAlumno().getId() != null ?
                    inscripcionActualizada.getAlumno().getId() :
                    inscripcionExistente.getAlumno().getId();

            if (inscripcionRepository.isAlumnoInscritoEnGrupo(alumnoId, nuevoGrupoId)) {
                throw new RuntimeException("El alumno ya está inscrito en este grupo");
            }

            inscripcionExistente.setGrupo(grupo);
        }

        inscripcionExistente.setFechInicio(inscripcionActualizada.getFechInicio());
        inscripcionExistente.setFechFin(inscripcionActualizada.getFechFin());
        inscripcionExistente.setEstado(inscripcionActualizada.getEstado());
        inscripcionExistente.setMetodoPago(inscripcionActualizada.getMetodoPago());

        return inscripcionRepository.save(inscripcionExistente);
    }

    @Override
    public Inscripcion cambiarEstado(Long id, String nuevoEstado) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));

        validarEstado(nuevoEstado);

        String estadoLower = nuevoEstado.toLowerCase();

        // Si se activa, verificar cupos disponibles
        if (estadoLower.equals("activa") && !inscripcion.getEstado().equals("activa")) {
            validarCuposDisponibles(inscripcion.getGrupo().getId());
        }

        inscripcion.setEstado(estadoLower);
        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public Inscripcion activar(Long id) {
        return cambiarEstado(id, "activa");
    }

    @Override
    public Inscripcion suspender(Long id) {
        return cambiarEstado(id, "suspender");
    }

    @Override
    public Inscripcion finalizar(Long id) {
        return cambiarEstado(id, "finalizada");
    }

    @Override
    public void deletePhysical(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        }
        inscripcionRepository.deleteById(id);
    }

    @Override
    public Inscripcion findById(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));
    }

    @Override
    public Inscripcion findByIdWithAlumnoAndGrupo(Long id) {
        return inscripcionRepository.findByIdWithAlumnoAndGrupo(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));
    }

    @Override
    public Inscripcion findByIdWithAllRelations(Long id) {
        return inscripcionRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));
    }

    @Override
    public List<Inscripcion> findAll() {
        return inscripcionRepository.findAllByOrderByFechInscripcionDesc();
    }

    @Override
    public List<Inscripcion> findAllWithAlumnoAndGrupo() {
        return inscripcionRepository.findAllWithAlumnoAndGrupo();
    }

    @Override
    public List<Inscripcion> findByEstado(String estado) {
        return inscripcionRepository.findByEstadoOrderByFechInscripcionDesc(estado);
    }

    @Override
    public List<Inscripcion> findActivasWithAlumnoAndGrupo() {
        return inscripcionRepository.findAllActivasWithAlumnoAndGrupo();
    }

    @Override
    public List<Inscripcion> findByAlumno(Long alumnoId) {
        return inscripcionRepository.findByAlumnoIdOrderByFechInscripcionDesc(alumnoId);
    }

    @Override
    public List<Inscripcion> findActivasByAlumno(Long alumnoId) {
        return inscripcionRepository.findActivasByAlumnoId(alumnoId);
    }

    @Override
    public List<Inscripcion> findByGrupo(Long grupoId) {
        return inscripcionRepository.findByGrupoIdOrderByFechInscripcionDesc(grupoId);
    }

    @Override
    public List<Inscripcion> findActivasByGrupo(Long grupoId) {
        return inscripcionRepository.findActivasByGrupoId(grupoId);
    }

    @Override
    public List<Inscripcion> findByApoderado(Long apoderadoId) {
        return inscripcionRepository.findByApoderadoId(apoderadoId);
    }

    @Override
    public List<Inscripcion> findActivasByApoderado(Long apoderadoId) {
        return inscripcionRepository.findActivasByApoderadoId(apoderadoId);
    }

    @Override
    public List<Inscripcion> findByFechasInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return inscripcionRepository.findByFechInscripcionBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Inscripcion> findByCategoria(Long categoriaId) {
        return inscripcionRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Inscripcion> findActivasByCategoria(Long categoriaId) {
        return inscripcionRepository.findActivasByCategoriaId(categoriaId);
    }

    @Override
    public List<Inscripcion> findBySucursal(Long sucursalId) {
        return inscripcionRepository.findBySucursalId(sucursalId);
    }

    @Override
    public List<Inscripcion> findActivasBySucursal(Long sucursalId) {
        return inscripcionRepository.findActivasBySucursalId(sucursalId);
    }

    /**
     * Validaciones
     */
    @Override
    public boolean isAlumnoInscritoEnGrupo(Long alumnoId, Long grupoId) {
        return inscripcionRepository.isAlumnoInscritoEnGrupo(alumnoId, grupoId);
    }

    @Override
    public boolean hasInscripcionesActivas(Long grupoId) {
        return inscripcionRepository.hasInscripcionesActivas(grupoId);
    }

    @Override
    public boolean hasInscripcionesActivasByAlumno(Long alumnoId) {
        return inscripcionRepository.hasInscripcionesActivasByAlumno(alumnoId);
    }

    @Override
    public boolean isInscripcionActiva(Long id) {
        Boolean activa = inscripcionRepository.isInscripcionActiva(id);
        if (activa == null) {
            throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        }
        return activa;
    }

    /**
     * Estadistica
     */
    @Override
    public EstadisticasInscripcionDTO getEstadisticas() {
        return EstadisticasInscripcionDTO.builder()
                .total(inscripcionRepository.count())
                .activas(inscripcionRepository.countInscripcionesActivas())
                .inactivas(inscripcionRepository.countInscripcionesInactivas())
                .suspendidas(inscripcionRepository.countInscripcionesSuspendidas())
                .finalizadas(inscripcionRepository.countInscripcionesFinalizadas())
                .inscripcionesPorEstado(inscripcionRepository.countInscripcionesByEstado())
                .inscripcionesPorMetodoPago(inscripcionRepository.countInscripcionesByMetodoPago())
                .inscripcionesPorMes(inscripcionRepository.countInscripcionesByMes())
                .inscripcionesPorAlumno(inscripcionRepository.countInscripcionesByAlumno())
                .inscripcionesPorGrupo(inscripcionRepository.countInscripcionesByGrupo())
                .inscripcionesActivasPorGrupo(inscripcionRepository.countInscripcionesActivasByGrupo())
                .inscripcionesPorCategoria(inscripcionRepository.countInscripcionesByCategoria())
                .inscripcionesPorSucursal(inscripcionRepository.countInscripcionesBySucursal())
                .build();
    }
}
