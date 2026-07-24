package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    /*
     * Esté repositorio tiene el fin de realizar la logica de negocio en Categoria
     * realizando busquedas faciles hasta métodos de consultas SQL técnicas
     *  y estadisticas que pueden beneficiar a futuras implementaciones dentro del código
    */

    /*
     * ===========================================================================================
     *  Realización de busquedas básicas
     * ===========================================================================================
    */
    Optional<Categoria> findByNombre(String nombre);
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
    List<Categoria> findByActivoTrue();
    List<Categoria> findByActivoFalse();
    Boolean existsByNombre(String nombre);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Categoria c " +
            "WHERE c.nombre = :nombre AND c.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    /*
     * ===========================================================================================
     *  Realización de consultas querys con relacion con deporte
     * ===========================================================================================
    */
    @Query("SELECT c FROM Categoria c WHERE c.deporte.id = :deporteId")
    List<Categoria> findByDeporteId(@Param("deporteId") Long deporteId);

    /**
     * Buscamos categorías activas por ID de deporte
     */
    @Query("SELECT c FROM Categoria c WHERE c.deporte.id = :deporteId AND c.activo = true")
    List<Categoria> findActivasByDeporteId(@Param("deporteId") Long deporteId);

    /**
     * Buscamos categorías por nombre de deporte
     */
    @Query("SELECT c FROM Categoria c WHERE c.deporte.nombre = :nombreDeporte")
    List<Categoria> findByDeporteNombre(@Param("nombreDeporte") String nombreDeporte);

    /**
     * Buscamos categorías activas por nombre de deporte
     */
    @Query("SELECT c FROM Categoria c WHERE c.deporte.nombre = :nombreDeporte AND c.activo = true")
    List<Categoria> findActivasByDeporteNombre(@Param("nombreDeporte") String nombreDeporte);

    /**
     * Buscamos categorías por rango de edad (edad mínima y máxima)
     */
    @Query("SELECT c FROM Categoria c WHERE c.edadMinima <= :edad AND c.edadMaxima >= :edad AND c.activo = true")
    List<Categoria> findCategoriasByEdad(@Param("edad") int edad);

    /**
     * Buscamos categorías por rango de edad y deporte
     */
    @Query("SELECT c FROM Categoria c " +
            "WHERE c.deporte.id = :deporteId " +
            "AND c.edadMinima <= :edad " +
            "AND c.edadMaxima >= :edad " +
            "AND c.activo = true")
    List<Categoria> findCategoriasByDeporteAndEdad(@Param("deporteId") Long deporteId,
                                                   @Param("edad") int edad);

    /*
     * ===========================================================================================
     *  Realización de consultas querys de busqueda por ordenamiento 'ASC'/'DESC'
     * ===========================================================================================
    */

    /**
     * Buscamos todas las categorías ordenadas por nombre
     */
    List<Categoria> findAllByOrderByNombreAsc();

    /**
     * Buscamos categorías activas ordenadas por nombre
     */
    List<Categoria> findByActivoTrueOrderByNombreAsc();

    /**
     * Buscamos categorías por deporte ordenadas por nombre
     */
    @Query("SELECT c FROM Categoria c WHERE c.deporte.id = :deporteId ORDER BY c.nombre ASC")
    List<Categoria> findByDeporteIdOrderByNombreAsc(@Param("deporteId") Long deporteId);

    /**
     * Buscamos categorías activas por deporte ordenadas por nombre
     */
    @Query("SELECT c FROM Categoria c WHERE c.deporte.id = :deporteId AND c.activo = true ORDER BY c.nombre ASC")
    List<Categoria> findActivasByDeporteIdOrderByNombreAsc(@Param("deporteId") Long deporteId);

    /*
     * ===========================================================================================
     *  Realización de consultas querys con FECTH (evitamos problemas futuros con (N+1)
     * ===========================================================================================
     */

    /**
     * Buscamos categoría por ID con su deporte (evita N+1)
     */
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.deporte WHERE c.id = :id")
    Optional<Categoria> findByIdWithDeporte(@Param("id") Long id);

    /**
     * Buscamos todas las categorías con su deporte
     */
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.deporte")
    List<Categoria> findAllWithDeporte();

    /**
     * Buscamos categorías activas con su deporte
     */
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.deporte WHERE c.activo = true")
    List<Categoria> findAllActivasWithDeporte();

    /**
     * Buscamos categorías por deporte con su deporte
     */
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.deporte WHERE c.deporte.id = :deporteId")
    List<Categoria> findByDeporteIdWithDeporte(@Param("deporteId") Long deporteId);

    /*
     * ===========================================================================================
     *  Realización de consultas querys con estadisticas (para futuras implementaciones)
     * ===========================================================================================
    */

    /**
     * Contamos categorías activas
     */
    @Query("SELECT COUNT(c) FROM Categoria c WHERE c.activo = true")
    Long countCategoriasActivas();

    /**
     * Contamos categorías inactivas
     */
    @Query("SELECT COUNT(c) FROM Categoria c WHERE c.activo = false")
    Long countCategoriasInactivas();

    /**
     * Contamos categorías por deporte
     */
    @Query("SELECT c.deporte.nombre, COUNT(c) FROM Categoria c GROUP BY c.deporte.nombre")
    List<Object[]> countCategoriasByDeporte();

    /**
     * Contamos categorías activas por deporte
     */
    @Query("SELECT c.deporte.nombre, COUNT(c) FROM Categoria c WHERE c.activo = true GROUP BY c.deporte.nombre")
    List<Object[]> countCategoriasActivasByDeporte();

    /*
     * ===========================================================================================
     *  Realización de consultas SELECTS/DROPDOWNS
     * ===========================================================================================
    */

    /**
     * Obtenemos solo ID y nombre de categorías activas (para combos/selects)
     */
    @Query("SELECT c.id, c.nombre FROM Categoria c WHERE c.activo = true ORDER BY c.nombre ASC")
    List<Object[]> findIdAndNombreByActivoTrue();

    /**
     * Obtenenemos solo ID y nombre de categorías activas por deporte
     */
    @Query("SELECT c.id, c.nombre FROM Categoria c WHERE c.deporte.id = :deporteId AND c.activo = true ORDER BY c.nombre ASC")
    List<Object[]> findIdAndNombreByDeporteIdAndActivoTrue(@Param("deporteId") Long deporteId);

    /*
     * ===========================================================================================
     *  Realización de consultas de validacion
     * ===========================================================================================
    */

    /**
     * Verifica si una categoría está activa
     */
    @Query("SELECT c.activo FROM Categoria c WHERE c.id = :id")
    Boolean isCategoriaActiva(@Param("id") Long id);

    /**
     * Verifica si una categoría tiene grupos asociados
     */
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " +
            "FROM Grupo g WHERE g.cate.id = :categoriaId")
    Boolean hasGruposAsociados(@Param("categoriaId") Long categoriaId);

    /**
     * Busca categorías por rango de edad exacto
     */
    @Query("SELECT c FROM Categoria c WHERE c.edadMinima = :edadMin AND c.edadMaxima = :edadMax")
    List<Categoria> findByEdadMinimaAndEdadMaxima(@Param("edadMin") int edadMin,
                                                  @Param("edadMax") int edadMax);
}

