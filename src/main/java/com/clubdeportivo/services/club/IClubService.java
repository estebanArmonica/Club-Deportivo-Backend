package com.clubdeportivo.services.club;

import com.clubdeportivo.dtos.club.EstadisticasClubDTO;
import com.clubdeportivo.models.Club;

import java.time.LocalDate;
import java.util.List;

public interface IClubService {

    /**
     * Creamos una categoria
     * @param club
     * @return retorna un objeto nuevo
     **/
    Club create(Club club);

    /**
     * Actualizamos un club existente a travez de su ID
     * @param id
     * @param clubActualizado
     * @return retorna un club actualizado
     */
    Club update(Long id, Club clubActualizado);
    void deleteLogical(Long id);
    Club findById(Long id);
    Club findByNombre(String nombre);
    Club findByCuit(String cuit);
    Club findByEmail(String email);
    List<Club> findAll();
    List<Club> findActivos();
    List<Club> searchByNombre(String nombre);
    List<Club> searchByDireccion(String direccion);
    List<Club> findByFechCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscamos clubes activos creados despues de una fecha
     * @param fecha
     * @return una lista de clubes activos
     */
    List<Club> findActivosCreadosDespuesDe(LocalDate fecha);

    // metodos de select / dropdown
    List<Object[]> getClubesParaSelect();
    List<Object[]> getClubesConCuitParaSelect();

    /**
     * Obtenemos estadisticas de clubes
     * @return una lista de estadisticas
     */
    EstadisticasClubDTO getEstadisticas();

    // Validaciones
    boolean existsById(Long id);
    boolean isActivo(Long id);
    boolean existsByEmail(String email);
    boolean existsByCuit(String cuit);

}
