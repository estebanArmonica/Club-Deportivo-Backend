package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICanchaRepository extends JpaRepository<Cancha, Long> {
    // ============================================================
    // BÚSQUEDAS BÁSICAS
    // ============================================================

    /**
     * Buscar cancha por nombre (exacto)
     */
    Optional<Cancha> findByNombre(String nombre);

    /**
     * Buscar canchas por nombre que contengan un texto (case insensitive)
     */
    List<Cancha> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar canchas disponibles
     */
    List<Cancha> findByDisponibleTrue();

    /**
     * Buscar canchas no disponibles
     */
    List<Cancha> findByDisponibleFalse();

    /**
     * Verificar si existe una cancha por nombre
     */
    Boolean existsByNombre(String nombre);

    /**
     * Verificar si existe una cancha por nombre (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Cancha c " +
            "WHERE c.nombre = :nombre AND c.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // ============================================================
    // BÚSQUEDAS POR TIPO
    // ============================================================

    /**
     * Buscar canchas por tipo
     */
    List<Cancha> findByTipo(String tipo);

    /**
     * Buscar canchas disponibles por tipo
     */
    List<Cancha> findByTipoAndDisponibleTrue(String tipo);

    /**
     * Buscar canchas por tipo (case insensitive)
     */
    List<Cancha> findByTipoIgnoreCase(String tipo);

    /**
     * Buscar tipos de cancha disponibles (para selects/distinct)
     */
    @Query("SELECT DISTINCT c.tipo FROM Cancha c WHERE c.disponible = true")
    List<String> findDistinctTiposByDisponibleTrue();

    // ============================================================
    // BÚSQUEDAS POR CAPACIDAD
    // ============================================================

    /**
     * Buscar canchas por capacidad exacta
     */
    List<Cancha> findByCapacidad(int capacidad);

    /**
     * Buscar canchas con capacidad mayor a un valor
     */
    List<Cancha> findByCapacidadGreaterThan(int capacidad);

    /**
     * Buscar canchas con capacidad menor a un valor
     */
    List<Cancha> findByCapacidadLessThan(int capacidad);

    /**
     * Buscar canchas con capacidad entre dos valores
     */
    List<Cancha> findByCapacidadBetween(int capacidadMin, int capacidadMax);

    /**
     * Buscar canchas disponibles con capacidad mayor a un valor
     */
    List<Cancha> findByDisponibleTrueAndCapacidadGreaterThan(int capacidad);

    // ============================================================
    // BÚSQUEDAS POR SUCURSAL
    // ============================================================

    /**
     * Buscar canchas por ID de sucursal
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId")
    List<Cancha> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar canchas disponibles por ID de sucursal
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.disponible = true")
    List<Cancha> findDisponiblesBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar canchas por nombre de sucursal
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.nombre = :nombreSucursal")
    List<Cancha> findBySucursalNombre(@Param("nombreSucursal") String nombreSucursal);

    /**
     * Buscar canchas disponibles por nombre de sucursal
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.nombre = :nombreSucursal AND c.disponible = true")
    List<Cancha> findDisponiblesBySucursalNombre(@Param("nombreSucursal") String nombreSucursal);

    // ============================================================
    // BÚSQUEDAS COMBINADAS (SUCURSAL + TIPO + CAPACIDAD)
    // ============================================================

    /**
     * Buscar canchas por sucursal y tipo
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.tipo = :tipo")
    List<Cancha> findBySucursalIdAndTipo(@Param("sucursalId") Long sucursalId,
                                         @Param("tipo") String tipo);

    /**
     * Buscar canchas disponibles por sucursal y tipo
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.tipo = :tipo AND c.disponible = true")
    List<Cancha> findDisponiblesBySucursalIdAndTipo(@Param("sucursalId") Long sucursalId,
                                                    @Param("tipo") String tipo);

    /**
     * Buscar canchas por sucursal y capacidad mínima
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.capacidad >= :capacidadMin")
    List<Cancha> findBySucursalIdAndCapacidadMin(@Param("sucursalId") Long sucursalId,
                                                 @Param("capacidadMin") int capacidadMin);

    /**
     * Buscar canchas disponibles por sucursal y capacidad mínima
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.capacidad >= :capacidadMin AND c.disponible = true")
    List<Cancha> findDisponiblesBySucursalIdAndCapacidadMin(@Param("sucursalId") Long sucursalId,
                                                            @Param("capacidadMin") int capacidadMin);

    // ============================================================
    // BÚSQUEDAS ORDENADAS
    // ============================================================

    /**
     * Buscar todas las canchas ordenadas por nombre
     */
    List<Cancha> findAllByOrderByNombreAsc();

    /**
     * Buscar canchas disponibles ordenadas por nombre
     */
    List<Cancha> findByDisponibleTrueOrderByNombreAsc();

    /**
     * Buscar canchas por sucursal ordenadas por nombre
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId ORDER BY c.nombre ASC")
    List<Cancha> findBySucursalIdOrderByNombreAsc(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar canchas disponibles por sucursal ordenadas por nombre
     */
    @Query("SELECT c FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.disponible = true ORDER BY c.nombre ASC")
    List<Cancha> findDisponiblesBySucursalIdOrderByNombreAsc(@Param("sucursalId") Long sucursalId);

    // ============================================================
    // CONSULTAS CON FETCH (para evitar N+1)
    // ============================================================

    /**
     * Buscar cancha por ID con su sucursal (evita N+1)
     */
    @Query("SELECT c FROM Cancha c LEFT JOIN FETCH c.sucursal WHERE c.id = :id")
    Optional<Cancha> findByIdWithSucursal(@Param("id") Long id);

    /**
     * Buscar todas las canchas con su sucursal
     */
    @Query("SELECT c FROM Cancha c LEFT JOIN FETCH c.sucursal")
    List<Cancha> findAllWithSucursal();

    /**
     * Buscar canchas disponibles con su sucursal
     */
    @Query("SELECT c FROM Cancha c LEFT JOIN FETCH c.sucursal WHERE c.disponible = true")
    List<Cancha> findAllDisponiblesWithSucursal();

    /**
     * Buscar canchas por sucursal con su sucursal
     */
    @Query("SELECT c FROM Cancha c LEFT JOIN FETCH c.sucursal WHERE c.sucursal.id = :sucursalId")
    List<Cancha> findBySucursalIdWithSucursal(@Param("sucursalId") Long sucursalId);

    // ============================================================
    // CONSULTAS ESTADÍSTICAS
    // ============================================================

    /**
     * Contar canchas disponibles
     */
    @Query("SELECT COUNT(c) FROM Cancha c WHERE c.disponible = true")
    Long countCanchasDisponibles();

    /**
     * Contar canchas no disponibles
     */
    @Query("SELECT COUNT(c) FROM Cancha c WHERE c.disponible = false")
    Long countCanchasNoDisponibles();

    /**
     * Contar canchas por tipo
     */
    @Query("SELECT c.tipo, COUNT(c) FROM Cancha c GROUP BY c.tipo")
    List<Object[]> countCanchasByTipo();

    /**
     * Contar canchas disponibles por tipo
     */
    @Query("SELECT c.tipo, COUNT(c) FROM Cancha c WHERE c.disponible = true GROUP BY c.tipo")
    List<Object[]> countCanchasDisponiblesByTipo();

    /**
     * Contar canchas por sucursal
     */
    @Query("SELECT c.sucursal.nombre, COUNT(c) FROM Cancha c GROUP BY c.sucursal.nombre")
    List<Object[]> countCanchasBySucursal();

    /**
     * Contar canchas disponibles por sucursal
     */
    @Query("SELECT c.sucursal.nombre, COUNT(c) FROM Cancha c WHERE c.disponible = true GROUP BY c.sucursal.nombre")
    List<Object[]> countCanchasDisponiblesBySucursal();

    /**
     * Calcular la capacidad promedio de las canchas disponibles
     */
    @Query("SELECT AVG(c.capacidad) FROM Cancha c WHERE c.disponible = true")
    Double avgCapacidadCanchasDisponibles();

    /**
     * Obtener la capacidad total de todas las canchas disponibles
     */
    @Query("SELECT SUM(c.capacidad) FROM Cancha c WHERE c.disponible = true")
    Integer sumCapacidadTotalCanchasDisponibles();

    // ============================================================
    // CONSULTAS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener solo ID y nombre de canchas disponibles (para combos/selects)
     */
    @Query("SELECT c.id, c.nombre FROM Cancha c WHERE c.disponible = true ORDER BY c.nombre ASC")
    List<Object[]> findIdAndNombreByDisponibleTrue();

    /**
     * Obtener solo ID y nombre de canchas disponibles por sucursal
     */
    @Query("SELECT c.id, c.nombre FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.disponible = true ORDER BY c.nombre ASC")
    List<Object[]> findIdAndNombreBySucursalIdAndDisponibleTrue(@Param("sucursalId") Long sucursalId);

    /**
     * Obtener solo ID, nombre y tipo de canchas disponibles por sucursal
     */
    @Query("SELECT c.id, c.nombre, c.tipo FROM Cancha c WHERE c.sucursal.id = :sucursalId AND c.disponible = true ORDER BY c.nombre ASC")
    List<Object[]> findIdNombreTipoBySucursalIdAndDisponibleTrue(@Param("sucursalId") Long sucursalId);

    // ============================================================
    // CONSULTAS DE VALIDACIÓN
    // ============================================================

    /**
     * Verificar si una cancha está disponible
     */
    @Query("SELECT c.disponible FROM Cancha c WHERE c.id = :id")
    Boolean isCanchaDisponible(@Param("id") Long id);

    /**
     * Verificar si una cancha tiene reservas asociadas
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reserva r WHERE r.cancha.id = :canchaId")
    Boolean hasReservasAsociadas(@Param("canchaId") Long canchaId);

    /**
     * Verificar si existe una cancha con el mismo nombre en la misma sucursal
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Cancha c " +
            "WHERE c.nombre = :nombre AND c.sucursal.id = :sucursalId AND c.id != :id")
    Boolean existsByNombreAndSucursalIdAndIdNot(@Param("nombre") String nombre,
                                                @Param("sucursalId") Long sucursalId,
                                                @Param("id") Long id);
}