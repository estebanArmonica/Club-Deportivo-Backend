package com.clubdeportivo.services.profesorgrupo.impl;

import com.clubdeportivo.dtos.profesorgrupo.EstadisticasProfesorGrupoDTO;
import com.clubdeportivo.models.Grupo;
import com.clubdeportivo.models.ProfesorGrupo;
import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IGrupoRepository;
import com.clubdeportivo.repositories.IProfesorGrupoRepository;
import com.clubdeportivo.repositories.IUsuarioRepository;
import com.clubdeportivo.services.profesorgrupo.IProfesorGrupoService;
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
public class ProfesorGrupoServiceImpl implements IProfesorGrupoService {
    private final IProfesorGrupoRepository profesorGrupoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IGrupoRepository grupoRepository;

    /**
     * Validaciones privadas
     */
    private Usuario validarUsuarioProfesor(Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("La asignación debe tener un usuario (profesor) asociado");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        if (!usuario.getIsActive()) {
            throw new RuntimeException("No se puede asignar un usuario inactivo como profesor");
        }

        return usuario;
    }

    private Grupo validarGrupo(Long grupoId) {
        if (grupoId == null) {
            throw new RuntimeException("La asignación debe tener un grupo asociado");
        }

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + grupoId));

        if (!grupo.getActivo()) {
            throw new RuntimeException("No se puede asignar un profesor a un grupo inactivo");
        }

        return grupo;
    }

    private void validarAsignacionUnica(Long usuarioId, Long grupoId, Long profesorGrupoId) {
        if (profesorGrupoRepository.existsUsuarioAsignadoGrupo(usuarioId, grupoId, profesorGrupoId)) {
            throw new RuntimeException("El usuario ya está asignado como profesor de este grupo");
        }
    }


    /**
     * CRUD de logica de negocio
     */

    /**
     * Creamos una nueva entidad del profesor del grupo
     * @param profesorGrupo parametro
     * @return retorna un objeto
     */
    @Override
    public ProfesorGrupo create(ProfesorGrupo profesorGrupo) {
        // validamos el usuario (profesor)
        Long usuarioId =  profesorGrupo.getUser() != null ? profesorGrupo.getUser().getId() : null;
        Usuario usuario = validarUsuarioProfesor(usuarioId);

        // validamos el grupo
        Long grupoId = profesorGrupo.getGrupo() != null ? profesorGrupo.getGrupo().getId() : null;
        Grupo grupo = validarGrupo(grupoId);

        // validamos que el usuario no esté asignado a un grupo
        validarAsignacionUnica(usuarioId, grupoId, null);

        // Si no se especifica fecha de creación, usamos la actual
        if(profesorGrupo.getFechCreacion() == null) {
            profesorGrupo.setFechCreacion(LocalDate.now());
        }

        profesorGrupo.setUser(usuario);
        profesorGrupo.setGrupo(grupo);

        return profesorGrupoRepository.save(profesorGrupo);
    }

    @Override
    public ProfesorGrupo update(Long id, ProfesorGrupo profesorGrupoActualizado) {
        ProfesorGrupo profesorGrupoExistente = profesorGrupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        // Si se cambia el usuario, validar que exista y esté activo
        Long nuevoUsuarioId = profesorGrupoActualizado.getUser() != null ?
                profesorGrupoActualizado.getUser().getId() : null;
        Long usuarioActualId = profesorGrupoExistente.getUser().getId();

        if (nuevoUsuarioId != null && !nuevoUsuarioId.equals(usuarioActualId)) {
            Usuario usuario = validarUsuarioProfesor(nuevoUsuarioId);

            // Verificar que el nuevo usuario no esté ya asignado a este grupo
            Long grupoId = profesorGrupoActualizado.getGrupo() != null &&
                    profesorGrupoActualizado.getGrupo().getId() != null ?
                    profesorGrupoActualizado.getGrupo().getId() :
                    profesorGrupoExistente.getGrupo().getId();

            validarAsignacionUnica(nuevoUsuarioId, grupoId, id);

            profesorGrupoExistente.setUser(usuario);
        }

        // Si se cambia el grupo, validar que exista y esté activo
        Long nuevoGrupoId = profesorGrupoActualizado.getGrupo() != null ?
                profesorGrupoActualizado.getGrupo().getId() : null;
        Long grupoActualId = profesorGrupoExistente.getGrupo().getId();

        if (nuevoGrupoId != null && !nuevoGrupoId.equals(grupoActualId)) {
            Grupo grupo = validarGrupo(nuevoGrupoId);

            // Verificar que el usuario no esté ya asignado al nuevo grupo
            Long usuarioId = profesorGrupoActualizado.getUser() != null &&
                    profesorGrupoActualizado.getUser().getId() != null ?
                    profesorGrupoActualizado.getUser().getId() :
                    profesorGrupoExistente.getUser().getId();

            validarAsignacionUnica(usuarioId, nuevoGrupoId, id);

            profesorGrupoExistente.setGrupo(grupo);
        }

        profesorGrupoExistente.setActivo(profesorGrupoActualizado.getActivo());

        return profesorGrupoRepository.save(profesorGrupoExistente);
    }

    @Override
    public void deleteLogical(Long id) {
        ProfesorGrupo profesorGrupo = profesorGrupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        profesorGrupo.setActivo(false);
        profesorGrupoRepository.save(profesorGrupo);
    }

    @Override
    public ProfesorGrupo findById(Long id) {
        return profesorGrupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));
    }

    @Override
    public ProfesorGrupo findByIdWithUsuarioAndGrupo(Long id) {
        return profesorGrupoRepository.findByIdWithUsuarioAndGrupo(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));
    }

    @Override
    public ProfesorGrupo findByIdWithAllRelations(Long id) {
        return profesorGrupoRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));
    }

    @Override
    public List<ProfesorGrupo> findAll() {
        return profesorGrupoRepository.findAllByOrderByFechCreacionDesc();
    }

    @Override
    public List<ProfesorGrupo> findAllWithUsuarioAndGrupo() {
        return profesorGrupoRepository.findAllWithUsuarioAndGrupo();
    }

    @Override
    public List<ProfesorGrupo> findActivos() {
        return profesorGrupoRepository.findByActivoTrueOrderByFechCreacionDesc();
    }

    @Override
    public List<ProfesorGrupo> findActivosWithUsuarioAndGrupo() {
        return profesorGrupoRepository.findAllActivosWithUsuarioAndGrupo();
    }

    @Override
    public List<ProfesorGrupo> findByGrupo(Long grupoId) {
        return profesorGrupoRepository.findByGrupoIdOrderByFechCreacionDesc(grupoId);
    }

    @Override
    public List<ProfesorGrupo> findActivosByGrupo(Long grupoId) {
        return profesorGrupoRepository.findActivosByGrupoId(grupoId);
    }

    @Override
    public List<ProfesorGrupo> findByUsuario(Long usuarioId) {
        return profesorGrupoRepository.findByUsuarioIdOrderByFechCreacionDesc(usuarioId);
    }

    @Override
    public List<ProfesorGrupo> findActivosByUsuario(Long usuarioId) {
        return profesorGrupoRepository.findActivosByUsuarioId(usuarioId);
    }

    @Override
    public List<ProfesorGrupo> findByCategoria(Long categoriaId) {
        return profesorGrupoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<ProfesorGrupo> findActivosByCategoria(Long categoriaId) {
        return profesorGrupoRepository.findActivosByCategoriaId(categoriaId);
    }

    @Override
    public List<ProfesorGrupo> findBySucursal(Long sucursalId) {
        return profesorGrupoRepository.findBySucursalId(sucursalId);
    }

    @Override
    public List<ProfesorGrupo> findActivosBySucursal(Long sucursalId) {
        return profesorGrupoRepository.findActivosBySucursalId(sucursalId);
    }

    @Override
    public List<ProfesorGrupo> findByClub(Long clubId) {
        return profesorGrupoRepository.findByClubId(clubId);
    }

    @Override
    public List<ProfesorGrupo> findActivosByClub(Long clubId) {
        return profesorGrupoRepository.findActivosByClubId(clubId);
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @Override
    public boolean existsById(Long id) {
        return profesorGrupoRepository.existsById(id);
    }

    @Override
    public boolean isActivo(Long id) {
        Boolean activo = profesorGrupoRepository.isRegistroActivo(id);
        if (activo == null) {
            throw new RuntimeException("Asignación no encontrada con ID: " + id);
        }
        return activo;
    }

    @Override
    public boolean isUsuarioAsignadoGrupo(Long usuarioId, Long grupoId) {
        return profesorGrupoRepository.existsUsuarioAsignadoGrupo(usuarioId, grupoId, null);
    }

    @Override
    public boolean hasProfesoresActivos(Long grupoId) {
        return profesorGrupoRepository.hasProfesoresActivos(grupoId);
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    @Override
    public EstadisticasProfesorGrupoDTO getEstadisticas() {
        return EstadisticasProfesorGrupoDTO.builder()
                .total(profesorGrupoRepository.count())
                .activos(profesorGrupoRepository.countRegistrosActivos())
                .inactivos(profesorGrupoRepository.countRegistrosInactivos())
                .asignacionesPorGrupo(profesorGrupoRepository.countRegistrosByGrupo())
                .asignacionesActivasPorGrupo(profesorGrupoRepository.countRegistrosActivosByGrupo())
                .asignacionesPorUsuario(profesorGrupoRepository.countRegistrosByUsuario())
                .asignacionesActivasPorUsuario(profesorGrupoRepository.countRegistrosActivosByUsuario())
                .asignacionesPorCategoria(profesorGrupoRepository.countRegistrosByCategoria())
                .asignacionesPorSucursal(profesorGrupoRepository.countRegistrosBySucursal())
                .asignacionesPorMes(profesorGrupoRepository.countRegistrosByMes())
                .build();
    }
}
