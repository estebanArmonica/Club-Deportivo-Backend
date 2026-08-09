package com.clubdeportivo.services.alumno;

import com.clubdeportivo.models.Alumno;
import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAlumnoService {
    /**
     * Crear un nuevo alumno
     */
    Alumno create(Alumno alumno);

    /**
     * Actualizar un alumno existente
     */
    Alumno update(Long id, Alumno alumnoActualizado);

    /**
     * Eliminar un alumno (borrado lógico)
     */
    void deleteLogical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar alumno por ID
     */
    Alumno findById(Long id);

    /**
     * Buscar alumno por ID con sus apoderados
     */
    Alumno findByIdWithApoderados(Long id);

    /**
     * Buscar alumno por RUT
     */
    Alumno findByRut(String rut);

    /**
     * Buscar todos los alumnos
     */
    List<Alumno> findAll();

    /**
     * Buscar todos los alumnos con paginación
     */
    Page<Alumno> findAll(Pageable pageable);

    /**
     * Buscar todos los alumnos con sus apoderados
     */
    List<Alumno> findAllWithApoderados();

    /**
     * Buscar alumnos activos
     */
    List<Alumno> findActivos();

    /**
     * Buscar alumnos inactivos
     */
    List<Alumno> findInactivos();

    /**
     * Buscar alumnos por nombre o apellido (búsqueda parcial)
     */
    List<Alumno> searchByNombreOrApellido(String nombre, String apellido);

    /**
     * Buscar alumnos por rango de fechas de inscripción
     */
    List<Alumno> findByFechInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar alumnos por apoderado
     */
    List<Alumno> findByApoderado(Long apoderadoId);

    /**
     * Buscar alumnos por grupo (a través de inscripciones)
     */
    List<Alumno> findAlumnosActivosByGrupo(Long grupoId);

    /**
     * Buscar alumnos que asistieron a una clase
     */
    List<Alumno> findAlumnosQueAsistieronAClase(Long claseId);

    /**
     * Buscar alumnos que NO asistieron a una clase
     */
    List<Alumno> findAlumnosQueNoAsistieronAClase(Long claseId);

    /**
     * Buscar alumnos por rango de edad
     */
    List<Alumno> findByRangoEdad(int edadMin, int edadMax);

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si un alumno existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si un alumno existe por RUT
     */
    boolean existsByRut(String rut);

    /**
     * Verificar si un alumno está activo
     */
    boolean isActivo(Long id);

    /**
     * Verificar si un alumno está inscrito en un grupo
     */
    boolean isAlumnoInscritoEnGrupo(Long alumnoId, Long grupoId);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Contar alumnos activos
     */
    Long countAlumnosActivos();

    /**
     * Obtener alumnos con más asistencias
     */
    List<Object[]> findAlumnosConMasAsistencias();
}
