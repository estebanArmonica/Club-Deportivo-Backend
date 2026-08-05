package com.clubdeportivo.services.cancha;

import com.clubdeportivo.dtos.cancha.EstadisticasCanchaDTO;
import com.clubdeportivo.models.Cancha;

import java.util.List;

public interface ICanchaService {
    // Crud
    /**
     * Crear una nueva cancha
     */
    Cancha create(Cancha cancha);

    /**
     * Actualizar una cancha existente
     */
    Cancha update(Long id, Cancha canchaActualizada);

    /**
     * Eliminar una cancha (borrado lógico)
     */
    void delete(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar cancha por ID
     */
    Cancha findById(Long id);

    /**
     * Buscar cancha por ID con su sucursal
     */
    Cancha findByIdWithSucursal(Long id);

    /**
     * Buscar cancha por nombre
     */
    Cancha findByNombre(String nombre);

    /**
     * Buscar todas las canchas
     */
    List<Cancha> findAll();

    /**
     * Buscar todas las canchas con su sucursal
     */
    List<Cancha> findAllWithSucursal();

    /**
     * Buscar canchas disponibles
     */
    List<Cancha> findDisponibles();

    /**
     * Buscar canchas disponibles con su sucursal
     */
    List<Cancha> findDisponiblesWithSucursal();

    /**
     * Buscar canchas por sucursal
     */
    List<Cancha> findBySucursal(Long sucursalId);

    /**
     * Buscar canchas disponibles por sucursal
     */
    List<Cancha> findDisponiblesBySucursal(Long sucursalId);

    /**
     * Buscar canchas por tipo
     */
    List<Cancha> findByTipo(String tipo);

    /**
     * Buscar canchas disponibles por tipo
     */
    List<Cancha> findDisponiblesByTipo(String tipo);

    /**
     * Buscar canchas por nombre (búsqueda parcial)
     */
    List<Cancha> searchByNombre(String nombre);

    /**
     * Buscar canchas por capacidad entre dos valores
     */
    List<Cancha> findByCapacidadBetween(int capacidadMin, int capacidadMax);

    /**
     * Buscar canchas por sucursal y tipo
     */
    List<Cancha> findBySucursalAndTipo(Long sucursalId, String tipo);

    /**
     * Buscar canchas disponibles por sucursal y tipo
     */
    List<Cancha> findDisponiblesBySucursalAndTipo(Long sucursalId, String tipo);

    // ============================================================
    // MÉTODOS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener lista de canchas para combos (solo disponibles)
     */
    List<Object[]> getCanchasParaSelect();

    /**
     * Obtener lista de canchas disponibles por sucursal para combos
     */
    List<Object[]> getCanchasBySucursalParaSelect(Long sucursalId);

    /**
     * Obtener lista de canchas con tipo por sucursal para combos
     */
    List<Object[]> getCanchasConTipoBySucursalParaSelect(Long sucursalId);

    /**
     * Obtener tipos de cancha disponibles
     */
    List<String> getTiposDisponibles();

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de canchas
     */
    EstadisticasCanchaDTO getEstadisticas();

    // ============================================================
    // VALIDACIONES Y UTILIDADES
    // ============================================================

    /**
     * Verificar si una cancha existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si una cancha está disponible
     */
    boolean isDisponible(Long id);

    /**
     * Cambiar disponibilidad de una cancha
     */
    Cancha cambiarDisponibilidad(Long id, boolean disponible);
}
