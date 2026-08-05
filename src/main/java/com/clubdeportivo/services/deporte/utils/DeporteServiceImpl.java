package com.clubdeportivo.services.deporte.utils;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clubdeportivo.models.Deporte;
import com.clubdeportivo.repositories.IDeporteRepository;
import com.clubdeportivo.services.deporte.IDeporteService;
import com.clubdeportivo.dtos.deporte.EstadisticasDeporteDTO;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeporteServiceImpl implements IDeporteService {

    private final IDeporteRepository deporteRepo;

    @Override
    public Deporte create(Deporte deporte) {
        // validamos que el nombre del deporte no exista
        if(deporteRepo.existsByNombre(deporte.getNombre())) {
            throw new RuntimeException("Ya existe un deporte con el nombre: " + deporte.getNombre());
        }

        // Validamos que el nombre no esté vacío
        if (deporte.getNombre() == null || deporte.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del deporte es obligatorio");
        }

        // Validamos que la descripción no esté vacía
        if (deporte.getDescripcion() == null || deporte.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción del deporte es obligatoria");
        }

        return deporteRepo.save(deporte);
    }

    @Override
    public Deporte update(Long id, Deporte deporte) {
        Deporte deporteExiste = deporteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte con el ID no encontrado: " + id));

        // Validamos que el nombre no exista en otro deporte
        if(deporteRepo.existsByNombreAndIdNot(deporte.getNombre(), id)){
            throw new RuntimeException("Ya existe otro deporte con el nombre: " + deporte.getNombre());
        }

        // validamos que el nombre no esté vacío
        if(deporte.getNombre() == null || deporte.getNombre().trim().isEmpty()){
            throw new RuntimeException("El nombre del deporte es obligatorio." );
        }

        // validamos que la descripcion no esté vacía
        if(deporte.getDescripcion() == null || deporte.getDescripcion().trim().isEmpty()){
            throw new RuntimeException("La descripcion del deporte es obligatorio." );
        }

        // actualizamos los datos de un deporte
        deporteExiste.setNombre(deporte.getNombre());
        deporteExiste.setDescripcion(deporte.getDescripcion());
        deporteExiste.setActivo(deporte.getActivo());

        return deporteRepo.save(deporteExiste);
    }

    @Override
    public void delete(Long id) {
        Deporte deporte = deporteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + id));

        deporte.setActivo(false);
        deporteRepo.save(deporte);
    }

    @Override
    public Deporte findById(Long id) {
        return deporteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte con el ID no encontrado: " + id));
    }

    @Override
    public Deporte findByNombre(String nombre) {
        return deporteRepo.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Deporte con el nombre no encontrado: " + nombre));
    }

    @Override
    public List<Deporte> findAll() {
        return deporteRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Deporte> findActivos() {
        return deporteRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Deporte> searchByNombre(String nombre) {
        if(nombre == null || nombre.trim().isEmpty()){
            return findAll();
        }
        return deporteRepo.findByNombreContainingIgnoreCaseOrderByNombreAsc(nombre);
    }

    @Override
    public List<Object[]> getDeportesParaSelect() {
        return deporteRepo.findIdAndNombreByActivoTrue();
    }

    @Override
    public List<Object[]> getTodosDeportesParaSelect() {
        return deporteRepo.findAllIdAndNombre();
    }

    @Override
    public EstadisticasDeporteDTO getEstadisticas() {
        return EstadisticasDeporteDTO.builder()
                .total(deporteRepo.countTotalDeportes())
                .activos(deporteRepo.countDeportesActivos())
                .inactivos(deporteRepo.countDeportesInactivos())
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return deporteRepo.existsById(id);
    }

    @Override
    public boolean isActivo(Long id) {
        Boolean activo = deporteRepo.isDeporteActivo(id);
        if(activo == null){
            throw new RuntimeException("Deporte no encontrado con ID: " + id);
        }
        return activo;
    }
}