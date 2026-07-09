package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClubRepository extends JpaRepository<Club, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar club por nombre (exacto)
     */
    Optional<Club> findByNombre(String nombre);

    /**
     * Buscar clubes por nombre que contengan un texto (case insensitive)
     */
    List<Club> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar clubes activos
     */
    List<Club> findByActivoTrue();

    /**
     * Buscar clubes inactivos
     */
    List<Club> findByActivoFalse();

    /**
     * Verificar si existe un club por nombre
     */
    Boolean existsByNombre(String nombre);

    /**
     * Verificar si existe un club por nombre (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Club c " +
            "WHERE c.nombre = :nombre AND c.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // ============================================================
    // BÚSQUEDAS POR CAMPOS ÚNICOS
    // ============================================================

    /**
     * Buscar club por CUIT
     */
    Optional<Club> findByCuit(String cuit);

    /**
     * Buscar club por email
     */
    Optional<Club> findByEmail(String email);

    /**
     * Buscar club por teléfono
     */
    Optional<Club> findByTelefono(String telefono);

    /**
     * Verificar si existe un club por CUIT
     */
    Boolean existsByCuit(String cuit);

    /**
     * Verificar si existe un club por email
     */
    Boolean existsByEmail(String email);

    /**
     * Verificar si existe un club por CUIT (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Club c " +
            "WHERE c.cuit = :cuit AND c.id != :id")
    Boolean existsByCuitAndIdNot(@Param("cuit") String cuit, @Param("id") Long id);

    /**
     * Verificar si existe un club por email (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Club c " +
            "WHERE c.email = :email AND c.id != :id")
    Boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    // ============================================================
    // BÚSQUEDAS POR DIRECCIÓN
    // ============================================================

    /**
     * Buscar clubes por dirección (búsqueda parcial)
     */
    List<Club> findByDireccionContainingIgnoreCase(String direccion);

    // ============================================================
    // BÚSQUEDAS POR FECHA DE CREACIÓN
    // ============================================================

    /**
     * Buscar clubes creados en una fecha específica
     */
    List<Club> findByFechCreacion(LocalDate fecha);

    /**
     * Buscar clubes creados después de una fecha
     */
    List<Club> findByFechCreacionAfter(LocalDate fecha);

    /**
     * Buscar clubes creados antes de una fecha
     */
    List<Club> findByFechCreacionBefore(LocalDate fecha);

    /**
     * Buscar clubes creados entre dos fechas
     */
    List<Club> findByFechCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar clubes activos creados después de una fecha
     */
    List<Club> findByActivoTrueAndFechCreacionAfter(LocalDate fecha);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todos los clubes ordenados por nombre
     */
    List<Club> findAllByOrderByNombreAsc();

    /**
     * Buscar clubes activos ordenados por nombre
     */
    List<Club> findByActivoTrueOrderByNombreAsc();

    /**
     * Buscar clubes ordenados por fecha de creación (más recientes primero)
     */
    List<Club> findAllByOrderByFechCreacionDesc();

    /**
     * Buscar clubes activos ordenados por fecha de creación (más recientes primero)
     */
    List<Club> findByActivoTrueOrderByFechCreacionDesc();

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar clubes activos
     */
    @Query("SELECT COUNT(c) FROM Club c WHERE c.activo = true")
    Long countClubesActivos();

    /**
     * Contar clubes inactivos
     */
    @Query("SELECT COUNT(c) FROM Club c WHERE c.activo = false")
    Long countClubesInactivos();

    /**
     * Contar total de clubes
     */
    @Query("SELECT COUNT(c) FROM Club c")
    Long countTotalClubes();

    /**
     * Contar clubes creados por mes
     */
    @Query("SELECT EXTRACT(YEAR FROM c.fechCreacion) AS anio, " +
            "EXTRACT(MONTH FROM c.fechCreacion) AS mes, " +
            "COUNT(c) AS total " +
            "FROM Club c " +
            "GROUP BY anio, mes " +
            "ORDER BY anio DESC, mes DESC")
    List<Object[]> countClubesByMesCreacion();

    // ============================================================
    // CONSULTAS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener solo ID y nombre de clubes activos (para combos/selects)
     */
    @Query("SELECT c.id, c.nombre FROM Club c WHERE c.activo = true ORDER BY c.nombre ASC")
    List<Object[]> findIdAndNombreByActivoTrue();

    /**
     * Obtener solo ID, nombre y CUIT de clubes activos (para combos/selects)
     */
    @Query("SELECT c.id, c.nombre, c.cuit FROM Club c WHERE c.activo = true ORDER BY c.nombre ASC")
    List<Object[]> findIdNombreCuitByActivoTrue();

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si un club está activo
     */
    @Query("SELECT c.activo FROM Club c WHERE c.id = :id")
    Boolean isClubActivo(@Param("id") Long id);

    /**
     * Verificar si un club tiene sucursales asociadas
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Sucursal s WHERE s.club.id = :clubId")
    Boolean hasSucursalesAsociadas(@Param("clubId") Long clubId);

    /**
     * Verificar si un club tiene usuarios asociados
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UsuarioClub u WHERE u.club.id = :clubId")
    Boolean hasUsuariosAsociados(@Param("clubId") Long clubId);
}