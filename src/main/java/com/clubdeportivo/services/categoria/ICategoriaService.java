package com.clubdeportivo.services.categoria;

import java.util.List;

import com.clubdeportivo.models.Categoria;
import com.clubdeportivo.dtos.categoria.EstadisticasCategoriaDTO;
public interface ICategoriaService {

    /**
      * Crear una nueva categoría
     */
    Categoria create(Categoria categoria);

    /**
      * Actualizar una categoría existente
     */
    Categoria update(Long id, Categoria categoriaActualizada);

    /**
     * Eliminar una categoría (borrado lógico)
     */
    void deleteLogical(Long id);

    /**
     * Eliminar una categoría (borrado físico)
     */
    void deletePhysical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar categoría por ID
     */
    Categoria findById(Long id);

    /**
     * Buscar categoría por ID con su deporte
     */
    Categoria findByIdWithDeporte(Long id);

    /**
     * Buscar categoría por nombre
     */
    Categoria findByNombre(String nombre);

    /**
     * Buscar todas las categorías
     */
    List<Categoria> findAll();

    /**
     * Buscar todas las categorías con su deporte
     */
    List<Categoria> findAllWithDeporte();

    /**
     * Buscar categorías activas
     */
    List<Categoria> findActivas();

    /**
     * Buscar categorías activas con su deporte
     */
    List<Categoria> findActivasWithDeporte();

    /**
     * Buscar categorías por deporte
     */
    List<Categoria> findByDeporte(Long deporteId);

    /**
     * Buscar categorías activas por deporte
     */
    List<Categoria> findActivasByDeporte(Long deporteId);

    /**
     * Buscar categorías por nombre (búsqueda parcial)
     */
    List<Categoria> searchByNombre(String nombre);

    /**
     * Buscar categorías por rango de edad
     */
    List<Categoria> findByEdad(int edad);

    /**
     * Buscar categorías por deporte y rango de edad
     */
    List<Categoria> findByDeporteAndEdad(Long deporteId, int edad);

    // ============================================================
    // MÉTODOS PARA SELECTS/DROPDOWNS
    // ============================================================

    /**
     * Obtener lista de categorías para combos (solo activas)
     */
    List<Object[]> getCategoriasParaSelect();

    /**
     * Obtener lista de categorías activas por deporte para combos
     */
    List<Object[]> getCategoriasByDeporteParaSelect(Long deporteId);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de categorías
     */
    EstadisticasCategoriaDTO getEstadisticas();

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si una categoría existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si una categoría está activa
     */
    boolean isActiva(Long id);
}
