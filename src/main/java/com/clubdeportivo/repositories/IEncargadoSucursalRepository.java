package com.clubdeportivo.repositories;

import com.clubdeportivo.models.EncargadoSucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEncargadoSucursalRepository extends JpaRepository<EncargadoSucursal, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar registros por turno
     */
    List<EncargadoSucursal> findByTurno(String turno);

    /**
     * Buscar registros por turno (case insensitive)
     */
    List<EncargadoSucursal> findByTurnoIgnoreCase(String turno);

    /**
     * Buscar registros activos
     */
    List<EncargadoSucursal> findByActivoTrue();

    /**
     * Buscar registros inactivos
     */
    List<EncargadoSucursal> findByActivoFalse();

    /**
     * Buscar registros por fecha de asignación
     */
    List<EncargadoSucursal> findByFechAsignacion(LocalDate fechAsignacion);

    /**
     * Buscar registros por rango de fechas de asignación
     */
    List<EncargadoSucursal> findByFechAsignacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar registros con fecha de asignación después de
     */
    List<EncargadoSucursal> findByFechAsignacionAfter(LocalDate fecha);

    /**
     * Buscar registros con fecha de asignación antes de
     */
    List<EncargadoSucursal> findByFechAsignacionBefore(LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR USUARIO (ENCARGADO)
    // ============================================================

    /**
     * Buscar registros por ID de usuario (encargado)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId")
    List<EncargadoSucursal> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar registros activos por ID de usuario (encargado)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId AND e.activo = true")
    List<EncargadoSucursal> findActivosByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar registros por email de usuario (encargado)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.email = :email")
    List<EncargadoSucursal> findByUsuarioEmail(@Param("email") String email);

    /**
     * Buscar registros activos por email de usuario (encargado)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.email = :email AND e.activo = true")
    List<EncargadoSucursal> findActivosByUsuarioEmail(@Param("email") String email);

    /**
     * Buscar registros por nombre de usuario (encargado)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.nombre = :nombre")
    List<EncargadoSucursal> findByUsuarioNombre(@Param("nombre") String nombre);

    /**
     * Buscar registros por apellido de usuario (encargado)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.apellido = :apellido")
    List<EncargadoSucursal> findByUsuarioApellido(@Param("apellido") String apellido);

    // ============================================================
    // BÚSQUEDAS POR SUCURSAL
    // ============================================================

    /**
     * Buscar registros por ID de sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.id = :sucursalId")
    List<EncargadoSucursal> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros activos por ID de sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.id = :sucursalId AND e.activo = true")
    List<EncargadoSucursal> findActivosBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros por nombre de sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.nombre = :nombreSucursal")
    List<EncargadoSucursal> findBySucursalNombre(@Param("nombreSucursal") String nombreSucursal);

    /**
     * Buscar registros activos por nombre de sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.nombre = :nombreSucursal AND e.activo = true")
    List<EncargadoSucursal> findActivosBySucursalNombre(@Param("nombreSucursal") String nombreSucursal);

    /**
     * Buscar registros por club (a través de sucursal)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.club.id = :clubId")
    List<EncargadoSucursal> findByClubId(@Param("clubId") Long clubId);

    /**
     * Buscar registros activos por club (a través de sucursal)
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.club.id = :clubId AND e.activo = true")
    List<EncargadoSucursal> findActivosByClubId(@Param("clubId") Long clubId);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (USUARIO + SUCURSAL + TURNO)
    // ============================================================

    /**
     * Buscar registros por usuario y sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId AND e.sucursal.id = :sucursalId")
    List<EncargadoSucursal> findByUsuarioIdAndSucursalId(@Param("usuarioId") Long usuarioId,
                                                         @Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros activos por usuario y sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId AND e.sucursal.id = :sucursalId AND e.activo = true")
    List<EncargadoSucursal> findActivosByUsuarioIdAndSucursalId(@Param("usuarioId") Long usuarioId,
                                                                @Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros por usuario y turno
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId AND e.turno = :turno")
    List<EncargadoSucursal> findByUsuarioIdAndTurno(@Param("usuarioId") Long usuarioId,
                                                    @Param("turno") String turno);

    /**
     * Buscar registros por sucursal y turno
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.id = :sucursalId AND e.turno = :turno")
    List<EncargadoSucursal> findBySucursalIdAndTurno(@Param("sucursalId") Long sucursalId,
                                                     @Param("turno") String turno);

    /**
     * Buscar registros activos por sucursal y turno
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.id = :sucursalId AND e.turno = :turno AND e.activo = true")
    List<EncargadoSucursal> findActivosBySucursalIdAndTurno(@Param("sucursalId") Long sucursalId,
                                                            @Param("turno") String turno);

    /**
     * Buscar registros por usuario, sucursal y turno
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId AND e.sucursal.id = :sucursalId AND e.turno = :turno")
    List<EncargadoSucursal> findByUsuarioIdAndSucursalIdAndTurno(@Param("usuarioId") Long usuarioId,
                                                                 @Param("sucursalId") Long sucursalId,
                                                                 @Param("turno") String turno);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todos los registros ordenados por fecha de asignación (más recientes primero)
     */
    List<EncargadoSucursal> findAllByOrderByFechAsignacionDesc();

    /**
     * Buscar registros por sucursal ordenados por fecha de asignación
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.sucursal.id = :sucursalId ORDER BY e.fechAsignacion DESC")
    List<EncargadoSucursal> findBySucursalIdOrderByFechAsignacionDesc(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros por usuario ordenados por fecha de asignación
     */
    @Query("SELECT e FROM EncargadoSucursal e WHERE e.user.id = :usuarioId ORDER BY e.fechAsignacion DESC")
    List<EncargadoSucursal> findByUsuarioIdOrderByFechAsignacionDesc(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar registros activos ordenados por fecha de asignación
     */
    List<EncargadoSucursal> findByActivoTrueOrderByFechAsignacionDesc();

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar registro por ID con su usuario y sucursal (evita N+1)
     */
    @Query("SELECT e FROM EncargadoSucursal e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.sucursal WHERE e.id = :id")
    Optional<EncargadoSucursal> findByIdWithUsuarioAndSucursal(@Param("id") Long id);

    /**
     * Buscar registro por ID con todas sus relaciones (usuario, sucursal, club)
     */
    @Query("SELECT e FROM EncargadoSucursal e " +
            "LEFT JOIN FETCH e.user " +
            "LEFT JOIN FETCH e.sucursal s " +
            "LEFT JOIN FETCH s.club " +
            "WHERE e.id = :id")
    Optional<EncargadoSucursal> findByIdWithAllRelations(@Param("id") Long id);

    /**
     * Buscar todos los registros con su usuario y sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.sucursal")
    List<EncargadoSucursal> findAllWithUsuarioAndSucursal();

    /**
     * Buscar registros activos con su usuario y sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.sucursal WHERE e.activo = true")
    List<EncargadoSucursal> findAllActivosWithUsuarioAndSucursal();

    /**
     * Buscar registros por sucursal con su usuario y sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.sucursal WHERE e.sucursal.id = :sucursalId")
    List<EncargadoSucursal> findBySucursalIdWithUsuarioAndSucursal(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros por usuario con su usuario y sucursal
     */
    @Query("SELECT e FROM EncargadoSucursal e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.sucursal WHERE e.user.id = :usuarioId")
    List<EncargadoSucursal> findByUsuarioIdWithUsuarioAndSucursal(@Param("usuarioId") Long usuarioId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar registros activos
     */
    @Query("SELECT COUNT(e) FROM EncargadoSucursal e WHERE e.activo = true")
    Long countRegistrosActivos();

    /**
     * Contar registros inactivos
     */
    @Query("SELECT COUNT(e) FROM EncargadoSucursal e WHERE e.activo = false")
    Long countRegistrosInactivos();

    /**
     * Contar registros por turno
     */
    @Query("SELECT e.turno, COUNT(e) FROM EncargadoSucursal e GROUP BY e.turno")
    List<Object[]> countRegistrosByTurno();

    /**
     * Contar registros activos por turno
     */
    @Query("SELECT e.turno, COUNT(e) FROM EncargadoSucursal e WHERE e.activo = true GROUP BY e.turno")
    List<Object[]> countRegistrosActivosByTurno();

    /**
     * Contar registros por sucursal
     */
    @Query("SELECT e.sucursal.nombre, COUNT(e) FROM EncargadoSucursal e GROUP BY e.sucursal.nombre")
    List<Object[]> countRegistrosBySucursal();

    /**
     * Contar registros activos por sucursal
     */
    @Query("SELECT e.sucursal.nombre, COUNT(e) FROM EncargadoSucursal e WHERE e.activo = true GROUP BY e.sucursal.nombre")
    List<Object[]> countRegistrosActivosBySucursal();

    /**
     * Contar registros por usuario (encargado)
     */
    @Query("SELECT CONCAT(e.user.nombre, ' ', e.user.apellido), COUNT(e) FROM EncargadoSucursal e GROUP BY e.user.id, e.user.nombre, e.user.apellido")
    List<Object[]> countRegistrosByUsuario();

    /**
     * Contar registros por mes de asignación
     */
    @Query("SELECT EXTRACT(YEAR FROM e.fechAsignacion) AS anio, " +
            "EXTRACT(MONTH FROM e.fechAsignacion) AS mes, " +
            "COUNT(e) AS total " +
            "FROM EncargadoSucursal e " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countRegistrosByMes();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si un registro está activo
     */
    @Query("SELECT e.activo FROM EncargadoSucursal e WHERE e.id = :id")
    Boolean isRegistroActivo(@Param("id") Long id);

    /**
     * Verificar si un usuario ya está asignado a una sucursal con un turno específico
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM EncargadoSucursal e " +
            "WHERE e.user.id = :usuarioId " +
            "AND e.sucursal.id = :sucursalId " +
            "AND e.turno = :turno " +
            "AND e.activo = true " +
            "AND e.id != :id")
    Boolean existsUsuarioAsignadoSucursalTurno(@Param("usuarioId") Long usuarioId,
                                               @Param("sucursalId") Long sucursalId,
                                               @Param("turno") String turno,
                                               @Param("id") Long id);

    /**
     * Verificar si un usuario ya está asignado a una sucursal (sin importar turno)
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM EncargadoSucursal e " +
            "WHERE e.user.id = :usuarioId " +
            "AND e.sucursal.id = :sucursalId " +
            "AND e.activo = true " +
            "AND e.id != :id")
    Boolean existsUsuarioAsignadoSucursal(@Param("usuarioId") Long usuarioId,
                                          @Param("sucursalId") Long sucursalId,
                                          @Param("id") Long id);

    /**
     * Verificar si una sucursal ya tiene un encargado activo para un turno específico
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM EncargadoSucursal e " +
            "WHERE e.sucursal.id = :sucursalId " +
            "AND e.turno = :turno " +
            "AND e.activo = true " +
            "AND e.id != :id")
    Boolean existsSucursalConEncargadoTurno(@Param("sucursalId") Long sucursalId,
                                            @Param("turno") String turno,
                                            @Param("id") Long id);
}
