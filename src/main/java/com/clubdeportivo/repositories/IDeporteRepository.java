package com.clubdeportivo.repositories;

import com.clubdeportivo.models.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDeporteRepository extends JpaRepository<Deporte, Long> {
    /*
     * Esté repositorio tiene el fin de crear la logica de negocio en Deporte
     * tiene el CRUD Básico y funcionalidades técnicas
    */

    /*
     * ===========================================================================
     * Busquedas Básicas
     * ===========================================================================
    */
    Optional<Deporte> findByNombre(String nombre);
    List<Deporte> findByNombreContainingIgnoreCase(String nombre);
    List<Deporte> findByActivoTrue();
    List<Deporte> findByActivoFalse();
    Boolean existsByNombre(String nombre);


    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Deporte d " +
            "WHERE d.nombre = :nombre AND d.id != :id")
    Boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    /*
     * ===========================================================================
     * Realización de busquedas de forma ordenada 'ASC'/'DESC'
     * ===========================================================================
    */
    List<Deporte> findAllByOrderByNombreAsc();
    List<Deporte> findByActivoTrueOrderByNombreAsc();
    List<Deporte> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);

    /*
     * ===========================================================================
     * Consultas Query con Estadisticas
     * ===========================================================================
    */
    @Query("SELECT COUNT(d) FROM Deporte d WHERE d.activo = true")
    Long countDeportesActivos();

    @Query("SELECT COUNT(d) FROM Deporte d WHERE d.activo = false")
    Long countDeportesInactivos();

    @Query("SELECT COUNT(d) FROM Deporte d")
    Long countTotalDeportes();

    /*
     * ===========================================================================
     * Consultas Querys para SELECT/DROPDOWS (solo el id y nombre)
     * ===========================================================================
    */
    @Query("SELECT d.id, d.nombre FROM Deporte d WHERE d.activo = true ORDER BY d.nombre ASC")
    List<Object[]> findIdAndNombreByActivoTrue();

    @Query("SELECT d.id, d.nombre FROM Deporte d ORDER BY d.nombre ASC")
    List<Object[]> findAllIdAndNombre();

    /*
     * ===========================================================================
     * Consultas Querys de Validación de datos y activos
     * ===========================================================================
    */
    @Query("SELECT d.activo FROM Deporte d WHERE d.id = :id")
    Boolean isDeporteActivo(@Param("id") Long id);
    List<Deporte> findByNombreStartingWithIgnoreCase(String prefijo);

    List<Deporte> findByNombreEndingWithIgnoreCase(String sufijo);
}
