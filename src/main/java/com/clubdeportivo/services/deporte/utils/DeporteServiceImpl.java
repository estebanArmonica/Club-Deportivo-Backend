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

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeporteServiceImpl implements IDeporteService {

    private IDeporteRepository deporteRepo;

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
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Deporte findById(Long id) {
        return null;
    }

    @Override
    public Deporte findByNombre(String nombre) {
        return null;
    }

    @Override
    public List<Deporte> findAll() {
        return null;
    }

    @Override
    public List<Deporte> findActivos() {
        return null;
    }

    @Override
    public List<Deporte> searchByNombre(String nombre) {
        return null;
    }

    @Override
    public List<Object[]> getDeportesParaSelect() {
        return null;
    }

    @Override
    public List<Object[]> getTodosDeportesParaSelect() {
        return null;
    }

    @Override
    public EstadisticasDeporteDTO getEstadisticas() {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public boolean isActivo(Long id) {
        return false;
    }
}
