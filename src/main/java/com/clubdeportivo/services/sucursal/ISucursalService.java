package com.clubdeportivo.services.sucursal;

import com.clubdeportivo.dtos.sucursal.EstadisticasSucursalDTO;
import com.clubdeportivo.models.Sucursal;

import java.util.List;

public interface ISucursalService {
    // crud
    Sucursal create(Sucursal sucursal);
    Sucursal update(Long id, Sucursal sucursalActualizada);
    void delete(Long id);

    // busquedas
    Sucursal findById(Long id);
    Sucursal findByIdWithClub(Long id);
    Sucursal findByNombre(String nombre);
    List<Sucursal> findAll();
    List<Sucursal> findAllWithClub();
    List<Sucursal> findActivas();
    List<Sucursal> findActivasWithClub();
    List<Sucursal> findByClub(Long clubId);
    List<Sucursal> findActivasByClub(Long clubId);
    List<Sucursal> searchByNombre(String nombre);
    List<Sucursal> searchByDireccion(String direccion);
    Sucursal findByTelefono(String telefono);

    // Select/Dropdowns
    List<Object[]> getSucursalesParaSelect();
    List<Object[]> getSucursalesByClubParaSelect(Long clubId);

    // Estadisticas
    EstadisticasSucursalDTO getEstadisticas();

    // Validaciones
    boolean existsById(Long id);
    boolean isActiva(Long id);
}
