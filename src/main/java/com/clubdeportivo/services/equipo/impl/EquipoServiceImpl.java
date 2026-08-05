package com.clubdeportivo.services.equipo.impl;

import com.clubdeportivo.dtos.equipo.EstadisticasEquipoDTO;
import com.clubdeportivo.models.Equipo;
import com.clubdeportivo.models.Grupo;
import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IEquipoRepository;
import com.clubdeportivo.repositories.IGrupoRepository;
import com.clubdeportivo.repositories.IUsuarioRepository;
import com.clubdeportivo.services.equipo.IEquipoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EquipoServiceImpl implements IEquipoService {

    // realizamos injección de dependencias
    private final IEquipoRepository equipoRepo;
    private final IGrupoRepository grupoRepo;
    private final IUsuarioRepository userRepo;

    /**
     * Realizamos validaciones privadas
     */
    private void validarNombre(String nombre) {
        if(nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre del equipo es obligatorio");
        }
    }

    private Grupo validarGrupo(Long grupoId) {
        if (grupoId == null) {
            throw new RuntimeException("El equipo debe tener un grupo asociado");
        }

        Grupo grupo = grupoRepo.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + grupoId));

        if (!grupo.getActivo()) {
            throw new RuntimeException("No se puede crear un equipo para un grupo inactivo");
        }

        return grupo;
    }

    private Usuario validarUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("El equipo debe tener un capitán (usuario) asignado");
        }

        Usuario usuario = userRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        if (!usuario.getIsActive()) {
            throw new RuntimeException("No se puede asignar un capitán inactivo al equipo");
        }

        return usuario;
    }

    private void validarUsuarioCapitanEnGrupo(Long usuarioId, Long grupoId, Long equipoId) {
        if (equipoRepo.existsUsuarioCapitanEnGrupo(usuarioId, grupoId, equipoId)) {
            throw new RuntimeException("El usuario ya es capitán de un equipo activo en este grupo");
        }
    }

    /**
     * Logica del CRUD
     */

    @Override
    public Equipo create(Equipo equipo) {
        // Validamos el nombre
        validarNombre(equipo.getNombre());

        // Validamos que el nombre no exista
        if(equipoRepo.existsByNombre(equipo.getNombre())) {
            throw new RuntimeException("Ya existe un equipo con el nombre: " + equipo.getNombre());
        }

        // Validamos el grupo
        Long grupoId = equipo.getGrupo() != null ? equipo.getGrupo().getId() : null;
        Grupo grupo = validarGrupo(grupoId);

        // Validamos el usuario (el rol capitan)
        Long usuarioId = equipo.getUser() != null ? equipo.getUser().getId() : null;
        Usuario usuario = validarUsuario(usuarioId);

        // Validamos que el usuario no sea capitan de otro equipo en el mismo grupo
        validarUsuarioCapitanEnGrupo(usuarioId, grupoId, null);

        // Si no se especifica fecha de creación, usar la actual
        if (equipo.getFechCreacion() == null) {
            equipo.setFechCreacion(LocalDate.now());
        }

        equipo.setGrupo(grupo);
        equipo.setUser(usuario);

        return equipoRepo.save(equipo);
    }

    @Override
    public Equipo update(Long id, Equipo equipoActualizado) {
        log.info("Buscando equipo para actualiza con el id {}", id);
        Equipo equipoExistente = equipoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));

        // Validamos el nombre
        validarNombre(equipoActualizado.getNombre());
        log.info("Validamos el nombre del equipo {}", equipoActualizado.getNombre());

        // Validamos que el nombre del equipo no exista en otro equipo y esté activo
        log.info("Verificando que el nombre no exista y no esté activo {}", equipoActualizado.getNombre());
        if(equipoRepo.existsByNombreAndIdNot(equipoActualizado.getNombre(), id)) {
            throw new RuntimeException("Ya existe otro equipo con el nombre: " + equipoActualizado.getNombre());
        }

        // si se cambia el grupo, validamos que exista y estpe activo
        Long nuevoGrupoId = equipoActualizado.getGrupo() != null ? equipoActualizado.getGrupo().getId() : null;
        Long grupoActualId = equipoExistente.getGrupo().getId();

        if (nuevoGrupoId != null && !nuevoGrupoId.equals(grupoActualId)) {
            Grupo grupo = validarGrupo(nuevoGrupoId);
            equipoExistente.setGrupo(grupo);
        }

        // si se cambia el capitan, validamos que exista y esté activo
        Long nuevoUsuarioId = equipoActualizado.getUser() != null ? equipoActualizado.getUser().getId() : null;
        Long usuarioActualId = equipoExistente.getUser().getId();

        if (nuevoUsuarioId != null && !nuevoUsuarioId.equals(usuarioActualId)) {
            Usuario usuario = validarUsuario(nuevoUsuarioId);

            // Validar que el nuevo capitán no sea capitán de otro equipo en el mismo grupo
            Long grupoIdFinal = nuevoGrupoId != null ? nuevoGrupoId : grupoActualId;
            validarUsuarioCapitanEnGrupo(nuevoUsuarioId, grupoIdFinal, id);

            equipoExistente.setUser(usuario);
        }

        equipoExistente.setNombre(equipoActualizado.getNombre());
        equipoExistente.setActivo(equipoActualizado.getActivo());

        return equipoRepo.save(equipoExistente);
    }

    @Override
    public Equipo asignarCapitan(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepo.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + equipoId));

        Usuario usuario = validarUsuario(usuarioId);

        // Validamos que el usuario no sea capitán de otro equipo en el mismo grupo
        validarUsuarioCapitanEnGrupo(usuarioId, equipo.getGrupo().getId(), equipoId);

        equipo.setUser(usuario);
        return equipoRepo.save(equipo);
    }

    @Override
    public void deleteLogical(Long id) {
        Equipo equipo = equipoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));

        // Verificar si tiene reservas asociadas
        if (equipoRepo.hasReservasAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar el equipo porque tiene reservas asociadas");
        }

        equipo.setActivo(false);
        equipoRepo.save(equipo);
    }

    @Override
    public Equipo findById(Long id) {
        return equipoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));
    }

    @Override
    public Equipo findByIdWithUsuarioAndGrupo(Long id) {
        return equipoRepo.findByIdWithUsuarioAndGrupo(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));
    }

    @Override
    public Equipo findByIdWithAllRelations(Long id) {
        return equipoRepo.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));
    }

    @Override
    public Equipo findByNombre(String nombre) {
        return equipoRepo.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con nombre: " + nombre));
    }

    @Override
    public List<Equipo> findAll() {
        return equipoRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Equipo> findAllWithUsuarioAndGrupo() {
        return equipoRepo.findAllWithUsuarioAndGrupo();
    }

    @Override
    public List<Equipo> findActivos() {
        return equipoRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Equipo> findActivosWithUsuarioAndGrupo() {
        return equipoRepo.findAllActivosWithUsuarioAndGrupo();
    }

    @Override
    public List<Equipo> findByGrupo(Long grupoId) {
        return equipoRepo.findByGrupoIdOrderByNombreAsc(grupoId);
    }

    @Override
    public List<Equipo> findActivosByGrupo(Long grupoId) {
        return equipoRepo.findActivosByGrupoIdOrderByNombreAsc(grupoId);
    }

    @Override
    public List<Equipo> findByUsuario(Long usuarioId) {
        return equipoRepo.findByUsuarioIdOrderByNombreAsc(usuarioId);
    }

    @Override
    public List<Equipo> findActivosByUsuario(Long usuarioId) {
        return equipoRepo.findActivosByUsuarioId(usuarioId);
    }

    @Override
    public List<Equipo> searchByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return equipoRepo.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Equipo> findByCategoria(Long categoriaId) {
        return equipoRepo.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Equipo> findActivosByCategoria(Long categoriaId) {
        return equipoRepo.findActivosByCategoriaId(categoriaId);
    }

    @Override
    public List<Equipo> findBySucursal(Long sucursalId) {
        return equipoRepo.findBySucursalId(sucursalId);
    }

    @Override
    public List<Equipo> findActivosBySucursal(Long sucursalId) {
        return equipoRepo.findActivosBySucursalId(sucursalId);
    }

    @Override
    public List<Object[]> getEquiposParaSelect() {
        return equipoRepo.findIdAndNombreByActivoTrue();
    }

    @Override
    public List<Object[]> getEquiposByGrupoParaSelect(Long grupoId) {
        return equipoRepo.findIdAndNombreByGrupoIdAndActivoTrue(grupoId);
    }

    @Override
    public List<Object[]> getEquiposByUsuarioParaSelect(Long usuarioId) {
        return equipoRepo.findIdAndNombreByUsuarioIdAndActivoTrue(usuarioId);
    }

    @Override
    public EstadisticasEquipoDTO getEstadisticas() {
        return EstadisticasEquipoDTO.builder()
                .total(equipoRepo.count())
                .activos(equipoRepo.countEquiposActivos())
                .inactivos(equipoRepo.countEquiposInactivos())
                .equiposPorGrupo(equipoRepo.countEquiposByGrupo())
                .equiposActivosPorGrupo(equipoRepo.countEquiposActivosByGrupo())
                .equiposPorUsuario(equipoRepo.countEquiposByUsuario())
                .equiposPorCategoria(equipoRepo.countEquiposByCategoria())
                .equiposPorSucursal(equipoRepo.countEquiposBySucursal())
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return equipoRepo.existsById(id);
    }

    @Override
    public boolean isActivo(Long id) {
        Boolean activo = equipoRepo.isEquipoActivo(id);
        if (activo == null) {
            throw new RuntimeException("Equipo no encontrado con ID: " + id);
        }
        return activo;
    }

    @Override
    public boolean hasCapitanAsignado(Long id) {
        Boolean tieneCapitan = equipoRepo.hasCapitanAsignado(id);
        if (tieneCapitan == null) {
            throw new RuntimeException("Equipo no encontrado con ID: " + id);
        }
        return tieneCapitan;
    }

    @Override
    public boolean isUsuarioCapitanEnGrupo(Long usuarioId, Long grupoId) {
        return equipoRepo.existsUsuarioCapitanEnGrupo(usuarioId, grupoId, null);
    }
}
