package com.clubdeportivo.services.grupo;

import com.clubdeportivo.dtos.grupo.EstadisticasGrupoDTO;
import com.clubdeportivo.models.Grupo;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public interface IGrupoService {
    Grupo create(Grupo grupo);
    Grupo update(Long id, Grupo grupoActualizado);
    void delete(Long id);

    // Busquedas
    Grupo findById(Long id);
    Grupo findByIdWithCategoriaAndSucursal(Long id);
    Grupo findByNombre(String nombre);
    List<Grupo> findAll();
    List<Grupo> findAllWithCategoriaAndSucursal();
    List<Grupo> findActivos();
    List<Grupo> findActivosWithCategoriaAndSucursal();
    List<Grupo> findByCategoria(Long categoriaId);
    List<Grupo> findActivosByCategoria(Long categoriaId);
    List<Grupo> findBySucursal(Long sucursalId);
    List<Grupo> findActivosBySucursal(Long sucursalId);
    List<Grupo> findByCategoriaAndSucursal(Long categoriaId, Long sucursalId);
    List<Grupo> findActivosByCategoriaAndSucursal(Long categoriaId, Long sucursalId);
    List<Grupo> searchByNombre(String nombre);
    List<Grupo> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    List<Grupo> findByCapacidadBetween(int capacidadMin, int capacidadMax);
    List<Grupo> findByHorario(LocalTime horaInicio, LocalTime horaFin);
    List<Object[]> getGruposParaSelect();
    List<Object[]> getGruposByCategoriaParaSelect(Long categoriaId);
    List<Object[]> getGruposBySucursalParaSelect(Long sucursalId);
    EstadisticasGrupoDTO getEstadisticas();
    boolean existsById(Long id);
    boolean isActivo(Long id);
    Integer getCuposDisponibles(Long grupoId);
    boolean hasCuposDisponibles(Long grupoId);
}
