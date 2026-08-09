package com.clubdeportivo.services.encargadosucursal;

import com.clubdeportivo.dtos.encargadosucursal.EstadisticasEncargadoSucursalDTO;
import com.clubdeportivo.models.EncargadoSucursal;

import java.util.List;

public interface IEncargadoSucursalService {
    /**
     * Crear una nueva asignación de encargado a sucursal
     */
    EncargadoSucursal create(EncargadoSucursal encargadoSucursal);

    /**
     * Actualizar una asignación existente
     */
    EncargadoSucursal update(Long id, EncargadoSucursal encargadoSucursalActualizado);

    /**
     * Desactivar una asignación (borrado lógico)
     */
    void deleteLogical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar asignación por ID
     */
    EncargadoSucursal findById(Long id);

    /**
     * Buscar asignación por ID con su usuario y sucursal
     */
    EncargadoSucursal findByIdWithUsuarioAndSucursal(Long id);

    /**
     * Buscar asignación por ID con todas sus relaciones
     */
    EncargadoSucursal findByIdWithAllRelations(Long id);

    /**
     * Buscar todas las asignaciones
     */
    List<EncargadoSucursal> findAll();

    /**
     * Buscar todas las asignaciones con su usuario y sucursal
     */
    List<EncargadoSucursal> findAllWithUsuarioAndSucursal();

    /**
     * Buscar asignaciones activas
     */
    List<EncargadoSucursal> findActivos();

    /**
     * Buscar asignaciones activas con su usuario y sucursal
     */
    List<EncargadoSucursal> findActivosWithUsuarioAndSucursal();

    /**
     * Buscar asignaciones por sucursal
     */
    List<EncargadoSucursal> findBySucursal(Long sucursalId);

    /**
     * Buscar asignaciones activas por sucursal
     */
    List<EncargadoSucursal> findActivosBySucursal(Long sucursalId);

    /**
     * Buscar asignaciones por usuario (encargado)
     */
    List<EncargadoSucursal> findByUsuario(Long usuarioId);

    /**
     * Buscar asignaciones activas por usuario (encargado)
     */
    List<EncargadoSucursal> findActivosByUsuario(Long usuarioId);

    /**
     * Buscar asignaciones por turno
     */
    List<EncargadoSucursal> findByTurno(String turno);

    /**
     * Buscar asignaciones por sucursal y turno
     */
    List<EncargadoSucursal> findBySucursalAndTurno(Long sucursalId, String turno);

    /**
     * Buscar asignaciones activas por sucursal y turno
     */
    List<EncargadoSucursal> findActivosBySucursalAndTurno(Long sucursalId, String turno);

    /**
     * Buscar asignaciones por club (a través de sucursal)
     */
    List<EncargadoSucursal> findByClub(Long clubId);

    /**
     * Buscar asignaciones activas por club (a través de sucursal)
     */
    List<EncargadoSucursal> findActivosByClub(Long clubId);

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si una asignación existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si una asignación está activa
     */
    boolean isActivo(Long id);

    /**
     * Verificar si un usuario ya está asignado a una sucursal con un turno específico
     */
    boolean existsUsuarioAsignadoSucursalTurno(Long usuarioId, Long sucursalId, String turno);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de asignaciones
     */
    EstadisticasEncargadoSucursalDTO getEstadisticas();
}
