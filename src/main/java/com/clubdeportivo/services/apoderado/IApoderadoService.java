package com.clubdeportivo.services.apoderado;

import com.clubdeportivo.models.Apoderado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IApoderadoService {
    /**
     * Crear un nuevo apoderado
     */
    Apoderado create(Apoderado apoderado);

    /**
     * Actualizar un apoderado existente
     */
    Apoderado update(Long id, Apoderado apoderadoActualizado);

    /**
     * Eliminar un apoderado (borrado físico)
     */
    void deletePhysical(Long id);

    // ============================================================
    // BÚSQUEDAS
    // ============================================================

    /**
     * Buscar apoderado por ID
     */
    Apoderado findById(Long id);

    /**
     * Buscar apoderado por ID con sus alumnos
     */
    Apoderado findByIdWithAlumnos(Long id);

    /**
     * Buscar apoderado por contacto
     */
    Apoderado findByContacto(String contacto);

    /**
     * Buscar todos los apoderados
     */
    List<Apoderado> findAll();

    /**
     * Buscar todos los apoderados con paginación
     */
    Page<Apoderado> findAll(Pageable pageable);

    /**
     * Buscar todos los apoderados con sus alumnos
     */
    List<Apoderado> findAllWithAlumnos();

    /**
     * Buscar apoderados por parentesco
     */
    List<Apoderado> findByParentesco(String parentesco);

    /**
     * Buscar apoderados por contacto (búsqueda parcial)
     */
    List<Apoderado> searchByContacto(String contacto);

    /**
     * Buscar apoderados por alumno
     */
    List<Apoderado> findByAlumno(Long alumnoId);

    /**
     * Buscar apoderados por RUT de alumno
     */
    List<Apoderado> findByAlumnoRut(String rut);

    /**
     * Buscar apoderados por grupo (a través de alumnos)
     */
    List<Apoderado> findByGrupo(Long grupoId);

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Contar apoderados por parentesco
     */
    List<Object[]> countApoderadosByParentesco();

    /**
     * Buscar apoderados con más alumnos
     */
    List<Object[]> findApoderadosConMasAlumnos();

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si un apoderado existe
     */
    boolean existsById(Long id);

    /**
     * Verificar si existe un apoderado por contacto
     */
    boolean existsByContacto(String contacto);

    /**
     * Verificar si un apoderado tiene alumnos
     */
    boolean hasAlumnos(Long apoderadoId);
}
