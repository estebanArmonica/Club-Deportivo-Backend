package com.clubdeportivo.services.equipo;

import java.util.List;

import com.clubdeportivo.dtos.equipo.EstadisticasEquipoDTO;
import com.clubdeportivo.models.Equipo;

public interface IEquipoService {

    /**
     *
     * Creación de la logica CRUD
     */

    /**
     * Crear un nuevo equipo
     */
    Equipo create(Equipo equipo);

    /**
     * Actualizar un equipo existente
     */
    Equipo update(Long id, Equipo equipoActualizado);

    /**
     * Asignar capitán a un equipo
     */
    Equipo asignarCapitan(Long equipoId, Long usuarioId);

    /**
     * Eliminar un equipo (borrado lógico)
     */
    void deleteLogical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar equipo por ID
     */
    Equipo findById(Long id);

    /**
     * Buscar equipo por ID con su usuario y grupo
     */
    Equipo findByIdWithUsuarioAndGrupo(Long id);

    /**
     * Buscar equipo por ID con todas sus relaciones
     */
    Equipo findByIdWithAllRelations(Long id);

    /**
     * Buscar equipo por nombre
     */
    Equipo findByNombre(String nombre);

    /**
     * Buscar todos los equipos
     */
    List<Equipo> findAll();

    /**
     * Buscar todos los equipos con su usuario y grupo
     */
    List<Equipo> findAllWithUsuarioAndGrupo();

    /**
     * Buscar equipos activos
     */
    List<Equipo> findActivos();

    /**
     * Buscar equipos activos con su usuario y grupo
     */
    List<Equipo> findActivosWithUsuarioAndGrupo();

    /**
     * Buscar equipos por grupo
     */
    List<Equipo> findByGrupo(Long grupoId);

    /**
     * Buscar equipos activos por grupo
     */
    List<Equipo> findActivosByGrupo(Long grupoId);

    /**
     * Buscar equipos por usuario (capitán)
     */
    List<Equipo> findByUsuario(Long usuarioId);

    /**
     * Buscar equipos activos por usuario (capitán)
     */
    List<Equipo> findActivosByUsuario(Long usuarioId);

    /**
     * Buscar equipos por nombre (búsqueda parcial)
     */
    List<Equipo> searchByNombre(String nombre);

    /**
     * Buscar equipos por categoría (a través de grupo)
     */
    List<Equipo> findByCategoria(Long categoriaId);

    /**
     * Buscar equipos activos por categoría (a través de grupo)
     */
    List<Equipo> findActivosByCategoria(Long categoriaId);

    /**
     * Buscar equipos por sucursal (a través de grupo)
     */
    List<Equipo> findBySucursal(Long sucursalId);

    /**
     * Buscar equipos activos por sucursal (a través de grupo)
     */
    List<Equipo> findActivosBySucursal(Long sucursalId);

    // ============================================================
    // MÉTODOS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener lista de equipos para combos (solo activos)
     */
    List<Object[]> getEquiposParaSelect();

    /**
     * Obtener lista de equipos activos por grupo para combos
     */
    List<Object[]> getEquiposByGrupoParaSelect(Long grupoId);

    /**
     * Obtener lista de equipos activos por usuario para combos
     */
    List<Object[]> getEquiposByUsuarioParaSelect(Long usuarioId);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de equipos
     */
    EstadisticasEquipoDTO getEstadisticas();

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si un equipo existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si un equipo está activo
     */
    boolean isActivo(Long id);

    /**
     * Verificar si un equipo tiene capitán asignado
     */
    boolean hasCapitanAsignado(Long id);

    /**
     * Verificar si un usuario es capitán de algún equipo en un grupo específico
     */
    boolean isUsuarioCapitanEnGrupo(Long usuarioId, Long grupoId);
}
