package com.clubdeportivo.services.grupo.utils;

import com.clubdeportivo.dtos.grupo.EstadisticasGrupoDTO;
import com.clubdeportivo.models.Categoria;
import com.clubdeportivo.models.Grupo;
import com.clubdeportivo.models.Sucursal;
import com.clubdeportivo.repositories.ICategoriaRepository;
import com.clubdeportivo.repositories.IGrupoRepository;
import com.clubdeportivo.repositories.ISucursalRepository;
import com.clubdeportivo.services.grupo.IGrupoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GrupoServiceImpl implements IGrupoService {

    private final IGrupoRepository grupoRepo;
    private final ICategoriaRepository cateRepo;
    private final ISucursalRepository sucursalRepo;

    // validaciones privadas
    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if(horaInicio == null || horaFin == null){
            throw new RuntimeException("La hora de inicio y fin son obligatorias");
        }

        if (horaInicio.isAfter(horaFin)) {
            throw new RuntimeException("La hora de inicio no puede ser después de la hora de fin");
        }

        if (horaInicio.equals(horaFin)) {
            throw new RuntimeException("La hora de inicio y fin no pueden ser iguales");
        }
    }

    private void validarCapacidad(int capacidadMax) {
        if (capacidadMax <= 0) {
            throw new RuntimeException("La capacidad máxima debe ser mayor a 0");
        }
    }

    private void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio por clase debe ser mayor a 0");
        }
    }

    private Categoria validarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            throw new RuntimeException("El grupo debe tener una categoría asociada");
        }

        Categoria categoria = cateRepo.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));

        if (!categoria.getActivo()) {
            throw new RuntimeException("No se puede crear un grupo para una categoría inactiva");
        }

        return categoria;
    }

    private Sucursal validarSucursal(Long sucursalId) {
        if (sucursalId == null) {
            throw new RuntimeException("El grupo debe tener una sucursal asociada");
        }

        Sucursal sucursal = sucursalRepo.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + sucursalId));

        if (!sucursal.getActivo()) {
            throw new RuntimeException("No se puede crear un grupo para una sucursal inactiva");
        }

        return sucursal;
    }

    private void validarHorarioDisponible(Long sucursalId, Long categoriaId, LocalTime horaInicio, LocalTime horaFin, Long grupoId) {
        List<Grupo> gruposExistentes = grupoRepo.findGruposQueSeSuperpongan(horaInicio, horaFin);

        for (Grupo g : gruposExistentes) {
            if (g.getSucursal().getId().equals(sucursalId) &&
                    g.getCate().getId().equals(categoriaId) &&
                    g.getActivo() &&
                    (grupoId == null || !g.getId().equals(grupoId))) {
                throw new RuntimeException("Ya existe un grupo activo con el mismo horario en esta sucursal y categoría");
            }
        }
    }

    @Override
    public Grupo create(Grupo grupo) {
        // Validamos que el nombre no exista
        if (grupoRepo.existsByNombre(grupo.getNombre())) {
            throw new RuntimeException("Ya existe un grupo con el nombre: " + grupo.getNombre());
        }

        // Validamos que el nombre no esté vacío
        if (grupo.getNombre() == null || grupo.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del grupo es obligatorio");
        }

        // Validamos los días de las semanas
        if (grupo.getDiasSemana() == null || grupo.getDiasSemana().trim().isEmpty()) {
            throw new RuntimeException("Los días de la semana son obligatorios");
        }

        // Validamos el horario
        validarHorario(grupo.getHoraInicio(), grupo.getHoraFin());

        // Validamos la capacidad
        validarCapacidad(grupo.getCapacidadMax());

        // Validamos el precio
        validarPrecio(grupo.getPrecioPorClase());

        // Validamos la categoria
        Long categoriaId = grupo.getCate() != null ? grupo.getCate().getId() : null;
        Categoria categoria = validarCategoria(categoriaId);

        // Validamos la sucursal
        Long sucursalId = grupo.getSucursal() != null ? grupo.getSucursal().getId() : null;
        Sucursal sucursal = validarSucursal(sucursalId);

        // Validar que no exista conflicto de horario
        validarHorarioDisponible(sucursalId, categoriaId, grupo.getHoraInicio(), grupo.getHoraFin(), null);

        grupo.setCate(categoria);
        grupo.setSucursal(sucursal);

        return grupoRepo.save(grupo);
    }

    @Override
    public Grupo update(Long id, Grupo grupoActualizado) {
        Grupo grupoExistente = grupoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ya existe otro grupo con el ID: " + id));

        // validamos que el nombre no exista en otro grupo
        if(grupoRepo.existsByNombreAndIdNot(grupoActualizado.getNombre(), id)){
            throw new RuntimeException("Ya existe otro grupo con el nombre: " + grupoActualizado.getNombre());
        }

        // Validar que el nombre no esté vacío
        if (grupoActualizado.getNombre() == null || grupoActualizado.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del grupo es obligatorio");
        }

        // Validar días de la semana
        if (grupoActualizado.getDiasSemana() == null || grupoActualizado.getDiasSemana().trim().isEmpty()) {
            throw new RuntimeException("Los días de la semana son obligatorios");
        }

        // Validar horario
        validarHorario(grupoActualizado.getHoraInicio(), grupoActualizado.getHoraFin());

        // Validar capacidad
        validarCapacidad(grupoActualizado.getCapacidadMax());

        // Validar precio
        validarPrecio(grupoActualizado.getPrecioPorClase());

        // Si se cambia la categoría, validar que exista y esté activa
        Long nuevaCategoriaId = grupoActualizado.getCate() != null ? grupoActualizado.getCate().getId() : null;
        Long categoriaActualId = grupoExistente.getCate().getId();

        if (nuevaCategoriaId != null && !nuevaCategoriaId.equals(categoriaActualId)) {
            Categoria categoria = validarCategoria(nuevaCategoriaId);
            grupoExistente.setCate(categoria);
        }

        // Si se cambia la sucursal, validar que exista y esté activa
        Long nuevaSucursalId = grupoActualizado.getSucursal() != null ? grupoActualizado.getSucursal().getId() : null;
        Long sucursalActualId = grupoExistente.getSucursal().getId();

        if (nuevaSucursalId != null && !nuevaSucursalId.equals(sucursalActualId)) {
            Sucursal sucursal = validarSucursal(nuevaSucursalId);
            grupoExistente.setSucursal(sucursal);
        }

        // Validar conflicto de horario con el nuevo horario (si cambió)
        if (!grupoActualizado.getHoraInicio().equals(grupoExistente.getHoraInicio()) ||
                !grupoActualizado.getHoraFin().equals(grupoExistente.getHoraFin()) ||
                (nuevaCategoriaId != null && !nuevaCategoriaId.equals(categoriaActualId)) ||
                (nuevaSucursalId != null && !nuevaSucursalId.equals(sucursalActualId))) {

            Long sucursalFinalId = nuevaSucursalId != null ? nuevaSucursalId : sucursalActualId;
            Long categoriaFinalId = nuevaCategoriaId != null ? nuevaCategoriaId : categoriaActualId;

            validarHorarioDisponible(
                    sucursalFinalId,
                    categoriaFinalId,
                    grupoActualizado.getHoraInicio(),
                    grupoActualizado.getHoraFin(),
                    id
            );
        }

        grupoExistente.setNombre(grupoActualizado.getNombre());
        grupoExistente.setDiasSemana(grupoActualizado.getDiasSemana());
        grupoExistente.setHoraInicio(grupoActualizado.getHoraInicio());
        grupoExistente.setHoraFin(grupoActualizado.getHoraFin());
        grupoExistente.setCapacidadMax(grupoActualizado.getCapacidadMax());
        grupoExistente.setPrecioPorClase(grupoActualizado.getPrecioPorClase());
        grupoExistente.setActivo(grupoActualizado.getActivo());

        return grupoRepo.save(grupoExistente);
    }

    @Override
    public void delete(Long id) {
        Grupo grupo = grupoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + id));

        // Verificar si tiene clases asociadas
        if (grupoRepo.hasClasesAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar el grupo porque tiene clases asociadas");
        }

        // Verificar si tiene inscripciones asociadas
        if (grupoRepo.hasInscripcionesAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar el grupo porque tiene inscripciones asociadas");
        }

        grupo.setActivo(false);
        grupoRepo.save(grupo);
    }

    @Override
    public Grupo findById(Long id) {
        return grupoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado: " + id));
    }

    @Override
    public Grupo findByIdWithCategoriaAndSucursal(Long id) {
        return grupoRepo.findByIdWithCategoriaAndSucursal(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + id));
    }

    @Override
    public Grupo findByNombre(String nombre) {
        return grupoRepo.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Grupo no encontrado con nombre: " + nombre));
    }

    @Override
    public List<Grupo> findAll() {
        return grupoRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Grupo> findAllWithCategoriaAndSucursal() {
        return grupoRepo.findAllWithCategoriaAndSucursal();
    }

    @Override
    public List<Grupo> findActivos() {
        return grupoRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Grupo> findActivosWithCategoriaAndSucursal() {
        return grupoRepo.findAllActivosWithCategoriaAndSucursal();
    }

    @Override
    public List<Grupo> findByCategoria(Long categoriaId) {
        return grupoRepo.findByCategoriaIdOrderByNombreAsc(categoriaId);
    }

    @Override
    public List<Grupo> findActivosByCategoria(Long categoriaId) {
        return grupoRepo.findActivosByCategoriaId(categoriaId);
    }

    @Override
    public List<Grupo> findBySucursal(Long sucursalId) {
        return grupoRepo.findBySucursalIdOrderByNombreAsc(sucursalId);
    }

    @Override
    public List<Grupo> findActivosBySucursal(Long sucursalId) {
        return grupoRepo.findActivosBySucursalId(sucursalId);
    }

    @Override
    public List<Grupo> findByCategoriaAndSucursal(Long categoriaId, Long sucursalId) {
        return grupoRepo.findByCategoriaIdAndSucursalId(categoriaId, sucursalId);
    }

    @Override
    public List<Grupo> findActivosByCategoriaAndSucursal(Long categoriaId, Long sucursalId) {
        return grupoRepo.findActivosByCategoriaIdAndSucursalId(categoriaId, sucursalId);
    }

    @Override
    public List<Grupo> searchByNombre(String nombre) {
        if(nombre == null || nombre.trim().isEmpty()){
            return findAll();
        }
        return grupoRepo.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Grupo> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax) {
        if (precioMin == null || precioMax == null) {
            throw new RuntimeException("El precio mínimo y máximo son obligatorios");
        }
        
        if (precioMin.compareTo(precioMax) > 0) {
            throw new RuntimeException("El precio mínimo no puede ser mayor al precio máximo");
        }
        
        return grupoRepo.findByPrecioPorClaseBetween(precioMin, precioMax);
    }

    @Override
    public List<Grupo> findByCapacidadBetween(int capacidadMin, int capacidadMax) {
        if (capacidadMin > capacidadMax) {
            throw new RuntimeException("La capacidad mínima no puede ser mayor a la capacidad máxima");
        }
        
        return grupoRepo.findByCapacidadMaxBetween(capacidadMin, capacidadMax);
    }

    @Override
    public List<Grupo> findByHorario(LocalTime horaInicio, LocalTime horaFin) {
        validarHorario(horaInicio, horaFin);
        return grupoRepo.findByHorarioBetween(horaInicio, horaFin);
    }

    @Override
    public List<Object[]> getGruposParaSelect() {
        return grupoRepo.findIdAndNombreByActivoTrue();
    }

    @Override
    public List<Object[]> getGruposByCategoriaParaSelect(Long categoriaId) {
        return grupoRepo.findIdAndNombreByCategoriaIdAndActivoTrue(categoriaId);
    }

    @Override
    public List<Object[]> getGruposBySucursalParaSelect(Long sucursalId) {
        return grupoRepo.findIdAndNombreBySucursalIdAndActivoTrue(sucursalId);
    }

    @Override
    public EstadisticasGrupoDTO getEstadisticas() {
        return EstadisticasGrupoDTO.builder()
            .total(grupoRepo.count())
            .activos(grupoRepo.countGruposActivos())
            .inactivos(grupoRepo.countGruposInactivos())
            .gruposPorCategoria(grupoRepo.countGruposByCategoria())
            .gruposActivosPorCategoria(grupoRepo.countGruposActivosByCategoria())
            .gruposPorSucursal(grupoRepo.countGruposBySucursal())
            .gruposActivosPorSucursal(grupoRepo.countGruposActivosBySucursal())
            .precioPromedio(grupoRepo.avgPrecioGruposActivos())
            .capacidadTotal(grupoRepo.sumCapacidadTotalGruposActivos())
            .build();
    }

    @Override
    public boolean existsById(Long id) {
        return grupoRepo.existsById(id);
    }

    @Override
    public boolean isActivo(Long id) {
        Boolean activo = grupoRepo.isGrupoActivo(id);
        if(activo == null){
            throw new RuntimeException("Grupo no encontrado con ID: " + id);
        }
        return activo;
    }

    @Override
    public Integer getCuposDisponibles(Long grupoId) {
        Integer cupos = grupoRepo.getCuposDisponibles(grupoId);
        if(cupos == null){
            throw new RuntimeException("Grupo no encontrado con ID: " + grupoId);
        }
        return cupos;
    }

    @Override
    public boolean hasCuposDisponibles(Long grupoId) {
        Boolean tieneCupos = grupoRepo.hasCuposDisponibles(grupoId);
        if (tieneCupos == null) {
            throw new RuntimeException("Grupo no encontrado con ID: " + grupoId);
        }
        return tieneCupos;
    }
}
