package com.clubdeportivo.services.inscripcion;

import com.clubdeportivo.dtos.inscripcion.EstadisticasInscripcionDTO;
import com.clubdeportivo.models.Inscripcion;

import java.time.LocalDate;
import java.util.List;

public interface IInscripcionService {
    /**
     * Crear una nueva inscripción
     */
    Inscripcion create(Inscripcion inscripcion);

    /**
     * Actualizar una inscripción existente
     */
    Inscripcion update(Long id, Inscripcion inscripcionActualizada);

    /**
     * Cambiar estado de una inscripción
     */
    Inscripcion cambiarEstado(Long id, String nuevoEstado);

    /**
     * Activar una inscripción
     */
    Inscripcion activar(Long id);

    /**
     * Suspender una inscripción
     */
    Inscripcion suspender(Long id);

    /**
     * Finalizar una inscripción
     */
    Inscripcion finalizar(Long id);

    /**
     * Eliminar una inscripción (borrado físico)
     */
    void deletePhysical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar inscripción por ID
     */
    Inscripcion findById(Long id);

    /**
     * Buscar inscripción por ID con su alumno y grupo
     */
    Inscripcion findByIdWithAlumnoAndGrupo(Long id);

    /**
     * Buscar inscripción por ID con todas sus relaciones
     */
    Inscripcion findByIdWithAllRelations(Long id);

    /**
     * Buscar todas las inscripciones
     */
    List<Inscripcion> findAll();

    /**
     * Buscar todas las inscripciones con su alumno y grupo
     */
    List<Inscripcion> findAllWithAlumnoAndGrupo();

    /**
     * Buscar inscripciones por estado
     */
    List<Inscripcion> findByEstado(String estado);

    /**
     * Buscar inscripciones activas con su alumno y grupo
     */
    List<Inscripcion> findActivasWithAlumnoAndGrupo();

    /**
     * Buscar inscripciones por alumno
     */
    List<Inscripcion> findByAlumno(Long alumnoId);

    /**
     * Buscar inscripciones activas por alumno
     */
    List<Inscripcion> findActivasByAlumno(Long alumnoId);

    /**
     * Buscar inscripciones por grupo
     */
    List<Inscripcion> findByGrupo(Long grupoId);

    /**
     * Buscar inscripciones activas por grupo
     */
    List<Inscripcion> findActivasByGrupo(Long grupoId);

    /**
     * Buscar inscripciones por apoderado (a través de alumno)
     */
    List<Inscripcion> findByApoderado(Long apoderadoId);

    /**
     * Buscar inscripciones activas por apoderado
     */
    List<Inscripcion> findActivasByApoderado(Long apoderadoId);

    /**
     * Buscar inscripciones por rango de fechas
     */
    List<Inscripcion> findByFechasInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar inscripciones por categoría (a través de grupo)
     */
    List<Inscripcion> findByCategoria(Long categoriaId);

    /**
     * Buscar inscripciones activas por categoría (a través de grupo)
     */
    List<Inscripcion> findActivasByCategoria(Long categoriaId);

    /**
     * Buscar inscripciones por sucursal (a través de grupo)
     */
    List<Inscripcion> findBySucursal(Long sucursalId);

    /**
     * Buscar inscripciones activas por sucursal (a través de grupo)
     */
    List<Inscripcion> findActivasBySucursal(Long sucursalId);

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si un alumno está inscrito en un grupo
     */
    boolean isAlumnoInscritoEnGrupo(Long alumnoId, Long grupoId);

    /**
     * Verificar si un grupo tiene inscripciones activas
     */
    boolean hasInscripcionesActivas(Long grupoId);

    /**
     * Verificar si un alumno tiene inscripciones activas
     */
    boolean hasInscripcionesActivasByAlumno(Long alumnoId);

    /**
     * Verificar si una inscripción está activa
     */
    boolean isInscripcionActiva(Long id);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de inscripciones
     */
    EstadisticasInscripcionDTO getEstadisticas();
}
