package com.clubdeportivo.repositories;

import com.clubdeportivo.models.ProfesorGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProfesorGrupoRepository extends JpaRepository<ProfesorGrupo, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar registros activos
     */
    List<ProfesorGrupo> findByActivoTrue();

    /**
     * Buscar registros inactivos
     */
    List<ProfesorGrupo> findByActivoFalse();

    /**
     * Buscar registros por fecha de creación
     */
    List<ProfesorGrupo> findByFechCreacion(LocalDate fechCreacion);

    /**
     * Buscar registros por rango de fechas de creación
     */
    List<ProfesorGrupo> findByFechCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar registros con fecha de creación después de
     */
    List<ProfesorGrupo> findByFechCreacionAfter(LocalDate fecha);

    /**
     * Buscar registros con fecha de creación antes de
     */
    List<ProfesorGrupo> findByFechCreacionBefore(LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS POR USUARIO (PROFESOR)
    // ============================================================

    /**
     * Buscar registros por ID de usuario (profesor)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.id = :usuarioId")
    List<ProfesorGrupo> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar registros activos por ID de usuario (profesor)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.id = :usuarioId AND p.activo = true")
    List<ProfesorGrupo> findActivosByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar registros por email de usuario (profesor)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.email = :email")
    List<ProfesorGrupo> findByUsuarioEmail(@Param("email") String email);

    /**
     * Buscar registros activos por email de usuario (profesor)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.email = :email AND p.activo = true")
    List<ProfesorGrupo> findActivosByUsuarioEmail(@Param("email") String email);

    /**
     * Buscar registros por nombre de usuario (profesor)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.nombre = :nombre")
    List<ProfesorGrupo> findByUsuarioNombre(@Param("nombre") String nombre);

    /**
     * Buscar registros por apellido de usuario (profesor)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.apellido = :apellido")
    List<ProfesorGrupo> findByUsuarioApellido(@Param("apellido") String apellido);

    // ============================================================
    // BÚSQUEDAS POR GRUPO
    // ============================================================

    /**
     * Buscar registros por ID de grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.id = :grupoId")
    List<ProfesorGrupo> findByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar registros activos por ID de grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.id = :grupoId AND p.activo = true")
    List<ProfesorGrupo> findActivosByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar registros por nombre de grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.nombre = :nombreGrupo")
    List<ProfesorGrupo> findByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    /**
     * Buscar registros activos por nombre de grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.nombre = :nombreGrupo AND p.activo = true")
    List<ProfesorGrupo> findActivosByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (USUARIO + GRUPO)
    // ============================================================

    /**
     * Buscar registros por usuario y grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.id = :usuarioId AND p.grupo.id = :grupoId")
    List<ProfesorGrupo> findByUsuarioIdAndGrupoId(@Param("usuarioId") Long usuarioId,
                                                  @Param("grupoId") Long grupoId);

    /**
     * Buscar registros activos por usuario y grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.id = :usuarioId AND p.grupo.id = :grupoId AND p.activo = true")
    List<ProfesorGrupo> findActivosByUsuarioIdAndGrupoId(@Param("usuarioId") Long usuarioId,
                                                         @Param("grupoId") Long grupoId);

    // ============================================================
    // BÚSQUEDAS POR CATEGORÍA (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar registros por ID de categoría (a través de grupo)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.cate.id = :categoriaId")
    List<ProfesorGrupo> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar registros activos por ID de categoría (a través de grupo)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.cate.id = :categoriaId AND p.activo = true")
    List<ProfesorGrupo> findActivosByCategoriaId(@Param("categoriaId") Long categoriaId);

    // ============================================================
    // BÚSQUEDAS POR SUCURSAL (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar registros por ID de sucursal (a través de grupo)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.sucursal.id = :sucursalId")
    List<ProfesorGrupo> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar registros activos por ID de sucursal (a través de grupo)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.sucursal.id = :sucursalId AND p.activo = true")
    List<ProfesorGrupo> findActivosBySucursalId(@Param("sucursalId") Long sucursalId);

    // ============================================================
    // BÚSQUEDAS POR CLUB (A TRAVÉS DE GRUPO -> SUCURSAL)
    // ============================================================

    /**
     * Buscar registros por ID de club (a través de grupo -> sucursal)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.sucursal.club.id = :clubId")
    List<ProfesorGrupo> findByClubId(@Param("clubId") Long clubId);

    /**
     * Buscar registros activos por ID de club (a través de grupo -> sucursal)
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.sucursal.club.id = :clubId AND p.activo = true")
    List<ProfesorGrupo> findActivosByClubId(@Param("clubId") Long clubId);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todos los registros ordenados por fecha de creación (más recientes primero)
     */
    List<ProfesorGrupo> findAllByOrderByFechCreacionDesc();

    /**
     * Buscar registros por grupo ordenados por fecha de creación
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.grupo.id = :grupoId ORDER BY p.fechCreacion DESC")
    List<ProfesorGrupo> findByGrupoIdOrderByFechCreacionDesc(@Param("grupoId") Long grupoId);

    /**
     * Buscar registros por usuario ordenados por fecha de creación
     */
    @Query("SELECT p FROM ProfesorGrupo p WHERE p.user.id = :usuarioId ORDER BY p.fechCreacion DESC")
    List<ProfesorGrupo> findByUsuarioIdOrderByFechCreacionDesc(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar registros activos ordenados por fecha de creación
     */
    List<ProfesorGrupo> findByActivoTrueOrderByFechCreacionDesc();

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar registro por ID con su usuario y grupo (evita N+1)
     */
    @Query("SELECT p FROM ProfesorGrupo p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.grupo WHERE p.id = :id")
    Optional<ProfesorGrupo> findByIdWithUsuarioAndGrupo(@Param("id") Long id);

    /**
     * Buscar registro por ID con todas sus relaciones (usuario, grupo, categoría, sucursal)
     */
    @Query("SELECT p FROM ProfesorGrupo p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.grupo g " +
            "LEFT JOIN FETCH g.cate " +
            "LEFT JOIN FETCH g.sucursal " +
            "WHERE p.id = :id")
    Optional<ProfesorGrupo> findByIdWithAllRelations(@Param("id") Long id);

    /**
     * Buscar todos los registros con su usuario y grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.grupo")
    List<ProfesorGrupo> findAllWithUsuarioAndGrupo();

    /**
     * Buscar registros activos con su usuario y grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.grupo WHERE p.activo = true")
    List<ProfesorGrupo> findAllActivosWithUsuarioAndGrupo();

    /**
     * Buscar registros por grupo con su usuario y grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.grupo WHERE p.grupo.id = :grupoId")
    List<ProfesorGrupo> findByGrupoIdWithUsuarioAndGrupo(@Param("grupoId") Long grupoId);

    /**
     * Buscar registros por usuario con su usuario y grupo
     */
    @Query("SELECT p FROM ProfesorGrupo p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.grupo WHERE p.user.id = :usuarioId")
    List<ProfesorGrupo> findByUsuarioIdWithUsuarioAndGrupo(@Param("usuarioId") Long usuarioId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar registros activos
     */
    @Query("SELECT COUNT(p) FROM ProfesorGrupo p WHERE p.activo = true")
    Long countRegistrosActivos();

    /**
     * Contar registros inactivos
     */
    @Query("SELECT COUNT(p) FROM ProfesorGrupo p WHERE p.activo = false")
    Long countRegistrosInactivos();

    /**
     * Contar registros por grupo
     */
    @Query("SELECT p.grupo.nombre, COUNT(p) FROM ProfesorGrupo p GROUP BY p.grupo.nombre")
    List<Object[]> countRegistrosByGrupo();

    /**
     * Contar registros activos por grupo
     */
    @Query("SELECT p.grupo.nombre, COUNT(p) FROM ProfesorGrupo p WHERE p.activo = true GROUP BY p.grupo.nombre")
    List<Object[]> countRegistrosActivosByGrupo();

    /**
     * Contar registros por usuario (profesor)
     */
    @Query("SELECT CONCAT(p.user.nombre, ' ', p.user.apellido), COUNT(p) FROM ProfesorGrupo p GROUP BY p.user.id, p.user.nombre, p.user.apellido")
    List<Object[]> countRegistrosByUsuario();

    /**
     * Contar registros activos por usuario (profesor)
     */
    @Query("SELECT CONCAT(p.user.nombre, ' ', p.user.apellido), COUNT(p) FROM ProfesorGrupo p WHERE p.activo = true GROUP BY p.user.id, p.user.nombre, p.user.apellido")
    List<Object[]> countRegistrosActivosByUsuario();

    /**
     * Contar registros por categoría (a través de grupo)
     */
    @Query("SELECT p.grupo.cate.nombre, COUNT(p) FROM ProfesorGrupo p GROUP BY p.grupo.cate.nombre")
    List<Object[]> countRegistrosByCategoria();

    /**
     * Contar registros por sucursal (a través de grupo)
     */
    @Query("SELECT p.grupo.sucursal.nombre, COUNT(p) FROM ProfesorGrupo p GROUP BY p.grupo.sucursal.nombre")
    List<Object[]> countRegistrosBySucursal();

    /**
     * Contar registros por mes de creación
     */
    @Query("SELECT EXTRACT(YEAR FROM p.fechCreacion) AS anio, " +
            "EXTRACT(MONTH FROM p.fechCreacion) AS mes, " +
            "COUNT(p) AS total " +
            "FROM ProfesorGrupo p " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countRegistrosByMes();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si un registro está activo
     */
    @Query("SELECT p.activo FROM ProfesorGrupo p WHERE p.id = :id")
    Boolean isRegistroActivo(@Param("id") Long id);

    /**
     * Verificar si un usuario ya está asignado a un grupo
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM ProfesorGrupo p " +
            "WHERE p.user.id = :usuarioId " +
            "AND p.grupo.id = :grupoId " +
            "AND p.activo = true " +
            "AND p.id != :id")
    Boolean existsUsuarioAsignadoGrupo(@Param("usuarioId") Long usuarioId,
                                       @Param("grupoId") Long grupoId,
                                       @Param("id") Long id);

    /**
     * Verificar si un grupo tiene profesores activos
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM ProfesorGrupo p " +
            "WHERE p.grupo.id = :grupoId AND p.activo = true")
    Boolean hasProfesoresActivos(@Param("grupoId") Long grupoId);

    /**
     * Verificar si un usuario tiene grupos asignados activos
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM ProfesorGrupo p " +
            "WHERE p.user.id = :usuarioId AND p.activo = true")
    Boolean hasGruposAsignadosActivos(@Param("usuarioId") Long usuarioId);
}