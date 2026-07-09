package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IGrupoRepository extends JpaRepository<Grupo, Long> {
    /*
    *  Esté es un repositorio en el cual está loa logica de negocio par5a grupo
    * con sus relaciones
    */

    // Busqueda básica

    /**
     * Buscar grupo por nombre (exacto)
     */
    Optional<Grupo> findByNombre(String nombre);

    /**
     * Buscar grupos por nombre que contengan un texto (case insensitive)
     */
    List<Grupo> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar grupos activos
     */
    List<Grupo> findByActivoTrue();

    /**
     * Buscar grupos inactivos
     */
    List<Grupo> findByActivoFalse();

    /**
     * Verificar si existe un grupo por nombre
     */
    Boolean existsByNombre(String nombre);

    /**
      * Verificar si existe un grupo por nombre (ignorando un ID específico)
     */
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " +
            "FROM Grupo g " +
            "WHERE g.nombre = :nombre AND g.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    /*
     * ===============================================================================================================
     * Realizamos busquedas por categorias
     * ===============================================================================================================
    */

    /**
     * Buscar grupos por ID de categoría
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.id = :categoriaId")
    List<Grupo> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar grupos activos por ID de categoría
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.id = :categoriaId AND g.activo = true")
    List<Grupo> findActivosByCategoriaId(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar grupos por nombre de categoría
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.nombre = :nombreCategoria")
    List<Grupo> findByCategoriaNombre(@Param("nombreCategoria") String nombreCategoria);

    /**
     * Buscar grupos activos por nombre de categoría
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.nombre = :nombreCategoria AND g.activo = true")
    List<Grupo> findActivosByCategoriaNombre(@Param("nombreCategoria") String nombreCategoria);

    /*
     * ===============================================================================================================
     * Realizamos busquedas por sucursales
     * ===============================================================================================================
    */

    /**
     * Buscar grupos por ID de sucursal
     */
    @Query("SELECT g FROM Grupo g WHERE g.sucursal.id = :sucursalId")
    List<Grupo> findBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar grupos activos por ID de sucursal
     */
    @Query("SELECT g FROM Grupo g WHERE g.sucursal.id = :sucursalId AND g.activo = true")
    List<Grupo> findActivosBySucursalId(@Param("sucursalId") Long sucursalId);

    /**
     * Buscar grupos por nombre de sucursal
     */
    @Query("SELECT g FROM Grupo g WHERE g.sucursal.nombre = :nombreSucursal")
    List<Grupo> findBySucursalNombre(@Param("nombreSucursal") String nombreSucursal);

    /**
     * Buscar grupos activos por nombre de sucursal
     */
    @Query("SELECT g FROM Grupo g WHERE g.sucursal.nombre = :nombreSucursal AND g.activo = true")
    List<Grupo> findActivosBySucursalNombre(@Param("nombreSucursal") String nombreSucursal);

    /*
    * Realizamos busquedas combinadas por categoria y sucursal
    */

    /**
     * Buscar grupos por categoría y sucursal
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.id = :categoriaId AND g.sucursal.id = :sucursalId")
    List<Grupo> findByCategoriaIdAndSucursalId(@Param("categoriaId") Long categoriaId,
                                               @Param("sucursalId") Long sucursalId);

    /**
     * Buscar grupos activos por categoría y sucursal
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.id = :categoriaId AND g.sucursal.id = :sucursalId AND g.activo = true")
    List<Grupo> findActivosByCategoriaIdAndSucursalId(@Param("categoriaId") Long categoriaId,
                                                      @Param("sucursalId") Long sucursalId);

    /*
     * ===============================================================================================================
     * Realizamos querys de busqueda por horarios
     * ===============================================================================================================
    */

    /**
     * Buscar grupos por horario de inicio
     */
    List<Grupo> findByHoraInicio(LocalTime horaInicio);

    /**
     * Buscar grupos por horario de fin
     */
    List<Grupo> findByHoraFin(LocalTime horaFin);

    /**
     * Buscar grupos que inicien antes de una hora específica
     */
    List<Grupo> findByHoraInicioBefore(LocalTime hora);

    /**
     * Buscar grupos que inicien después de una hora específica
     */
    List<Grupo> findByHoraInicioAfter(LocalTime hora);

    /**
     * Buscar grupos por rango de horario
     */
    @Query("SELECT g FROM Grupo g WHERE g.horaInicio >= :horaInicio AND g.horaFin <= :horaFin")
    List<Grupo> findByHorarioBetween(@Param("horaInicio") LocalTime horaInicio,
                                     @Param("horaFin") LocalTime horaFin);

    /**
     * Buscar grupos que se superpongan con un horario dado
     */
    @Query("SELECT g FROM Grupo g WHERE g.activo = true " +
            "AND ((g.horaInicio <= :horaFin AND g.horaFin >= :horaInicio))")
    List<Grupo> findGruposQueSeSuperpongan(@Param("horaInicio") LocalTime horaInicio,
                                           @Param("horaFin") LocalTime horaFin);

    /*
     * ===============================================================================================================
     * Realizamos busquedas por capacidad
     * ===============================================================================================================
    */

    /**
     * Buscar grupos por capacidad máxima
     */
    List<Grupo> findByCapacidadMax(int capacidadMax);

    /**
     * Buscar grupos con capacidad mayor a un valor
     */
    List<Grupo> findByCapacidadMaxGreaterThan(int capacidad);

    /**
     * Buscar grupos con capacidad menor a un valor
     */
    List<Grupo> findByCapacidadMaxLessThan(int capacidad);

    /**
     * Buscar grupos con capacidad entre dos valores
     */
    List<Grupo> findByCapacidadMaxBetween(int capacidadMin, int capacidadMax);

    /*
     * ===============================================================================================================
     *  Realizamos busquedas por precio
     * ===============================================================================================================
    */

    /**
     * Buscar grupos por precio exacto
     */
    List<Grupo> findByPrecioPorClase(BigDecimal precio);

    /**
     * Buscar grupos con precio mayor a un valor
     */
    List<Grupo> findByPrecioPorClaseGreaterThan(BigDecimal precio);

    /**
     * Buscar grupos con precio menor a un valor
     */
    List<Grupo> findByPrecioPorClaseLessThan(BigDecimal precio);

    /**
     * Buscar grupos con precio entre dos valores
     */
    List<Grupo> findByPrecioPorClaseBetween(BigDecimal precioMin, BigDecimal precioMax);

    /*
      * ===============================================================================================================
      *  Realizamos busquedas ordenadas 'ASC'/'DESC'
      * ===============================================================================================================
     */

    /**
     * Buscar todas las grupos ordenadas por nombre
     */
    List<Grupo> findAllByOrderByNombreAsc();

    /**
     * Buscar grupos activas ordenadas por nombre
     */
    List<Grupo> findByActivoTrueOrderByNombreAsc();

    /**
     * Buscar grupos por categoría ordenadas por nombre
     */
    @Query("SELECT g FROM Grupo g WHERE g.cate.id = :categoriaId ORDER BY g.nombre ASC")
    List<Grupo> findByCategoriaIdOrderByNombreAsc(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar grupos por sucursal ordenadas por nombre
     */
    @Query("SELECT g FROM Grupo g WHERE g.sucursal.id = :sucursalId ORDER BY g.nombre ASC")
    List<Grupo> findBySucursalIdOrderByNombreAsc(@Param("sucursalId") Long sucursalId);

    /*
      * ===============================================================================================================
      *  Realizamos consultas con FECTH (evitamos el N+1)
      * ===============================================================================================================
     */

    /**
     * Buscar grupo por ID con su categoría y sucursal (evita N+1)
     */
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.cate LEFT JOIN FETCH g.sucursal WHERE g.id = :id")
    Optional<Grupo> findByIdWithCategoriaAndSucursal(@Param("id") Long id);

    /**
     * Buscar todas los grupos con su categoría y sucursal
     */
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.cate LEFT JOIN FETCH g.sucursal")
    List<Grupo> findAllWithCategoriaAndSucursal();

    /**
     * Buscar grupos activos con su categoría y sucursal
     */
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.cate LEFT JOIN FETCH g.sucursal WHERE g.activo = true")
    List<Grupo> findAllActivosWithCategoriaAndSucursal();

    /**
     * Buscar grupos por categoría con su categoría y sucursal
     */
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.cate LEFT JOIN FETCH g.sucursal WHERE g.cate.id = :categoriaId")
    List<Grupo> findByCategoriaIdWithCategoriaAndSucursal(@Param("categoriaId") Long categoriaId);

    /**
     * Buscar grupos por sucursal con su categoría y sucursal
     */
    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.cate LEFT JOIN FETCH g.sucursal WHERE g.sucursal.id = :sucursalId")
    List<Grupo> findBySucursalIdWithCategoriaAndSucursal(@Param("sucursalId") Long sucursalId);

    /*
      * ===============================================================================================================
      *  Realizamos consultas de estadisticas
      * ===============================================================================================================
     */

    /**
     * Contar grupos activos
     */
    @Query("SELECT COUNT(g) FROM Grupo g WHERE g.activo = true")
    Long countGruposActivos();

    /**
     * Contar grupos inactivos
     */
    @Query("SELECT COUNT(g) FROM Grupo g WHERE g.activo = false")
    Long countGruposInactivos();

    /**
     * Contar grupos por categoría
     */
    @Query("SELECT g.cate.nombre, COUNT(g) FROM Grupo g GROUP BY g.cate.nombre")
    List<Object[]> countGruposByCategoria();

    /**
     * Contar grupos activos por categoría
     */
    @Query("SELECT g.cate.nombre, COUNT(g) FROM Grupo g WHERE g.activo = true GROUP BY g.cate.nombre")
    List<Object[]> countGruposActivosByCategoria();

    /**
     * Contar grupos por sucursal
     */
    @Query("SELECT g.sucursal.nombre, COUNT(g) FROM Grupo g GROUP BY g.sucursal.nombre")
    List<Object[]> countGruposBySucursal();

    /**
     * Contar grupos activos por sucursal
     */
    @Query("SELECT g.sucursal.nombre, COUNT(g) FROM Grupo g WHERE g.activo = true GROUP BY g.sucursal.nombre")
    List<Object[]> countGruposActivosBySucursal();

    /**
     * Calcular el precio promedio de los grupos activos
     */
    @Query("SELECT AVG(g.precioPorClase) FROM Grupo g WHERE g.activo = true")
    BigDecimal avgPrecioGruposActivos();

    /**
     * Encontrar el grupo más caro
     */
    @Query("SELECT g FROM Grupo g WHERE g.activo = true ORDER BY g.precioPorClase DESC")
    List<Grupo> findTopGruposByPrecio();

    /**
     * Encontrar la capacidad total de todos los grupos activos
     */
    @Query("SELECT SUM(g.capacidadMax) FROM Grupo g WHERE g.activo = true")
    Integer sumCapacidadTotalGruposActivos();

    /*
      * ===============================================================================================================
      *  Realizamos consultas SELECTS/DROPDOWNS
      * ===============================================================================================================
     */

    /**
     * Obtener solo ID y nombre de grupos activos (para combos/selects)
     */
    @Query("SELECT g.id, g.nombre FROM Grupo g WHERE g.activo = true ORDER BY g.nombre ASC")
    List<Object[]> findIdAndNombreByActivoTrue();

    /**
     * Obtener solo ID y nombre de grupos activos por categoría
     */
    @Query("SELECT g.id, g.nombre FROM Grupo g WHERE g.cate.id = :categoriaId AND g.activo = true ORDER BY g.nombre ASC")
    List<Object[]> findIdAndNombreByCategoriaIdAndActivoTrue(@Param("categoriaId") Long categoriaId);

    /**
     * Obtener solo ID y nombre de grupos activos por sucursal
     */
    @Query("SELECT g.id, g.nombre FROM Grupo g WHERE g.sucursal.id = :sucursalId AND g.activo = true ORDER BY g.nombre ASC")
    List<Object[]> findIdAndNombreBySucursalIdAndActivoTrue(@Param("sucursalId") Long sucursalId);

    /*
      * ===============================================================================================================
      *  Realizamos consultas de validacion
      * ===============================================================================================================
     */

    /**
     * Verificar si un grupo está activo
     */
    @Query("SELECT g.activo FROM Grupo g WHERE g.id = :id")
    Boolean isGrupoActivo(@Param("id") Long id);

    /**
     * Verificar si un grupo tiene clases asociadas
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Clase c WHERE c.grupo.id = :grupoId")
    Boolean hasClasesAsociadas(@Param("grupoId") Long grupoId);

    /**
     * Verificar si un grupo tiene inscripciones asociadas
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Inscripcion i WHERE i.grupo.id = :grupoId")
    Boolean hasInscripcionesAsociadas(@Param("grupoId") Long grupoId);

    /**
     * Verificar cupos disponibles en un grupo
     */
    @Query("SELECT g.capacidadMax - COUNT(i) " +
            "FROM Grupo g " +
            "LEFT JOIN Inscripcion i ON i.grupo.id = g.id AND i.estado = 'activa' " +
            "WHERE g.id = :grupoId " +
            "GROUP BY g.capacidadMax")
    Integer getCuposDisponibles(@Param("grupoId") Long grupoId);

    /**
     * Verificar si un grupo tiene cupos disponibles
     */
    @Query("SELECT (g.capacidadMax - COUNT(i)) > 0 " +
            "FROM Grupo g " +
            "LEFT JOIN Inscripcion i ON i.grupo.id = g.id AND i.estado = 'activa' " +
            "WHERE g.id = :grupoId " +
            "GROUP BY g.capacidadMax")
    Boolean hasCuposDisponibles(@Param("grupoId") Long grupoId);
}
