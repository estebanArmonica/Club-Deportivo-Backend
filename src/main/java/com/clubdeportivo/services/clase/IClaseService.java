package com.clubdeportivo.services.clase;

import com.clubdeportivo.dtos.clase.EstadisticasClaseDTO;
import com.clubdeportivo.models.Clase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IClaseService {
    /**
     * Crear una nueva clase
     */
    Clase create(Clase clase);

    /**
     * Actualizar una clase existente
     */
    Clase update(Long id, Clase claseActualizada);

    /**
     * Cambiar estado de una clase
     */
    Clase cambiarEstado(Long id, String nuevoEstado);

    /**
     * Iniciar una clase (cambiar a 'en_curso')
     */
    Clase iniciar(Long id);

    /**
     * Completar una clase (cambiar a 'completada')
     */
    Clase completar(Long id);

    /**
     * Cancelar una clase (cambiar a 'cancelada')
     */
    Clase cancelar(Long id);

    /**
     * Eliminar una clase (borrado lógico)
     */
    void deleteLogical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar clase por ID
     */
    Clase findById(Long id);

    /**
     * Buscar clase por ID con su grupo
     */
    Clase findByIdWithGrupo(Long id);

    /**
     * Buscar clase por ID con todas sus relaciones
     */
    Clase findByIdWithAllRelations(Long id);

    /**
     * Buscar todas las clases
     */
    List<Clase> findAll();

    /**
     * Buscar todas las clases con su grupo
     */
    List<Clase> findAllWithGrupo();

    /**
     * Buscar clases activas
     */
    List<Clase> findActivas();

    /**
     * Buscar clases activas con su grupo
     */
    List<Clase> findActivasWithGrupo();

    /**
     * Buscar clases por estado
     */
    List<Clase> findByEstado(String estado);

    /**
     * Buscar clases por grupo
     */
    List<Clase> findByGrupo(Long grupoId);

    /**
     * Buscar clases activas por grupo
     */
    List<Clase> findActivasByGrupo(Long grupoId);

    /**
     * Buscar clases por grupo y fecha
     */
    List<Clase> findByGrupoAndFecha(Long grupoId, LocalDate fecha);

    /**
     * Buscar clases por grupo y rango de fechas
     */
    List<Clase> findByGrupoAndFechasBetween(Long grupoId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar clases por fecha
     */
    List<Clase> findByFecha(LocalDate fecha);

    /**
     * Buscar clases por fecha con su grupo
     */
    List<Clase> findByFechaWithGrupo(LocalDate fecha);

    /**
     * Buscar clases por rango de fechas
     */
    List<Clase> findByFechasBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar clases por categoría (a través de grupo)
     */
    List<Clase> findByCategoria(Long categoriaId);

    /**
     * Buscar clases activas por categoría (a través de grupo)
     */
    List<Clase> findActivasByCategoria(Long categoriaId);

    /**
     * Buscar clases por sucursal (a través de grupo)
     */
    List<Clase> findBySucursal(Long sucursalId);

    /**
     * Buscar clases activas por sucursal (a través de grupo)
     */
    List<Clase> findActivasBySucursal(Long sucursalId);

    /**
     * Buscar clases por club (a través de grupo -> sucursal)
     */
    List<Clase> findByClub(Long clubId);

    /**
     * Buscar clases activas por club (a través de grupo -> sucursal)
     */
    List<Clase> findActivasByClub(Long clubId);

    // ============================================================
    // VALIDACIONES Y UTILIDADES
    // ============================================================

    /**
     * Verificar si una clase existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si una clase está activa
     */
    boolean isActiva(Long id);

    /**
     * Verificar si existe una clase en el mismo horario
     */
    boolean existsClaseEnHorario(Long grupoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de clases
     */
    EstadisticasClaseDTO getEstadisticas();
}
