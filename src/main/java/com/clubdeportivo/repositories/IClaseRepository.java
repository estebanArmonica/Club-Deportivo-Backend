package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClaseRepository extends JpaRepository<Clase, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar clases por estado
     */
    List<Clase> findByEstado(String estado);

    /**
     * Buscar clases por estado (case insensitive)
     */
    List<Clase> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar clases activas
     */
    List<Clase> findByActivoTrue();

    /**
     * Buscar clases inactivas
     */
    List<Clase> findByActivoFalse();

    /**
     * Buscar clases por fecha
     */
    List<Clase> findByFechClase(LocalDate fechClase);

    /**
     * Buscar clases por rango de fechas
     */
    List<Clase> findByFechClaseBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar clases por fecha después de
     */
    List<Clase> findByFechClaseAfter(LocalDate fecha);

    /**
     * Buscar clases por fecha antes de
     */
    List<Clase> findByFechClaseBefore(LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR HORARIO
    // ============================================================

    /**
     * Buscar clases por hora de inicio
     */
    List<Clase> findByHoraInicio(LocalTime horaInicio);

    /**
     * Buscar clases por hora de fin
     */
    List<Clase> findByHoraFin(LocalTime horaFin);

    /**
     * Buscar clases que inicien después de una hora específica
     */
    List<Clase> findByHoraInicioAfter(LocalTime hora);

    /**
     * Buscar clases que inicien antes de una hora específica
     */
    List<Clase> findByHoraInicioBefore(LocalTime hora);

    /**
     * Buscar clases por rango de horario
     */
    @Query("SELECT c FROM Clase c WHERE c.horaInicio >= :horaInicio AND c.horaFin <= :horaFin")
    List<Clase> findByHorarioBetween(@Param("horaInicio") LocalTime horaInicio,
                                     @Param("horaFin") LocalTime horaFin);

    // ============================================================
    // BÚSQUEDAS POR GRUPO
    // ============================================================

    /**
     * Buscar clases por ID de grupo
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId")
    List<Clase> findByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar clases activas por ID de grupo
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId AND c.activo = true")
    List<Clase> findActivasByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar clases por ID de grupo y estado
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId AND c.estado = :estado")
    List<Clase> findByGrupoIdAndEstado(@Param("grupoId") Long grupoId,
                                       @Param("estado") String estado);

    /**
     * Buscar clases por nombre de grupo
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.nombre = :nombreGrupo")
    List<Clase> findByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    /**
     * Buscar clases activas por nombre de grupo
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.nombre = :nombreGrupo AND c.activo = true")
    List<Clase> findActivasByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    // ============================================================
    // BÚSQUEDAS POR GRUPO Y FECHA
    // ============================================================

    /**
     * Buscar clases por grupo y fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId AND c.fechClase = :fecha")
    List<Clase> findByGrupoIdAndFecha(@Param("grupoId") Long grupoId,
                                      @Param("fecha") LocalDate fecha);

    /**
     * Buscar clases activas por grupo y fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId AND c.fechClase = :fecha AND c.activo = true")
    List<Clase> findActivasByGrupoIdAndFecha(@Param("grupoId") Long grupoId,
                                             @Param("fecha") LocalDate fecha);

    /**
     * Buscar clases por grupo y rango de fechas
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId AND c.fechClase BETWEEN :fechaInicio AND :fechaFin")
    List<Clase> findByGrupoIdAndFechasBetween(@Param("grupoId") Long grupoId,
                                              @Param("fechaInicio") LocalDate fechaInicio,
                                              @Param("fechaFin") LocalDate fechaFin);

    // ============================================================
    // BÚSQUEDAS POR CATEGORÍA (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar clases por ID de categoría (a través de grupo)
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.cate.id = :categoriaId")
    List<Clase> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar clases activas por ID de categoría (a través de grupo)
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.cate.id = :categoriaId AND c.activo = true")
    List<Clase> findActivasByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar clases por categoría y fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.cate.id = :categoriaId AND c.fechClase = :fecha")
    List<Clase> findByCategoriaIdAndFecha(@Param("categoriaId") Long categoriaId,
                                          @Param("fecha") LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR SUCURSAL (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar clases por ID de sucursal (a través de grupo)
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.sucursal.id = :sucursalId")
    List<Clase> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar clases activas por ID de sucursal (a través de grupo)
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.sucursal.id = :sucursalId AND c.activo = true")
    List<Clase> findActivasBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar clases por sucursal y fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.sucursal.id = :sucursalId AND c.fechClase = :fecha")
    List<Clase> findBySucursalIdAndFecha(@Param("sucursalId") Long sucursalId,
                                         @Param("fecha") LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR CLUB (A TRAVÉS DE GRUPO -> SUCURSAL)
    // ============================================================

    /**
     * Buscar clases por ID de club (a través de grupo -> sucursal)
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.sucursal.club.id = :clubId")
    List<Clase> findByClubId(@Param("clubId") Long clubId);

    /**
     * Buscar clases activas por ID de club (a través de grupo -> sucursal)
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.sucursal.club.id = :clubId AND c.activo = true")
    List<Clase> findActivasByClubId(@Param("clubId") Long clubId);

    // ============================================================
    // BÚSQUEDAS POR ESTADO Y FECHA
    // ============================================================

    /**
     * Buscar clases por estado y fecha
     */
    List<Clase> findByEstadoAndFechClase(String estado, LocalDate fecha);

    /**
     * Buscar clases por estado y rango de fechas
     */
    List<Clase> findByEstadoAndFechClaseBetween(String estado, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar clases por estado y grupo
     */
    @Query("SELECT c FROM Clase c WHERE c.estado = :estado AND c.grupo.id = :grupoId")
    List<Clase> findByEstadoAndGrupoId(@Param("estado") String estado,
                                       @Param("grupoId") Long grupoId);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todas las clases ordenadas por fecha y hora
     */
    @Query("SELECT c FROM Clase c ORDER BY c.fechClase DESC, c.horaInicio ASC")
    List<Clase> findAllOrderByFechaHora();

    /**
     * Buscar clases por estado ordenadas por fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.estado = :estado ORDER BY c.fechClase DESC, c.horaInicio ASC")
    List<Clase> findByEstadoOrderByFechaHora(@Param("estado") String estado);

    /**
     * Buscar clases por grupo ordenadas por fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId ORDER BY c.fechClase DESC, c.horaInicio ASC")
    List<Clase> findByGrupoIdOrderByFechaHora(@Param("grupoId") Long grupoId);

    /**
     * Buscar clases activas por grupo ordenadas por fecha
     */
    @Query("SELECT c FROM Clase c WHERE c.grupo.id = :grupoId AND c.activo = true ORDER BY c.fechClase DESC, c.horaInicio ASC")
    List<Clase> findActivasByGrupoIdOrderByFechaHora(@Param("grupoId") Long grupoId);

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar clase por ID con su grupo (evita N+1)
     */
    @Query("SELECT c FROM Clase c LEFT JOIN FETCH c.grupo WHERE c.id = :id")
    Optional<Clase> findByIdWithGrupo(@Param("id") Long id);

    /**
     * Buscar clase por ID con todas sus relaciones (grupo, categoría, sucursal)
     */
    @Query("SELECT c FROM Clase c " +
            "LEFT JOIN FETCH c.grupo g " +
            "LEFT JOIN FETCH g.cate " +
            "LEFT JOIN FETCH g.sucursal " +
            "WHERE c.id = :id")
    Optional<Clase> findByIdWithAllRelations(@Param("id") Long id);

    /**
     * Buscar todas las clases con su grupo
     */
    @Query("SELECT c FROM Clase c LEFT JOIN FETCH c.grupo")
    List<Clase> findAllWithGrupo();

    /**
     * Buscar clases activas con su grupo
     */
    @Query("SELECT c FROM Clase c LEFT JOIN FETCH c.grupo WHERE c.activo = true")
    List<Clase> findAllActivasWithGrupo();

    /**
     * Buscar clases por grupo con su grupo
     */
    @Query("SELECT c FROM Clase c LEFT JOIN FETCH c.grupo WHERE c.grupo.id = :grupoId")
    List<Clase> findByGrupoIdWithGrupo(@Param("grupoId") Long grupoId);

    /**
     * Buscar clases por fecha con su grupo
     */
    @Query("SELECT c FROM Clase c LEFT JOIN FETCH c.grupo WHERE c.fechClase = :fecha")
    List<Clase> findByFechaWithGrupo(@Param("fecha") LocalDate fecha);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar clases por estado
     */
    @Query("SELECT c.estado, COUNT(c) FROM Clase c GROUP BY c.estado")
    List<Object[]> countClasesByEstado();

    /**
     * Contar clases activas
     */
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.activo = true")
    Long countClasesActivas();

    /**
     * Contar clases inactivas
     */
    @Query("SELECT COUNT(c) FROM Clase c WHERE c.activo = false")
    Long countClasesInactivas();

    /**
     * Contar clases por grupo
     */
    @Query("SELECT c.grupo.nombre, COUNT(c) FROM Clase c GROUP BY c.grupo.nombre")
    List<Object[]> countClasesByGrupo();

    /**
     * Contar clases activas por grupo
     */
    @Query("SELECT c.grupo.nombre, COUNT(c) FROM Clase c WHERE c.activo = true GROUP BY c.grupo.nombre")
    List<Object[]> countClasesActivasByGrupo();

    /**
     * Contar clases por categoría (a través de grupo)
     */
    @Query("SELECT c.grupo.cate.nombre, COUNT(c) FROM Clase c GROUP BY c.grupo.cate.nombre")
    List<Object[]> countClasesByCategoria();

    /**
     * Contar clases por sucursal (a través de grupo)
     */
    @Query("SELECT c.grupo.sucursal.nombre, COUNT(c) FROM Clase c GROUP BY c.grupo.sucursal.nombre")
    List<Object[]> countClasesBySucursal();

    /**
     * Contar clases por mes
     */
    @Query("SELECT EXTRACT(YEAR FROM c.fechClase) AS anio, " +
            "EXTRACT(MONTH FROM c.fechClase) AS mes, " +
            "COUNT(c) AS total " +
            "FROM Clase c " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countClasesByMes();

    /**
     * Contar clases por día de la semana
     */
    @Query("SELECT EXTRACT(DOW FROM c.fechClase) AS diaSemana, COUNT(c) " +
            "FROM Clase c " +
            "GROUP BY diaSemana " +
            "ORDER BY diaSemana")
    List<Object[]> countClasesByDiaSemana();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si una clase está activa
     */
    @Query("SELECT c.activo FROM Clase c WHERE c.id = :id")
    Boolean isClaseActiva(@Param("id") Long id);

    /**
     * Verificar si un grupo tiene clases asociadas
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Clase c WHERE c.grupo.id = :grupoId")
    Boolean hasClasesAsociadas(@Param("grupoId") Long grupoId);

    /**
     * Verificar si existe una clase en el mismo horario para un grupo
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Clase c " +
            "WHERE c.grupo.id = :grupoId " +
            "AND c.fechClase = :fecha " +
            "AND c.activo = true " +
            "AND ((c.horaInicio < :horaFin AND c.horaFin > :horaInicio)) " +
            "AND c.id != :id")
    Boolean existsClaseEnHorario(@Param("grupoId") Long grupoId,
                                 @Param("fecha") LocalDate fecha,
                                 @Param("horaInicio") LocalTime horaInicio,
                                 @Param("horaFin") LocalTime horaFin,
                                 @Param("id") Long id);

    /**
     * Verificar si una clase tiene asistencias asociadas
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Asistencia a WHERE a.clase.id = :claseId")
    Boolean hasAsistenciasAsociadas(@Param("claseId") Long claseId);
}
