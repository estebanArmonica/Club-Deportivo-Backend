package com.clubdeportivo.services.categoria.utils;

import com.clubdeportivo.dtos.categoria.EstadisticasCategoriaDTO;
import com.clubdeportivo.models.Categoria;
import com.clubdeportivo.models.Deporte;
import com.clubdeportivo.repositories.ICategoriaRepository;
import com.clubdeportivo.repositories.IDeporteRepository;
import com.clubdeportivo.services.categoria.ICategoriaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaServiceImpl implements ICategoriaService {

    private final IDeporteRepository deporteRepo;
    private final ICategoriaRepository cateRepo;

    // Validaciones privadas
    private void validarRangoEdad(int edadMinima, int edadMaxima) {
        if(edadMinima > edadMaxima) {
            throw new RuntimeException("La edad mínima no puede ser mayor que la edad máxima");
        }

        if (edadMinima < 0) {
            throw new RuntimeException("La edad mínima no puede ser negativa");
        }

        if (edadMaxima < 0) {
            throw new RuntimeException("La edad máxima no puede ser negativa");
        }
    }

    private Deporte validarDeporte(Long deporteId) {
        if (deporteId == null) {
            throw new RuntimeException("La categoría debe tener un deporte asociado");
        }

        Deporte deporte = deporteRepo.findById(deporteId)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + deporteId));

        if (!deporte.getActivo()) {
            throw new RuntimeException("No se puede crear una categoría para un deporte inactivo");
        }

        return deporte;
    }

    // logica del crud
    @Override
    public Categoria create(Categoria categoria) {
        // validamos que el nombre no exista
        if(cateRepo.existsByNombre(categoria.getNombre())){
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }

        // validamos que el nombre no esté vacío
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría es obligatorio");
        }

        // validamos el rango de edades
        validarRangoEdad(categoria.getEdadMinima(), categoria.getEdadMaxima());

        // validamos el deporte
        Long deporteId = categoria.getDeporte() != null ? categoria.getDeporte().getId() : null;
        Deporte deporte = validarDeporte(deporteId);

        categoria.setDeporte(deporte);

        return cateRepo.save(categoria);
    }

    @Override
    public Categoria update(Long id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = cateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Validar que el nombre no exista en otra categoría
        if (cateRepo.existsByNombreAndIdNot(categoriaActualizada.getNombre(), id)) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + categoriaActualizada.getNombre());
        }

        // Validar que el nombre no esté vacío
        if (categoriaActualizada.getNombre() == null || categoriaActualizada.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría es obligatorio");
        }

        // Validar rango de edades
        validarRangoEdad(categoriaActualizada.getEdadMinima(), categoriaActualizada.getEdadMaxima());

        // Si se cambia el deporte, validar que exista y esté activo
        if (categoriaActualizada.getDeporte() != null &&
                categoriaActualizada.getDeporte().getId() != null &&
                !categoriaActualizada.getDeporte().getId().equals(categoriaExistente.getDeporte().getId())) {

            Deporte deporte = validarDeporte(categoriaActualizada.getDeporte().getId());
            categoriaExistente.setDeporte(deporte);
        }

        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setEdadMinima(categoriaActualizada.getEdadMinima());
        categoriaExistente.setEdadMaxima(categoriaActualizada.getEdadMaxima());
        categoriaExistente.setActivo(categoriaActualizada.getActivo());

        return cateRepo.save(categoriaExistente);
    }

    @Override
    public void deleteLogical(Long id) {
        Categoria cate = cateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("categoria no encontrada con ID: " + id));

        // verificamos que si tiene grupos asociados
        if(cateRepo.hasGruposAsociados(id)){
            throw new RuntimeException("No se puede desactivar la categoria por que tiene grupos asociados");
        }

        cate.setActivo(false);
        cateRepo.save(cate);
    }

    @Override
    public void deletePhysical(Long id) {

    }

    @Override
    public Categoria findById(Long id) {
        return cateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + id));
    }

    @Override
    public Categoria findByIdWithDeporte(Long id) {
        return cateRepo.findByIdWithDeporte(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con ID: " + id));
    }

    @Override
    public Categoria findByNombre(String nombre) {
        return cateRepo.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con nombre: " + nombre));
    }

    @Override
    public List<Categoria> findAll() {
        return cateRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Categoria> findAllWithDeporte() {
        return cateRepo.findAllWithDeporte();
    }

    @Override
    public List<Categoria> findActivas() {
        return cateRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Categoria> findActivasWithDeporte() {
        return cateRepo.findAllActivasWithDeporte();
    }

    @Override
    public List<Categoria> findByDeporte(Long deporteId) {
        return cateRepo.findByDeporteIdOrderByNombreAsc(deporteId);
    }

    @Override
    public List<Categoria> findActivasByDeporte(Long deporteId) {
        return cateRepo.findActivasByDeporteIdOrderByNombreAsc(deporteId);
    }

    @Override
    public List<Categoria> searchByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return cateRepo.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Categoria> findByEdad(int edad) {
        return cateRepo.findCategoriasByEdad(edad);
    }

    @Override
    public List<Categoria> findByDeporteAndEdad(Long deporteId, int edad) {
        return cateRepo.findCategoriasByDeporteAndEdad(deporteId, edad);
    }

    @Override
    public List<Object[]> getCategoriasParaSelect() {
        return cateRepo.findIdAndNombreByActivoTrue();
    }

    @Override
    public List<Object[]> getCategoriasByDeporteParaSelect(Long deporteId) {
        return cateRepo.findIdAndNombreByDeporteIdAndActivoTrue(deporteId);
    }

    @Override
    public EstadisticasCategoriaDTO getEstadisticas() {
        return EstadisticasCategoriaDTO.builder()
                .total(cateRepo.count())
                .activas(cateRepo.countCategoriasActivas())
                .inactivas(cateRepo.countCategoriasInactivas())
                .categoriasPorDeporte(cateRepo.countCategoriasByDeporte())
                .categoriasActivasPorDeporte(cateRepo.countCategoriasActivasByDeporte())
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return cateRepo.existsById(id);
    }

    @Override
    public boolean isActiva(Long id) {
        Boolean activo = cateRepo.isCategoriaActiva(id);
        if(activo == null){
            throw new RuntimeException("Categoria no encontrada con ID: " + id);
        }
        return activo;
    }
}
