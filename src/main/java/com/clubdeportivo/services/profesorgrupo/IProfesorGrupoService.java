package com.clubdeportivo.services.profesorgrupo;

import java.time.LocalDate;
import java.util.List;

import com.clubdeportivo.dtos.profesorgrupo.EstadisticasProfesorGrupoDTO;
import com.clubdeportivo.models.ProfesorGrupo;
public interface IProfesorGrupoService {

    /**
     * CRUD Básico
     */

    /**
     * Crear una nueva asignación de profesor a grupo
     */
    ProfesorGrupo create(ProfesorGrupo profesorGrupo);

    /**
     * Actualizar una asignación existente
     */
    ProfesorGrupo update(Long id, ProfesorGrupo profesorGrupoActualizado);

    /**
     * Desactivar una asignación (borrado lógico)
     */
    void deleteLogical(Long id);

    /**
     * Busquedas
     */
    /**
     * Buscar asignación por ID
     */
    ProfesorGrupo findById(Long id);

    /**
     * Buscar asignación por ID con su usuario y grupo
     */
    ProfesorGrupo findByIdWithUsuarioAndGrupo(Long id);

    /**
     * Buscar asignación por ID con todas sus relaciones
     */
    ProfesorGrupo findByIdWithAllRelations(Long id);

    /**
     * Buscar todas las asignaciones
     */
    List<ProfesorGrupo> findAll();

    /**
     * Buscar todas las asignaciones con su usuario y grupo
     */
    List<ProfesorGrupo> findAllWithUsuarioAndGrupo();

    /**
     * Buscar asignaciones activas
     */
    List<ProfesorGrupo> findActivos();

    /**
     * Buscar asignaciones activas con su usuario y grupo
     */
    List<ProfesorGrupo> findActivosWithUsuarioAndGrupo();

    /**
     * Buscar asignaciones por grupo
     */
    List<ProfesorGrupo> findByGrupo(Long grupoId);

    /**
     * Buscar asignaciones activas por grupo
     */
    List<ProfesorGrupo> findActivosByGrupo(Long grupoId);

    /**
     * Buscar asignaciones por usuario (profesor)
     */
    List<ProfesorGrupo> findByUsuario(Long usuarioId);

    /**
     * Buscar asignaciones activas por usuario (profesor)
     */
    List<ProfesorGrupo> findActivosByUsuario(Long usuarioId);

    /**
     * Buscar asignaciones por categoría (a través de grupo)
     */
    List<ProfesorGrupo> findByCategoria(Long categoriaId);

    /**
     * Buscar asignaciones activas por categoría (a través de grupo)
     */
    List<ProfesorGrupo> findActivosByCategoria(Long categoriaId);

    /**
     * Buscar asignaciones por sucursal (a través de grupo)
     */
    List<ProfesorGrupo> findBySucursal(Long sucursalId);

    /**
     * Buscar asignaciones activas por sucursal (a través de grupo)
     */
    List<ProfesorGrupo> findActivosBySucursal(Long sucursalId);

    /**
     * Buscar asignaciones por club (a través de grupo -> sucursal)
     */
    List<ProfesorGrupo> findByClub(Long clubId);

    /**
     * Buscar asignaciones activas por club (a través de grupo -> sucursal)
     */
    List<ProfesorGrupo> findActivosByClub(Long clubId);

    /**
     * Validaciones
     */

    /**
     * Verificar si una asignación existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si una asignación está activa
     */
    boolean isActivo(Long id);

    /**
     * Verificar si un usuario ya está asignado a un grupo
     */
    boolean isUsuarioAsignadoGrupo(Long usuarioId, Long grupoId);

    /**
     * Verificar si un grupo tiene profesores activos
     */
    boolean hasProfesoresActivos(Long grupoId);

    /**
     * Estadisticas
     */

    /**
     * Obtener estadísticas de asignaciones
     */
    EstadisticasProfesorGrupoDTO getEstadisticas();
}
