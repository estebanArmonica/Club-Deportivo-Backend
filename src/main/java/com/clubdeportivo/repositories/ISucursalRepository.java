package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISucursalRepository extends JpaRepository<Sucursal, Long> {
    /**
     * Buscar sucursal por nombre (exacto)
     */
    Optional<Sucursal> findByNombre(String nombre);

    /**
     * Buscar sucursales por nombre que contengan un texto (case insensitive)
     */
    List<Sucursal> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar sucursales activas
     */
    List<Sucursal> findByActivoTrue();

    /**
     * Buscar sucursales inactivas
     */
    List<Sucursal> findByActivoFalse();

    /**
     * Verificar si existe una sucursal por nombre
     */
    Boolean existsByNombre(String nombre);

    /**
     * Verificar si existe una sucursal por nombre (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Sucursal s " +
            "WHERE s.nombre = :nombre AND s.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // ============================================================
    // BÚSQUEDAS POR CLUB
    // ============================================================

    /**
     * Buscar sucursales por ID de club
     */
    @Query("SELECT s FROM Sucursal s WHERE s.club.id = :clubId")
    List<Sucursal> findByClubId(@Param("clubId") Long clubId);

    /**
     * Buscar sucursales activas por ID de club
     */
    @Query("SELECT s FROM Sucursal s WHERE s.club.id = :clubId AND s.activo = true")
    List<Sucursal> findActivasByClubId(@Param("clubId") Long clubId);

    /**
     * Buscar sucursales por nombre de club
     */
    @Query("SELECT s FROM Sucursal s WHERE s.club.nombre = :nombreClub")
    List<Sucursal> findByClubNombre(@Param("nombreClub") String nombreClub);

    /**
     * Buscar sucursales activas por nombre de club
     */
    @Query("SELECT s FROM Sucursal s WHERE s.club.nombre = :nombreClub AND s.activo = true")
    List<Sucursal> findActivasByClubNombre(@Param("nombreClub") String nombreClub);

    // ============================================================
    // BÚSQUEDAS POR DIRECCIÓN Y TELÉFONO
    // ============================================================

    /**
     * Buscar sucursales por dirección (búsqueda parcial)
     */
    List<Sucursal> findByDireccionContainingIgnoreCase(String direccion);

    /**
     * Buscar sucursales por teléfono
     */
    Optional<Sucursal> findByTelefono(String telefono);

    /**
     * Buscar sucursales por teléfono que contenga un valor
     */
    List<Sucursal> findByTelefonoContaining(String telefono);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todas las sucursales ordenadas por nombre
     */
    List<Sucursal> findAllByOrderByNombreAsc();

    /**
     * Buscar sucursales activas ordenadas por nombre
     */
    List<Sucursal> findByActivoTrueOrderByNombreAsc();

    /**
     * Buscar sucursales por club ordenadas por nombre
     */
    @Query("SELECT s FROM Sucursal s WHERE s.club.id = :clubId ORDER BY s.nombre ASC")
    List<Sucursal> findByClubIdOrderByNombreAsc(@Param("clubId") Long clubId);

    /**
     * Buscar sucursales activas por club ordenadas por nombre
     */
    @Query("SELECT s FROM Sucursal s WHERE s.club.id = :clubId AND s.activo = true ORDER BY s.nombre ASC")
    List<Sucursal> findActivasByClubIdOrderByNombreAsc(@Param("clubId") Long clubId);

    /*
      * ============================================================
      * Realizamos consultas FETCH (para evitar N+1)
      * ============================================================
     */

    /**
     * Buscar sucursal por ID con su club (evita N+1)
     */
    @Query("SELECT s FROM Sucursal s LEFT JOIN FETCH s.club WHERE s.id = :id")
    Optional<Sucursal> findByIdWithClub(@Param("id") Long id);

    /**
     * Buscar todas las sucursales con su club
     */
    @Query("SELECT s FROM Sucursal s LEFT JOIN FETCH s.club")
    List<Sucursal> findAllWithClub();

    /**
     * Buscar sucursales activas con su club
     */
    @Query("SELECT s FROM Sucursal s LEFT JOIN FETCH s.club WHERE s.activo = true")
    List<Sucursal> findAllActivasWithClub();

    /**
     * Buscar sucursales por club con su club
     */
    @Query("SELECT s FROM Sucursal s LEFT JOIN FETCH s.club WHERE s.club.id = :clubId")
    List<Sucursal> findByClubIdWithClub(@Param("clubId") Long clubId);

    /*
    * ============================================================
    * Consultas de estadisticas
    * ============================================================
    * */

    /**
     * Contar sucursales activas
     */
    @Query("SELECT COUNT(s) FROM Sucursal s WHERE s.activo = true")
    Long countSucursalesActivas();

    /**
     * Contar sucursales inactivas
     */
    @Query("SELECT COUNT(s) FROM Sucursal s WHERE s.activo = false")
    Long countSucursalesInactivas();

    /**
     * Contar sucursales por club
     */
    @Query("SELECT s.club.nombre, COUNT(s) FROM Sucursal s GROUP BY s.club.nombre")
    List<Object[]> countSucursalesByClub();

    /**
     * Contar sucursales activas por club
     */
    @Query("SELECT s.club.nombre, COUNT(s) FROM Sucursal s WHERE s.activo = true GROUP BY s.club.nombre")
    List<Object[]> countSucursalesActivasByClub();

    /*
    * ============================================================
    * Realizamos consultas SELECTS/DROPDOWNS
    * ============================================================
    * */

    /**
     * Obtener solo ID y nombre de sucursales activas (para combos/selects)
     */
    @Query("SELECT s.id, s.nombre FROM Sucursal s WHERE s.activo = true ORDER BY s.nombre ASC")
    List<Object[]> findIdAndNombreByActivoTrue();

    /**
     * Obtener solo ID y nombre de sucursales activas por club
     */
    @Query("SELECT s.id, s.nombre FROM Sucursal s WHERE s.club.id = :clubId AND s.activo = true ORDER BY s.nombre ASC")
    List<Object[]> findIdAndNombreByClubIdAndActivoTrue(@Param("clubId") Long clubId);

    /**
     * ============================================================
     * Realizamos consultas de validacion
     * ============================================================
    */

    /**
     * Verificar si una sucursal está activa
     */
    @Query("SELECT s.activo FROM Sucursal s WHERE s.id = :id")
    Boolean isSucursalActiva(@Param("id") Long id);

    /**
     * Verificar si una sucursal tiene grupos asociados
     */
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " +
            "FROM Grupo g WHERE g.sucursal.id = :sucursalId")
    Boolean hasGruposAsociados(@Param("sucursalId") Long sucursalId);

    /**
     * Verificar si una sucursal tiene canchas asociadas
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Cancha c WHERE c.sucursal.id = :sucursalId")
    Boolean hasCanchasAsociadas(@Param("sucursalId") Long sucursalId);

    /**
     * Verificar si una sucursal tiene encargados asociados
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM EncargadoSucursal e WHERE e.sucursal.id = :sucursalId")
    Boolean hasEncargadosAsociados(@Param("sucursalId") Long sucursalId);
}
