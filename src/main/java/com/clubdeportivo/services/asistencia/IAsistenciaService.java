package com.clubdeportivo.services.asistencia;

import com.clubdeportivo.dtos.asistencia.EstadisticasAsistenciaDTO;
import com.clubdeportivo.models.Asistencia;

import java.time.LocalDate;
import java.util.List;

public interface IAsistenciaService {
    /**
     * Crear una nueva asistencia
     */
    Asistencia create(Asistencia asistencia);

    /**
     * Registrar asistencia con hora de llegada automática
     */
    Asistencia registrarAsistencia(Long claseId, Long pagoId, Boolean asistio);

    /**
     * Actualizar una asistencia existente
     */
    Asistencia update(Long id, Asistencia asistenciaActualizada);

    /**
     * Marcar asistencia como presente
     */
    Asistencia marcarPresente(Long id);

    /**
     * Marcar asistencia como ausente
     */
    Asistencia marcarAusente(Long id);

    /**
     * Eliminar una asistencia (borrado físico)
     */
    void deletePhysical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar asistencia por ID
     */
    Asistencia findById(Long id);

    /**
     * Buscar asistencia por ID con su clase y pago
     */
    Asistencia findByIdWithClaseAndPago(Long id);

    /**
     * Buscar asistencia por ID con todas sus relaciones
     */
    Asistencia findByIdWithAllRelations(Long id);

    /**
     * Buscar todas las asistencias
     */
    List<Asistencia> findAll();

    /**
     * Buscar todas las asistencias con su clase y pago
     */
    List<Asistencia> findAllWithClaseAndPago();

    /**
     * Buscar asistencias por clase
     */
    List<Asistencia> findByClase(Long claseId);

    /**
     * Buscar asistencias presentes por clase
     */
    List<Asistencia> findPresentesByClase(Long claseId);

    /**
     * Buscar asistencias ausentes por clase
     */
    List<Asistencia> findAusentesByClase(Long claseId);

    /**
     * Buscar asistencias por grupo
     */
    List<Asistencia> findByGrupo(Long grupoId);

    /**
     * Buscar asistencias por pago
     */
    List<Asistencia> findByPago(Long pagoId);

    /**
     * Buscar asistencias por fecha
     */
    List<Asistencia> findByFecha(LocalDate fecha);

    /**
     * Buscar asistencias por rango de fechas
     */
    List<Asistencia> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar asistencias por alumno (a través de pago -> inscripcion)
     */
    List<Asistencia> findByAlumno(Long alumnoId);

    /**
     * Buscar asistencias presentes por alumno
     */
    List<Asistencia> findPresentesByAlumno(Long alumnoId);

    /**
     * Buscar asistencias ausentes por alumno
     */
    List<Asistencia> findAusentesByAlumno(Long alumnoId);

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si existe una asistencia para una clase y pago
     */
    boolean existsByClaseAndPago(Long claseId, Long pagoId);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de asistencias
     */
    EstadisticasAsistenciaDTO getEstadisticas();
}
