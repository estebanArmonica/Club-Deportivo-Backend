package com.clubdeportivo.services.sucursal.impl;

import com.clubdeportivo.dtos.sucursal.EstadisticasSucursalDTO;
import com.clubdeportivo.models.Club;
import com.clubdeportivo.models.Sucursal;
import com.clubdeportivo.repositories.IClubRepository;
import com.clubdeportivo.repositories.ISucursalRepository;
import com.clubdeportivo.services.sucursal.ISucursalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SucursalServiceImpl implements ISucursalService {
    private final ISucursalRepository sucursalRepo;
    private final IClubRepository clubRepo;

    // Validaciones privadas
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la sucursal es obligatorio");
        }
    }

    private void validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new RuntimeException("La dirección es obligatoria");
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        // Solo números, guiones y espacios
        if (!telefono.matches("[0-9\\-\\s+]+")) {
            throw new RuntimeException("El teléfono solo debe contener números, guiones, espacios y el signo +");
        }
    }

    private Club validarClub(Long clubId) {
        if (clubId == null) {
            throw new RuntimeException("La sucursal debe tener un club asociado");
        }

        Club club = clubRepo.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con ID: " + clubId));

        if (!club.getActivo()) {
            throw new RuntimeException("No se puede crear una sucursal para un club inactivo");
        }

        return club;
    }

    // Crud
    @Override
    public Sucursal create(Sucursal sucursal) {
        // Validar nombre
        validarNombre(sucursal.getNombre());

        // Validar que el nombre no exista
        if (sucursalRepo.existsByNombre(sucursal.getNombre())) {
            throw new RuntimeException("Ya existe una sucursal con el nombre: " + sucursal.getNombre());
        }

        // Validamos la dirección
        validarDireccion(sucursal.getDireccion());

        // Validamos el teléfono
        validarTelefono(sucursal.getTelefono());

        // Validamos el club si existe en una sucursal
        Long clubId = sucursal.getClub() != null ? sucursal.getClub().getId() : null;
        Club club = validarClub(clubId);

        sucursal.setClub(club);

        return sucursalRepo.save(sucursal);
    }

    @Override
    public Sucursal update(Long id, Sucursal sucursalActualizada) {
        Sucursal sucursalExistente = sucursalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));

        // Validamos el nombre
        validarNombre(sucursalActualizada.getNombre());

        // Validamos que el nombre no exista en otra sucursal
        if (sucursalRepo.existsByNombreAndIdNot(sucursalActualizada.getNombre(), id)) {
            throw new RuntimeException("Ya existe otra sucursal con el nombre: " + sucursalActualizada.getNombre());
        }

        // Validamos la dirección
        validarDireccion(sucursalActualizada.getDireccion());

        // Validamos el teléfono
        validarTelefono(sucursalActualizada.getTelefono());

        // Si se cambia el club, validar que exista y esté activo
        if (sucursalActualizada.getClub() != null &&
                sucursalActualizada.getClub().getId() != null &&
                !sucursalActualizada.getClub().getId().equals(sucursalExistente.getClub().getId())) {

            Club club = validarClub(sucursalActualizada.getClub().getId());
            sucursalExistente.setClub(club);
        }

        sucursalExistente.setNombre(sucursalActualizada.getNombre());
        sucursalExistente.setDireccion(sucursalActualizada.getDireccion());
        sucursalExistente.setTelefono(sucursalActualizada.getTelefono());
        sucursalExistente.setActivo(sucursalActualizada.getActivo());

        return sucursalRepo.save(sucursalExistente);
    }

    @Override
    public void delete(Long id) {
        Sucursal sucursal = sucursalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));

        // Verificar si tiene grupos asociados
        if (sucursalRepo.hasGruposAsociados(id)) {
            throw new RuntimeException("No se puede desactivar la sucursal porque tiene grupos asociados");
        }

        // Verificar si tiene canchas asociadas
        if (sucursalRepo.hasCanchasAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar la sucursal porque tiene canchas asociadas");
        }

        // Verificar si tiene encargados asociados
        if (sucursalRepo.hasEncargadosAsociados(id)) {
            throw new RuntimeException("No se puede desactivar la sucursal porque tiene encargados asociados");
        }

        sucursal.setActivo(false);
        sucursalRepo.save(sucursal);
    }

    @Override
    public Sucursal findById(Long id) {
        return sucursalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }

    @Override
    public Sucursal findByIdWithClub(Long id) {
        return sucursalRepo.findByIdWithClub(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }

    @Override
    public Sucursal findByNombre(String nombre) {
        return sucursalRepo.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con nombre: " + nombre));
    }

    @Override
    public List<Sucursal> findAll() {
        return sucursalRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Sucursal> findAllWithClub() {
        return sucursalRepo.findAllWithClub();
    }

    @Override
    public List<Sucursal> findActivas() {
        return sucursalRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Sucursal> findActivasWithClub() {
        return sucursalRepo.findAllActivasWithClub();
    }

    @Override
    public List<Sucursal> findByClub(Long clubId) {
        return sucursalRepo.findByClubIdOrderByNombreAsc(clubId);
    }

    @Override
    public List<Sucursal> findActivasByClub(Long clubId) {
        return sucursalRepo.findActivasByClubIdOrderByNombreAsc(clubId);
    }

    @Override
    public List<Sucursal> searchByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return sucursalRepo.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Sucursal> searchByDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            return findAll();
        }
        return sucursalRepo.findByDireccionContainingIgnoreCase(direccion);
    }

    @Override
    public Sucursal findByTelefono(String telefono) {
        return sucursalRepo.findByTelefono(telefono)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con teléfono: " + telefono));
    }

    // Selects/Dropdowns
    @Override
    public List<Object[]> getSucursalesParaSelect() {
        return sucursalRepo.findIdAndNombreByActivoTrue();
    }

    @Override
    public List<Object[]> getSucursalesByClubParaSelect(Long clubId) {
        return sucursalRepo.findIdAndNombreByClubIdAndActivoTrue(clubId);
    }

    @Override
    public EstadisticasSucursalDTO getEstadisticas() {
        return EstadisticasSucursalDTO.builder()
                .total(sucursalRepo.count())
                .activas(sucursalRepo.countSucursalesActivas())
                .inactivas(sucursalRepo.countSucursalesInactivas())
                .sucursalesPorClub(sucursalRepo.countSucursalesByClub())
                .sucursalesActivasPorClub(sucursalRepo.countSucursalesActivasByClub())
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return sucursalRepo.existsById(id);
    }

    @Override
    public boolean isActiva(Long id) {
        Boolean activo = sucursalRepo.isSucursalActiva(id);
        if (activo == null) {
            throw new RuntimeException("Sucursal no encontrada con ID: " + id);
        }
        return activo;
    }
}
