package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAsistenciaRepository extends JpaRepository<Asistencia, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar asistencias por asistio (true/false)
     */
    List<Asistencia> findByAsistio(Boolean asistio);

    /**
     * Buscar asistencias por fecha de registro
     */
    List<Asistencia> findByFechRegistro(LocalDate fechRegistro);

    /**
     * Buscar asistencias por rango de fechas
     */
    List<Asistencia> findByFechRegistroBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar asistencias por fecha después de
     */
    List<Asistencia> findByFechRegistroAfter(LocalDate fecha);

    /**
     * Buscar asistencias por fecha antes de
     */
    List<Asistencia> findByFechRegistroBefore(LocalDate fecha);

    /**
     * Buscar asistencias por hora de llegada
     */
    List<Asistencia> findByHoraLlegada(LocalTime horaLlegada);

    /**
     * Buscar asistencias con hora de llegada después de
     */
    List<Asistencia> findByHoraLlegadaAfter(LocalTime hora);

    /**
     * Buscar asistencias con hora de llegada antes de
     */
    List<Asistencia> findByHoraLlegadaBefore(LocalTime hora);

    /**
     * Buscar asistencias donde asistio = true
     */
    List<Asistencia> findByAsistioTrue();

    /**
     * Buscar asistencias donde asistio = false
     */
    List<Asistencia> findByAsistioFalse();

    // ============================================================
    // BÚSQUEDAS POR CLASE
    // ============================================================

    /**
     * Buscar asistencias por ID de clase
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId")
    List<Asistencia> findByClaseId(@Param("claseId") Long claseId);

    /**
     * Buscar asistencias por ID de clase y asistio
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId AND a.asistio = :asistio")
    List<Asistencia> findByClaseIdAndAsistio(@Param("claseId") Long claseId,
                                             @Param("asistio") Boolean asistio);

    /**
     * Buscar asistencias donde asistio = true por ID de clase
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId AND a.asistio = true")
    List<Asistencia> findPresentesByClaseId(@Param("claseId") Long claseId);

    /**
     * Buscar asistencias donde asistio = false por ID de clase
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId AND a.asistio = false")
    List<Asistencia> findAusentesByClaseId(@Param("claseId") Long claseId);

    /**
     * Buscar asistencias por fecha de clase (a través de clase)
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.fechClase = :fecha")
    List<Asistencia> findByFechaClase(@Param("fecha") LocalDate fecha);

    /**
     * Buscar asistencias por rango de fechas de clase (a través de clase)
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.fechClase BETWEEN :fechaInicio AND :fechaFin")
    List<Asistencia> findByFechaClaseBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                             @Param("fechaFin") LocalDate fechaFin);

    // ============================================================
    // BÚSQUEDAS POR PAGO
    // ============================================================

    /**
     * Buscar asistencias por ID de pago
     */
    @Query("SELECT a FROM Asistencia a WHERE a.pago.id = :pagoId")
    List<Asistencia> findByPagoId(@Param("pagoId") Long pagoId);

    /**
     * Buscar asistencias por ID de pago y asistio
     */
    @Query("SELECT a FROM Asistencia a WHERE a.pago.id = :pagoId AND a.asistio = :asistio")
    List<Asistencia> findByPagoIdAndAsistio(@Param("pagoId") Long pagoId,
                                            @Param("asistio") Boolean asistio);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (CLASE + PAGO)
    // ============================================================

    /**
     * Buscar asistencias por clase y pago
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId AND a.pago.id = :pagoId")
    List<Asistencia> findByClaseIdAndPagoId(@Param("claseId") Long claseId,
                                            @Param("pagoId") Long pagoId);

    /**
     * Buscar asistencias por clase, pago y asistio
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId AND a.pago.id = :pagoId AND a.asistio = :asistio")
    List<Asistencia> findByClaseIdAndPagoIdAndAsistio(@Param("claseId") Long claseId,
                                                      @Param("pagoId") Long pagoId,
                                                      @Param("asistio") Boolean asistio);

    // ============================================================
    // BÚSQUEDAS POR GRUPO (A TRAVÉS DE CLASE)
    // ============================================================

    /**
     * Buscar asistencias por ID de grupo (a través de clase)
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.grupo.id = :grupoId")
    List<Asistencia> findByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar asistencias por ID de grupo y asistio (a través de clase)
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.grupo.id = :grupoId AND a.asistio = :asistio")
    List<Asistencia> findByGrupoIdAndAsistio(@Param("grupoId") Long grupoId,
                                             @Param("asistio") Boolean asistio);

    /**
     * Buscar asistencias por ID de grupo y fecha (a través de clase)
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.grupo.id = :grupoId AND a.fechRegistro = :fecha")
    List<Asistencia> findByGrupoIdAndFecha(@Param("grupoId") Long grupoId,
                                           @Param("fecha") LocalDate fecha);

    /**
     * Buscar asistencias por ID de grupo y rango de fechas (a través de clase)
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.grupo.id = :grupoId AND a.fechRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Asistencia> findByGrupoIdAndFechasBetween(@Param("grupoId") Long grupoId,
                                                   @Param("fechaInicio") LocalDate fechaInicio,
                                                   @Param("fechaFin") LocalDate fechaFin);

    // ============================================================
    // BÚSQUEDAS POR ALUMNO (A TRAVÉS DE PAGO -> INSCRIPCION)
    // NOTA: Como Alumno no tiene relación directa con Asistencia en tu modelo,
    // estas consultas deben ir a través de Pago -> Inscripcion -> Alumno
    // ============================================================

    /**
     * Buscar asistencias por ID de alumno (a través de pago -> inscripcion -> alumno)
     */
    @Query("SELECT a FROM Asistencia a " +
            "WHERE a.pago.id IN (" +
            "    SELECT p.id FROM Pago p " +
            "    WHERE p.inscripcion.alumno.id = :alumnoId" +
            ")")
    List<Asistencia> findByAlumnoId(@Param("alumnoId") Long alumnoId);

    /**
     * Buscar asistencias por ID de alumno y asistio (a través de pago -> inscripcion -> alumno)
     */
    @Query("SELECT a FROM Asistencia a " +
            "WHERE a.asistio = :asistio AND a.pago.id IN (" +
            "    SELECT p.id FROM Pago p " +
            "    WHERE p.inscripcion.alumno.id = :alumnoId" +
            ")")
    List<Asistencia> findByAlumnoIdAndAsistio(@Param("alumnoId") Long alumnoId,
                                              @Param("asistio") Boolean asistio);

    /**
     * Buscar asistencias por RUT de alumno (a través de pago -> inscripcion -> alumno)
     */
    @Query("SELECT a FROM Asistencia a " +
            "WHERE a.pago.id IN (" +
            "    SELECT p.id FROM Pago p " +
            "    WHERE p.inscripcion.alumno.rut = :rut" +
            ")")
    List<Asistencia> findByAlumnoRut(@Param("rut") String rut);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todas las asistencias ordenadas por fecha (más recientes primero)
     */
    List<Asistencia> findAllByOrderByFechRegistroDesc();

    /**
     * Buscar asistencias por clase ordenadas por fecha
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.id = :claseId ORDER BY a.fechRegistro DESC")
    List<Asistencia> findByClaseIdOrderByFechaDesc(@Param("claseId") Long claseId);

    /**
     * Buscar asistencias por grupo ordenadas por fecha
     */
    @Query("SELECT a FROM Asistencia a WHERE a.clase.grupo.id = :grupoId ORDER BY a.fechRegistro DESC")
    List<Asistencia> findByGrupoIdOrderByFechaDesc(@Param("grupoId") Long grupoId);

    /**
     * Buscar asistencias donde asistio = true ordenadas por fecha
     */
    List<Asistencia> findByAsistioTrueOrderByFechRegistroDesc();

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar asistencia por ID con su clase y pago (evita N+1)
     */
    @Query("SELECT a FROM Asistencia a LEFT JOIN FETCH a.clase LEFT JOIN FETCH a.pago WHERE a.id = :id")
    Optional<Asistencia> findByIdWithClaseAndPago(@Param("id") Long id);

    /**
     * Buscar asistencia por ID con todas sus relaciones (clase, pago, grupo)
     */
    @Query("SELECT a FROM Asistencia a " +
            "LEFT JOIN FETCH a.clase c " +
            "LEFT JOIN FETCH c.grupo g " +
            "LEFT JOIN FETCH a.pago " +
            "WHERE a.id = :id")
    Optional<Asistencia> findByIdWithAllRelations(@Param("id") Long id);

    /**
     * Buscar todas las asistencias con su clase y pago
     */
    @Query("SELECT a FROM Asistencia a LEFT JOIN FETCH a.clase LEFT JOIN FETCH a.pago")
    List<Asistencia> findAllWithClaseAndPago();

    /**
     * Buscar asistencias por clase con su clase y pago
     */
    @Query("SELECT a FROM Asistencia a LEFT JOIN FETCH a.clase LEFT JOIN FETCH a.pago WHERE a.clase.id = :claseId")
    List<Asistencia> findByClaseIdWithClaseAndPago(@Param("claseId") Long claseId);

    /**
     * Buscar asistencias por grupo con su clase y pago
     */
    @Query("SELECT a FROM Asistencia a LEFT JOIN FETCH a.clase LEFT JOIN FETCH a.pago WHERE a.clase.grupo.id = :grupoId")
    List<Asistencia> findByGrupoIdWithClaseAndPago(@Param("grupoId") Long grupoId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar asistencias totales
     */
    @Query("SELECT COUNT(a) FROM Asistencia a")
    Long countTotalAsistencias();

    /**
     * Contar asistencias por asistio (presentes)
     */
    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.asistio = true")
    Long countAsistenciasPresentes();

    /**
     * Contar asistencias por asistio (ausentes)
     */
    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.asistio = false")
    Long countAsistenciasAusentes();

    /**
     * Contar asistencias por clase
     */
    @Query("SELECT a.clase.id, COUNT(a) FROM Asistencia a GROUP BY a.clase.id")
    List<Object[]> countAsistenciasByClase();

    /**
     * Contar asistencias por grupo (a través de clase)
     */
    @Query("SELECT a.clase.grupo.nombre, COUNT(a) FROM Asistencia a GROUP BY a.clase.grupo.nombre")
    List<Object[]> countAsistenciasByGrupo();

    /**
     * Contar asistencias por mes
     */
    @Query("SELECT EXTRACT(YEAR FROM a.fechRegistro) AS anio, " +
            "EXTRACT(MONTH FROM a.fechRegistro) AS mes, " +
            "COUNT(a) AS total " +
            "FROM Asistencia a " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countAsistenciasByMes();

    /**
     * Calcular porcentaje de asistencia por clase
     */
    @Query("SELECT a.clase.id, " +
            "SUM(CASE WHEN a.asistio = true THEN 1 ELSE 0 END) AS presentes, " +
            "COUNT(a) AS total, " +
            "(SUM(CASE WHEN a.asistio = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a)) AS porcentaje " +
            "FROM Asistencia a " +
            "GROUP BY a.clase.id")
    List<Object[]> calcularPorcentajeAsistenciaByClase();

    /**
     * Calcular porcentaje de asistencia por grupo (a través de clase)
     */
    @Query("SELECT a.clase.grupo.nombre, " +
            "SUM(CASE WHEN a.asistio = true THEN 1 ELSE 0 END) AS presentes, " +
            "COUNT(a) AS total, " +
            "(SUM(CASE WHEN a.asistio = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a)) AS porcentaje " +
            "FROM Asistencia a " +
            "GROUP BY a.clase.grupo.nombre")
    List<Object[]> calcularPorcentajeAsistenciaByGrupo();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si existe una asistencia para una clase y pago específicos
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Asistencia a WHERE a.clase.id = :claseId AND a.pago.id = :pagoId")
    Boolean existsByClaseIdAndPagoId(@Param("claseId") Long claseId,
                                     @Param("pagoId") Long pagoId);

    /**
     * Verificar si un pago tiene asistencias asociadas
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Asistencia a WHERE a.pago.id = :pagoId")
    Boolean hasAsistenciasByPagoId(@Param("pagoId") Long pagoId);

    /**
     * Verificar si una clase tiene asistencias asociadas
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Asistencia a WHERE a.clase.id = :claseId")
    Boolean hasAsistenciasByClaseId(@Param("claseId") Long claseId);
}
