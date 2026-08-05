package com.clubdeportivo.services.cancha.impl;

import com.clubdeportivo.dtos.cancha.EstadisticasCanchaDTO;
import com.clubdeportivo.models.Cancha;
import com.clubdeportivo.models.Sucursal;
import com.clubdeportivo.repositories.ICanchaRepository;
import com.clubdeportivo.repositories.ISucursalRepository;
import com.clubdeportivo.services.cancha.ICanchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CanchaServiceImpl implements ICanchaService {
    private ICanchaRepository canchaRepository;
    private ISucursalRepository sucursalRepository;

    // Validaciones privadas
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la cancha es obligatorio");
        }
    }

    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new RuntimeException("El tipo de cancha es obligatorio");
        }
    }

    private void validarCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new RuntimeException("La capacidad debe ser mayor a 0");
        }
    }

    private Sucursal validarSucursal(Long sucursalId) {
        if (sucursalId == null) {
            throw new RuntimeException("La cancha debe tener una sucursal asociada");
        }

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + sucursalId));

        if (!sucursal.getActivo()) {
            throw new RuntimeException("No se puede crear una cancha para una sucursal inactiva");
        }

        return sucursal;
    }

    private void validarNombreUnicoEnSucursal(String nombre, Long sucursalId, Long canchaId) {
        if (canchaRepository.existsByNombreAndSucursalIdAndIdNot(nombre, sucursalId, canchaId)) {
            throw new RuntimeException("Ya existe una cancha con el nombre '" + nombre + "' en esta sucursal");
        }
    }

    @Override
    public Cancha create(Cancha cancha) {
        // Validamos el nombre
        validarNombre(cancha.getNombre());

        // Validamos que el nombre no exista globalmente
        if (canchaRepository.existsByNombre(cancha.getNombre())) {
            throw new RuntimeException("Ya existe una cancha con el nombre: " + cancha.getNombre());
        }

        // Validamos el tipo
        validarTipo(cancha.getTipo());

        // Validamos la capacidad
        validarCapacidad(cancha.getCapacidad());

        // Validamos la sucursal
        Long sucursalId = cancha.getSucursal() != null ? cancha.getSucursal().getId() : null;
        Sucursal sucursal = validarSucursal(sucursalId);

        // Validamos que no exista otra cancha con el mismo nombre en la misma sucursal
        validarNombreUnicoEnSucursal(cancha.getNombre(), sucursalId, null);

        cancha.setSucursal(sucursal);

        return canchaRepository.save(cancha);
    }

    @Override
    public Cancha update(Long id, Cancha canchaActualizada) {
        Cancha canchaExistente = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));

        // Validar nombre
        validarNombre(canchaActualizada.getNombre());

        // Validar que el nombre no exista en otra cancha
        if (canchaRepository.existsByNombreAndIdNot(canchaActualizada.getNombre(), id)) {
            throw new RuntimeException("Ya existe otra cancha con el nombre: " + canchaActualizada.getNombre());
        }

        // Validar tipo
        validarTipo(canchaActualizada.getTipo());

        // Validar capacidad
        validarCapacidad(canchaActualizada.getCapacidad());

        // Si se cambia la sucursal, validar que exista y esté activa
        if (canchaActualizada.getSucursal() != null &&
                canchaActualizada.getSucursal().getId() != null &&
                !canchaActualizada.getSucursal().getId().equals(canchaExistente.getSucursal().getId())) {

            Sucursal sucursal = validarSucursal(canchaActualizada.getSucursal().getId());

            // Verificar que no exista otra cancha con el mismo nombre en la nueva sucursal
            validarNombreUnicoEnSucursal(canchaActualizada.getNombre(), sucursal.getId(), id);

            canchaExistente.setSucursal(sucursal);
        } else {
            // Verificar que no exista otra cancha con el mismo nombre en la misma sucursal
            Long sucursalId = canchaExistente.getSucursal().getId();
            validarNombreUnicoEnSucursal(canchaActualizada.getNombre(), sucursalId, id);
        }

        canchaExistente.setNombre(canchaActualizada.getNombre());
        canchaExistente.setTipo(canchaActualizada.getTipo());
        canchaExistente.setCapacidad(canchaActualizada.getCapacidad());
        canchaExistente.setDisponible(canchaActualizada.getDisponible());

        return canchaRepository.save(canchaExistente);
    }

    @Override
    public void delete(Long id) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));

        // Verificar si tiene reservas asociadas
        if (canchaRepository.hasReservasAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar la cancha porque tiene reservas asociadas");
        }

        cancha.setDisponible(false);
        canchaRepository.save(cancha);
    }

    @Override
    public Cancha findById(Long id) {
        return canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));
    }

    @Override
    public Cancha findByIdWithSucursal(Long id) {
        return canchaRepository.findByIdWithSucursal(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));
    }

    @Override
    public Cancha findByNombre(String nombre) {
        return canchaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con nombre: " + nombre));
    }

    @Override
    public List<Cancha> findAll() {
        return canchaRepository.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Cancha> findAllWithSucursal() {
        return canchaRepository.findAllWithSucursal();
    }

    @Override
    public List<Cancha> findDisponibles() {
        return canchaRepository.findByDisponibleTrueOrderByNombreAsc();
    }

    @Override
    public List<Cancha> findDisponiblesWithSucursal() {
        return canchaRepository.findAllDisponiblesWithSucursal();
    }

    @Override
    public List<Cancha> findBySucursal(Long sucursalId) {
        return canchaRepository.findBySucursalIdOrderByNombreAsc(sucursalId);
    }

    @Override
    public List<Cancha> findDisponiblesBySucursal(Long sucursalId) {
        return canchaRepository.findDisponiblesBySucursalIdOrderByNombreAsc(sucursalId);
    }

    @Override
    public List<Cancha> findByTipo(String tipo) {
        return canchaRepository.findByTipo(tipo);
    }

    @Override
    public List<Cancha> findDisponiblesByTipo(String tipo) {
        return canchaRepository.findByTipoAndDisponibleTrue(tipo);
    }

    @Override
    public List<Cancha> searchByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return canchaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Cancha> findByCapacidadBetween(int capacidadMin, int capacidadMax) {
        if (capacidadMin > capacidadMax) {
            throw new RuntimeException("La capacidad mínima no puede ser mayor a la capacidad máxima");
        }
        return canchaRepository.findByCapacidadBetween(capacidadMin, capacidadMax);
    }

    @Override
    public List<Cancha> findBySucursalAndTipo(Long sucursalId, String tipo) {
        return canchaRepository.findBySucursalIdAndTipo(sucursalId, tipo);
    }

    @Override
    public List<Cancha> findDisponiblesBySucursalAndTipo(Long sucursalId, String tipo) {
        return canchaRepository.findDisponiblesBySucursalIdAndTipo(sucursalId, tipo);
    }

    // Selects/Dropdowns
    @Override
    public List<Object[]> getCanchasParaSelect() {
        return canchaRepository.findIdAndNombreByDisponibleTrue();
    }

    @Override
    public List<Object[]> getCanchasBySucursalParaSelect(Long sucursalId) {
        return canchaRepository.findIdAndNombreBySucursalIdAndDisponibleTrue(sucursalId);
    }

    @Override
    public List<Object[]> getCanchasConTipoBySucursalParaSelect(Long sucursalId) {
        return canchaRepository.findIdNombreTipoBySucursalIdAndDisponibleTrue(sucursalId);
    }

    @Override
    public List<String> getTiposDisponibles() {
        return canchaRepository.findDistinctTiposByDisponibleTrue();
    }

    @Override
    public EstadisticasCanchaDTO getEstadisticas() {
        return EstadisticasCanchaDTO.builder()
                .total(canchaRepository.count())
                .disponibles(canchaRepository.countCanchasDisponibles())
                .noDisponibles(canchaRepository.countCanchasNoDisponibles())
                .canchasPorTipo(canchaRepository.countCanchasByTipo())
                .canchasDisponiblesPorTipo(canchaRepository.countCanchasDisponiblesByTipo())
                .canchasPorSucursal(canchaRepository.countCanchasBySucursal())
                .canchasDisponiblesPorSucursal(canchaRepository.countCanchasDisponiblesBySucursal())
                .capacidadPromedio(canchaRepository.avgCapacidadCanchasDisponibles())
                .capacidadTotal(canchaRepository.sumCapacidadTotalCanchasDisponibles())
                .build();
    }

    // Validaciones
    @Override
    public boolean existsById(Long id) {
        return canchaRepository.existsById(id);
    }

    @Override
    public boolean isDisponible(Long id) {
        Boolean disponible = canchaRepository.isCanchaDisponible(id);
        if (disponible == null) {
            throw new RuntimeException("Cancha no encontrada con ID: " + id);
        }
        return disponible;
    }

    @Override
    public Cancha cambiarDisponibilidad(Long id, boolean disponible) {
        Cancha cancha = findById(id);
        cancha.setDisponible(disponible);
        return canchaRepository.save(cancha);
    }
}
