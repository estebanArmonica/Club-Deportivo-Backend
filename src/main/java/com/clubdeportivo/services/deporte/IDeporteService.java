package com.clubdeportivo.services.deporte;

import java.util.List;

import com.clubdeportivo.models.Deporte;
import com.clubdeportivo.dtos.deporte.EstadisticasDeporteDTO;
public interface IDeporteService {
    /*
     *  Crud básico
    */

    // creamos un nuevo deporte
    Deporte create(Deporte deporte);

    // actualizamos un deporte existente buscando su id
    Deporte update(Long id, Deporte deporte);

    // eliminamos un deporte (soft delete)
    void delete(Long id);

    /*
     *  Búsquedas
    */

    // buscamos un deporte por el id
    Deporte findById(Long id);

    // buscamos un deporte por el nombre
    Deporte findByNombre(String nombre);

    // buscamos todos los deportes
    List<Deporte> findAll();

    // buscamos deportes activos
    List<Deporte> findActivos();

    // buscamos por nombre (busqueda parcial)
    List<Deporte> searchByNombre(String nombre);

    /*
     * Select y Dropdown
    */

    // obtenemos una lista de los deportes para combos
    List<Object[]> getDeportesParaSelect();

    // Obtenemos una lista de todos los deportes para combos
    List<Object[]> getTodosDeportesParaSelect();

    /*
     * Estadisticas
    */

    // Obtenemos estadisticas de deportes
    EstadisticasDeporteDTO getEstadisticas();

    /*
     * Validaciones
    */

    // verificamos que el deporte exista
    boolean existsById(Long id);

    // Verificamos si un deporte está activo
    boolean isActivo(Long id);
}
