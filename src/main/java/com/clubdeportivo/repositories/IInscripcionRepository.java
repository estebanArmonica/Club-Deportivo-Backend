package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IInscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar inscripciones por estado
     */
    List<Inscripcion> findByEstado(String estado);

    /**
     * Buscar inscripciones por estado (case insensitive)
     */
    List<Inscripcion> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar inscripciones por método de pago
     */
    List<Inscripcion> findByMetodoPago(String metodoPago);

    /**
     * Buscar inscripciones por fecha de inscripción
     */
    List<Inscripcion> findByFechInscripcion(LocalDate fechInscripcion);

    /**
     * Buscar inscripciones por rango de fechas de inscripción
     */
    List<Inscripcion> findByFechInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar inscripciones por rango de fechas de inicio
     */
    List<Inscripcion> findByFechInicioBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar inscripciones por rango de fechas de fin
     */
    List<Inscripcion> findByFechFinBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar inscripciones con fecha de fin antes de una fecha específica
     */
    List<Inscripcion> findByFechFinBefore(LocalDate fecha);

    /**
     * Buscar inscripciones con fecha de fin después de una fecha específica
     */
    List<Inscripcion> findByFechFinAfter(LocalDate fecha);


    // ============================================================
    // BÚSQUEDAS POR ALUMNO
    // ============================================================

    /**
     * Buscar inscripciones por ID de alumno
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :alumnoId")
    List<Inscripcion> findByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Buscar inscripciones activas por ID de alumno
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :alumnoId AND i.estado = 'activa'")
    List<Inscripcion> findActivasByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Buscar inscripciones por RUT de alumno
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.rut = :rut")
    List<Inscripcion> findByAlumnoRut(@Param("rut") String rut);

    /**
     * Buscar inscripciones activas por RUT de alumno
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.rut = :rut AND i.estado = 'activa'")
    List<Inscripcion> findActivasByAlumnoRut(@Param("rut") String rut);

    /**
     * Buscar inscripciones por nombre de alumno
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.nombre = :nombre")
    List<Inscripcion> findByAlumnoNombre(@Param("nombre") String nombre);

    /**
     * Buscar inscripciones por apellido de alumno
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.apellido = :apellido")
    List<Inscripcion> findByAlumnoApellido(@Param("apellido") String apellido);

    // ============================================================
    // BÚSQUEDAS POR GRUPO
    // ============================================================

    /**
     * Buscar inscripciones por ID de grupo
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.id = :grupoId")
    List<Inscripcion> findByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar inscripciones activas por ID de grupo
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.id = :grupoId AND i.estado = 'activa'")
    List<Inscripcion> findActivasByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar inscripciones por nombre de grupo
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.nombre = :nombreGrupo")
    List<Inscripcion> findByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    /**
     * Buscar inscripciones activas por nombre de grupo
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.nombre = :nombreGrupo AND i.estado = 'activa'")
    List<Inscripcion> findActivasByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (ALUMNO + GRUPO)
    // ============================================================

    /**
     * Buscar inscripciones por alumno y grupo
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :alumnoId AND i.grupo.id = :grupoId")
    List<Inscripcion> findByAlumnoIdAndGrupoId(@Param("alumnoId") Long alumnoId,
                                               @Param("grupoId") Long grupoId);

    /**
     * Buscar inscripciones activas por alumno y grupo
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :alumnoId AND i.grupo.id = :grupoId AND i.estado = 'activa'")
    List<Inscripcion> findActivasByAlumnoIdAndGrupoId(@Param("alumnoId") Long alumnoId,
                                                      @Param("grupoId") Long grupoId);

    // ============================================================
    // BÚSQUEDAS POR CATEGORÍA (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar inscripciones por ID de categoría (a través de grupo)
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.cate.id = :categoriaId")
    List<Inscripcion> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar inscripciones activas por ID de categoría (a través de grupo)
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.cate.id = :categoriaId AND i.estado = 'activa'")
    List<Inscripcion> findActivasByCategoriaId(@Param("categoriaId") Long categoriaId);

    // ============================================================
    // BÚSQUEDAS POR SUCURSAL (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar inscripciones por ID de sucursal (a través de grupo)
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.sucursal.id = :sucursalId")
    List<Inscripcion> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar inscripciones activas por ID de sucursal (a través de grupo)
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.sucursal.id = :sucursalId AND i.estado = 'activa'")
    List<Inscripcion> findActivasBySucursalId(@Param("sucursalId") Long sucursalId);

    // ============================================================
    // BÚSQUEDAS POR APODERADO (A TRAVÉS DE ALUMNO)
    // ============================================================

    /**
     * Buscar inscripciones por ID de apoderado (a través de alumno)
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id IN " +
            "(SELECT a.id FROM Alumno a JOIN a.apoderados ap WHERE ap.id = :apoderadoId)")
    List<Inscripcion> findByApoderadoId(@Param("apoderadoId") Long apoderadoId);

    /**
     * Buscar inscripciones activas por ID de apoderado (a través de alumno)
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.estado = 'activa' AND i.alumno.id IN " +
            "(SELECT a.id FROM Alumno a JOIN a.apoderados ap WHERE ap.id = :apoderadoId)")
    List<Inscripcion> findActivasByApoderadoId(@Param("apoderadoId") Long apoderadoId);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todas las inscripciones ordenadas por fecha de inscripción (más recientes primero)
     */
    List<Inscripcion> findAllByOrderByFechInscripcionDesc();

    /**
     * Buscar inscripciones por estado ordenadas por fecha de inscripción
     */
    List<Inscripcion> findByEstadoOrderByFechInscripcionDesc(String estado);

    /**
     * Buscar inscripciones por alumno ordenadas por fecha de inscripción
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.alumno.id = :alumnoId ORDER BY i.fechInscripcion DESC")
    List<Inscripcion> findByAlumnoIdOrderByFechInscripcionDesc(@Param("alumnoId") Long alumnoId);

    /**
     * Buscar inscripciones por grupo ordenadas por fecha de inscripción
     */
    @Query("SELECT i FROM Inscripcion i WHERE i.grupo.id = :grupoId ORDER BY i.fechInscripcion DESC")
    List<Inscripcion> findByGrupoIdOrderByFechInscripcionDesc(@Param("grupoId") Long grupoId);

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar inscripción por ID con su alumno y grupo (evita N+1)
     */
    @Query("SELECT i FROM Inscripcion i LEFT JOIN FETCH i.alumno LEFT JOIN FETCH i.grupo WHERE i.id = :id")
    Optional<Inscripcion> findByIdWithAlumnoAndGrupo(@Param("id") Long id);

    /**
     * Buscar inscripción por ID con todas sus relaciones (alumno, grupo, categoría, sucursal)
     */
    @Query("SELECT i FROM Inscripcion i " +
            "LEFT JOIN FETCH i.alumno " +
            "LEFT JOIN FETCH i.grupo g " +
            "LEFT JOIN FETCH g.cate " +
            "LEFT JOIN FETCH g.sucursal " +
            "WHERE i.id = :id")
    Optional<Inscripcion> findByIdWithAllRelations(@Param("id") Long id);

    /**
     * Buscar todas las inscripciones con su alumno y grupo
     */
    @Query("SELECT i FROM Inscripcion i LEFT JOIN FETCH i.alumno LEFT JOIN FETCH i.grupo")
    List<Inscripcion> findAllWithAlumnoAndGrupo();

    /**
     * Buscar inscripciones activas con su alumno y grupo
     */
    @Query("SELECT i FROM Inscripcion i LEFT JOIN FETCH i.alumno LEFT JOIN FETCH i.grupo WHERE i.estado = 'activa'")
    List<Inscripcion> findAllActivasWithAlumnoAndGrupo();

    /**
     * Buscar inscripciones por alumno con su alumno y grupo
     */
    @Query("SELECT i FROM Inscripcion i LEFT JOIN FETCH i.alumno LEFT JOIN FETCH i.grupo WHERE i.alumno.id = :alumnoId")
    List<Inscripcion> findByAlumnoIdWithAlumnoAndGrupo(@Param("alumnoId") Long alumnoId);

    /**
     * Buscar inscripciones por grupo con su alumno y grupo
     */
    @Query("SELECT i FROM Inscripcion i LEFT JOIN FETCH i.alumno LEFT JOIN FETCH i.grupo WHERE i.grupo.id = :grupoId")
    List<Inscripcion> findByGrupoIdWithAlumnoAndGrupo(@Param("grupoId") Long grupoId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar inscripciones por estado
     */
    @Query("SELECT i.estado, COUNT(i) FROM Inscripcion i GROUP BY i.estado")
    List<Object[]> countInscripcionesByEstado();

    /**
     * Contar inscripciones activas
     */
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.estado = 'activa'")
    Long countInscripcionesActivas();

    /**
     * Contar inscripciones inactivas
     */
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.estado = 'inactiva'")
    Long countInscripcionesInactivas();

    /**
     * Contar inscripciones suspendidas
     */
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.estado = 'suspendida'")
    Long countInscripcionesSuspendidas();

    /**
     * Contar inscripciones finalizadas
     */
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.estado = 'finalizada'")
    Long countInscripcionesFinalizadas();

    /**
     * Contar inscripciones por método de pago
     */
    @Query("SELECT i.metodoPago, COUNT(i) FROM Inscripcion i GROUP BY i.metodoPago")
    List<Object[]> countInscripcionesByMetodoPago();

    /**
     * Contar inscripciones por mes
     */
    @Query("SELECT EXTRACT(YEAR FROM i.fechInscripcion) AS anio, " +
            "EXTRACT(MONTH FROM i.fechInscripcion) AS mes, " +
            "COUNT(i) AS total " +
            "FROM Inscripcion i " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countInscripcionesByMes();

    /**
     * Contar inscripciones por alumno
     */
    @Query("SELECT CONCAT(i.alumno.nombre, ' ', i.alumno.apellido), COUNT(i) FROM Inscripcion i GROUP BY i.alumno.id, i.alumno.nombre, i.alumno.apellido")
    List<Object[]> countInscripcionesByAlumno();

    /**
     * Contar inscripciones por grupo
     */
    @Query("SELECT i.grupo.nombre, COUNT(i) FROM Inscripcion i GROUP BY i.grupo.nombre")
    List<Object[]> countInscripcionesByGrupo();

    /**
     * Contar inscripciones activas por grupo
     */
    @Query("SELECT i.grupo.nombre, COUNT(i) FROM Inscripcion i WHERE i.estado = 'activa' GROUP BY i.grupo.nombre")
    List<Object[]> countInscripcionesActivasByGrupo();

    /**
     * Contar inscripciones por categoría (a través de grupo)
     */
    @Query("SELECT i.grupo.cate.nombre, COUNT(i) FROM Inscripcion i GROUP BY i.grupo.cate.nombre")
    List<Object[]> countInscripcionesByCategoria();

    /**
     * Contar inscripciones por sucursal (a través de grupo)
     */
    @Query("SELECT i.grupo.sucursal.nombre, COUNT(i) FROM Inscripcion i GROUP BY i.grupo.sucursal.nombre")
    List<Object[]> countInscripcionesBySucursal();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si un alumno está inscrito en un grupo
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Inscripcion i " +
            "WHERE i.alumno.id = :alumnoId " +
            "AND i.grupo.id = :grupoId " +
            "AND i.estado = 'activa'")
    Boolean isAlumnoInscritoEnGrupo(@Param("alumnoId") Long alumnoId,
                                    @Param("grupoId") Long grupoId);

    /**
     * Verificar si un grupo tiene inscripciones activas
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Inscripcion i " +
            "WHERE i.grupo.id = :grupoId AND i.estado = 'activa'")
    Boolean hasInscripcionesActivas(@Param("grupoId") Long grupoId);

    /**
     * Verificar si un alumno tiene inscripciones activas
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Inscripcion i " +
            "WHERE i.alumno.id = :alumnoId AND i.estado = 'activa'")
    Boolean hasInscripcionesActivasByAlumno(@Param("alumnoId") Long alumnoId);

    /**
     * Verificar si una inscripción está activa
     */
    @Query("SELECT i.estado = 'activa' FROM Inscripcion i WHERE i.id = :id")
    Boolean isInscripcionActiva(@Param("id") Long id);

    // ============================================================
    // CONSULTAS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener solo ID y estado de inscripciones activas (para combos/selects)
     */
    @Query("SELECT i.id, i.estado FROM Inscripcion i WHERE i.estado = 'activa' ORDER BY i.fechInscripcion DESC")
    List<Object[]> findIdAndEstadoByActiva();

    /**
     * Obtener inscripciones con detalles para reportes
     */
    @Query("SELECT i.id, i.fechInscripcion, i.fechInicio, i.fechFin, i.estado, " +
            "CONCAT(i.alumno.nombre, ' ', i.alumno.apellido) AS alumno, " +
            "i.grupo.nombre AS grupo " +
            "FROM Inscripcion i " +
            "WHERE i.estado = :estado " +
            "ORDER BY i.fechInscripcion DESC")
    List<Object[]> findReporteByEstado(@Param("estado") String estado);
}
