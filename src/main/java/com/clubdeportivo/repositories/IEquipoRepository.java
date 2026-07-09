package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEquipoRepository extends JpaRepository<Equipo, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar equipo por nombre (exacto)
     */
    Optional<Equipo> findByNombre(String nombre);

    /**
     * Buscar equipos por nombre que contengan un texto (case insensitive)
     */
    List<Equipo> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar equipos activos
     */
    List<Equipo> findByActivoTrue();

    /**
     * Buscar equipos inactivos
     */
    List<Equipo> findByActivoFalse();

    /**
     * Verificar si existe un equipo por nombre
     */
    Boolean existsByNombre(String nombre);

    /**
     * Verificar si existe un equipo por nombre (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Equipo e " +
            "WHERE e.nombre = :nombre AND e.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // ============================================================
    // BÚSQUEDAS POR FECHA DE CREACIÓN
    // ============================================================

    /**
     * Buscar equipos creados en una fecha específica
     */
    List<Equipo> findByFechCreacion(LocalDate fecha);

    /**
     * Buscar equipos creados después de una fecha
     */
    List<Equipo> findByFechCreacionAfter(LocalDate fecha);

    /**
     * Buscar equipos creados antes de una fecha
     */
    List<Equipo> findByFechCreacionBefore(LocalDate fecha);

    /**
     * Buscar equipos creados entre dos fechas
     */
    List<Equipo> findByFechCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // ============================================================
    // BÚSQUEDAS POR USUARIO (CAPITÁN)
    // ============================================================

    /**
     * Buscar equipos por ID de usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.id = :usuarioId")
    List<Equipo> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar equipos activos por ID de usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.id = :usuarioId AND e.activo = true")
    List<Equipo> findActivosByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Buscar equipos por email de usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.email = :email")
    List<Equipo> findByUsuarioEmail(@Param("email") String email);

    /**
     * Buscar equipos activos por email de usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.email = :email AND e.activo = true")
    List<Equipo> findActivosByUsuarioEmail(@Param("email") String email);

    /**
     * Buscar equipos por nombre de usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.nombre = :nombre")
    List<Equipo> findByUsuarioNombre(@Param("nombre") String nombre);

    /**
     * Buscar equipos por apellido de usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.apellido = :apellido")
    List<Equipo> findByUsuarioApellido(@Param("apellido") String apellido);

    // ============================================================
    // BÚSQUEDAS POR GRUPO
    // ============================================================

    /**
     * Buscar equipos por ID de grupo
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.id = :grupoId")
    List<Equipo> findByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar equipos activos por ID de grupo
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.id = :grupoId AND e.activo = true")
    List<Equipo> findActivosByGrupoId(@Param("grupoId") Long grupoId);

    /**
     * Buscar equipos por nombre de grupo
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.nombre = :nombreGrupo")
    List<Equipo> findByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    /**
     * Buscar equipos activos por nombre de grupo
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.nombre = :nombreGrupo AND e.activo = true")
    List<Equipo> findActivosByGrupoNombre(@Param("nombreGrupo") String nombreGrupo);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (GRUPO + USUARIO)
    // ============================================================

    /**
     * Buscar equipos por grupo y usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.id = :grupoId AND e.user.id = :usuarioId")
    List<Equipo> findByGrupoIdAndUsuarioId(@Param("grupoId") Long grupoId,
                                           @Param("usuarioId") Long usuarioId);

    /**
     * Buscar equipos activos por grupo y usuario (capitán)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.id = :grupoId AND e.user.id = :usuarioId AND e.activo = true")
    List<Equipo> findActivosByGrupoIdAndUsuarioId(@Param("grupoId") Long grupoId,
                                                  @Param("usuarioId") Long usuarioId);

    // ============================================================
    // BÚSQUEDAS POR CATEGORÍA (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar equipos por ID de categoría (a través de grupo)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.cate.id = :categoriaId")
    List<Equipo> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar equipos activos por ID de categoría (a través de grupo)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.cate.id = :categoriaId AND e.activo = true")
    List<Equipo> findActivosByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar equipos por nombre de categoría (a través de grupo)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.cate.nombre = :nombreCategoria")
    List<Equipo> findByCategoriaNombre(@Param("nombreCategoria") String nombreCategoria);

    // ============================================================
    // BÚSQUEDAS POR SUCURSAL (A TRAVÉS DE GRUPO)
    // ============================================================

    /**
     * Buscar equipos por ID de sucursal (a través de grupo)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.sucursal.id = :sucursalId")
    List<Equipo> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar equipos activos por ID de sucursal (a través de grupo)
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.sucursal.id = :sucursalId AND e.activo = true")
    List<Equipo> findActivosBySucursalId(@Param("sucursalId") Long sucursalId);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todos los equipos ordenados por nombre
     */
    List<Equipo> findAllByOrderByNombreAsc();

    /**
     * Buscar equipos activos ordenados por nombre
     */
    List<Equipo> findByActivoTrueOrderByNombreAsc();

    /**
     * Buscar equipos por grupo ordenados por nombre
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.id = :grupoId ORDER BY e.nombre ASC")
    List<Equipo> findByGrupoIdOrderByNombreAsc(@Param("grupoId") Long grupoId);

    /**
     * Buscar equipos activos por grupo ordenados por nombre
     */
    @Query("SELECT e FROM Equipo e WHERE e.grupo.id = :grupoId AND e.activo = true ORDER BY e.nombre ASC")
    List<Equipo> findActivosByGrupoIdOrderByNombreAsc(@Param("grupoId") Long grupoId);

    /**
     * Buscar equipos por usuario (capitán) ordenados por nombre
     */
    @Query("SELECT e FROM Equipo e WHERE e.user.id = :usuarioId ORDER BY e.nombre ASC")
    List<Equipo> findByUsuarioIdOrderByNombreAsc(@Param("usuarioId") Long usuarioId);

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar equipo por ID con su usuario y grupo (evita N+1)
     */
    @Query("SELECT e FROM Equipo e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.grupo WHERE e.id = :id")
    Optional<Equipo> findByIdWithUsuarioAndGrupo(@Param("id") Long id);

    /**
     * Buscar equipo por ID con su usuario, grupo, categoría y sucursal (evita N+1)
     */
    @Query("SELECT e FROM Equipo e " +
            "LEFT JOIN FETCH e.user " +
            "LEFT JOIN FETCH e.grupo g " +
            "LEFT JOIN FETCH g.cate " +
            "LEFT JOIN FETCH g.sucursal " +
            "WHERE e.id = :id")
    Optional<Equipo> findByIdWithAllRelations(@Param("id") Long id);

    /**
     * Buscar todos los equipos con su usuario y grupo
     */
    @Query("SELECT e FROM Equipo e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.grupo")
    List<Equipo> findAllWithUsuarioAndGrupo();

    /**
     * Buscar equipos activos con su usuario y grupo
     */
    @Query("SELECT e FROM Equipo e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.grupo WHERE e.activo = true")
    List<Equipo> findAllActivosWithUsuarioAndGrupo();

    /**
     * Buscar equipos por grupo con su usuario y grupo
     */
    @Query("SELECT e FROM Equipo e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.grupo WHERE e.grupo.id = :grupoId")
    List<Equipo> findByGrupoIdWithUsuarioAndGrupo(@Param("grupoId") Long grupoId);

    /**
     * Buscar equipos por usuario con su usuario y grupo
     */
    @Query("SELECT e FROM Equipo e LEFT JOIN FETCH e.user LEFT JOIN FETCH e.grupo WHERE e.user.id = :usuarioId")
    List<Equipo> findByUsuarioIdWithUsuarioAndGrupo(@Param("usuarioId") Long usuarioId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar equipos activos
     */
    @Query("SELECT COUNT(e) FROM Equipo e WHERE e.activo = true")
    Long countEquiposActivos();

    /**
     * Contar equipos inactivos
     */
    @Query("SELECT COUNT(e) FROM Equipo e WHERE e.activo = false")
    Long countEquiposInactivos();

    /**
     * Contar equipos por grupo
     */
    @Query("SELECT e.grupo.nombre, COUNT(e) FROM Equipo e GROUP BY e.grupo.nombre")
    List<Object[]> countEquiposByGrupo();

    /**
     * Contar equipos activos por grupo
     */
    @Query("SELECT e.grupo.nombre, COUNT(e) FROM Equipo e WHERE e.activo = true GROUP BY e.grupo.nombre")
    List<Object[]> countEquiposActivosByGrupo();

    /**
     * Contar equipos por usuario (capitán)
     */
    @Query("SELECT CONCAT(e.user.nombre, ' ', e.user.apellido), COUNT(e) FROM Equipo e GROUP BY e.user.id, e.user.nombre, e.user.apellido")
    List<Object[]> countEquiposByUsuario();

    /**
     * Contar equipos por categoría (a través de grupo)
     */
    @Query("SELECT e.grupo.cate.nombre, COUNT(e) FROM Equipo e GROUP BY e.grupo.cate.nombre")
    List<Object[]> countEquiposByCategoria();

    /**
     * Contar equipos por sucursal (a través de grupo)
     */
    @Query("SELECT e.grupo.sucursal.nombre, COUNT(e) FROM Equipo e GROUP BY e.grupo.sucursal.nombre")
    List<Object[]> countEquiposBySucursal();

    // ============================================================
    // CONSULTAS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener solo ID y nombre de equipos activos (para combos/selects)
     */
    @Query("SELECT e.id, e.nombre FROM Equipo e WHERE e.activo = true ORDER BY e.nombre ASC")
    List<Object[]> findIdAndNombreByActivoTrue();

    /**
     * Obtener solo ID y nombre de equipos activos por grupo
     */
    @Query("SELECT e.id, e.nombre FROM Equipo e WHERE e.grupo.id = :grupoId AND e.activo = true ORDER BY e.nombre ASC")
    List<Object[]> findIdAndNombreByGrupoIdAndActivoTrue(@Param("grupoId") Long grupoId);

    /**
     * Obtener solo ID y nombre de equipos activos por usuario (capitán)
     */
    @Query("SELECT e.id, e.nombre FROM Equipo e WHERE e.user.id = :usuarioId AND e.activo = true ORDER BY e.nombre ASC")
    List<Object[]> findIdAndNombreByUsuarioIdAndActivoTrue(@Param("usuarioId") Long usuarioId);

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si un equipo está activo
     */
    @Query("SELECT e.activo FROM Equipo e WHERE e.id = :id")
    Boolean isEquipoActivo(@Param("id") Long id);

    /**
     * Verificar si un equipo tiene reservas asociadas
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reserva r WHERE r.equipo.id = :equipoId")
    Boolean hasReservasAsociadas(@Param("equipoId") Long equipoId);

    /**
     * Verificar si un equipo tiene un capitán (usuario) asignado
     */
    @Query("SELECT CASE WHEN e.user IS NOT NULL THEN true ELSE false END " +
            "FROM Equipo e WHERE e.id = :id")
    Boolean hasCapitanAsignado(@Param("id") Long id);

    /**
     * Verificar si un usuario ya es capitán de un equipo activo en el mismo grupo
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Equipo e " +
            "WHERE e.user.id = :usuarioId " +
            "AND e.grupo.id = :grupoId " +
            "AND e.activo = true " +
            "AND e.id != :id")
    Boolean existsUsuarioCapitanEnGrupo(@Param("usuarioId") Long usuarioId,
                                        @Param("grupoId") Long grupoId,
                                        @Param("id") Long id);
}