package com.clubdeportivo.controllers;

import com.clubdeportivo.models.Apoderado;
import com.clubdeportivo.services.apoderado.IApoderadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/apoderados")
@RequiredArgsConstructor
@Tag(name = "Apoderados", description = "Endpoints for Apoderados")
public class ApoderadoController {
    private IApoderadoService apoderadoService;

    /**
     * Crear un nuevo apoderado
     */
    @PostMapping("/create-apoderado")
    public ResponseEntity<Apoderado> create(@Valid @RequestBody Apoderado apoderado) {
        Apoderado created = apoderadoService.create(apoderado);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualizar un apoderado existente
     */
    @PutMapping("/update-apoderado/{id}")
    public ResponseEntity<Apoderado> update(
            @PathVariable Long id,
            @Valid @RequestBody Apoderado apoderado) {
        Apoderado updated = apoderadoService.update(id, apoderado);
        return ResponseEntity.ok(updated);
    }

    /**
     * Buscar apoderado por ID
     */
    @GetMapping("/list-apoderado/{id}")
    public ResponseEntity<Apoderado> getById(@PathVariable Long id) {
        Apoderado apoderado = apoderadoService.findByIdWithAlumnos(id);
        return ResponseEntity.ok(apoderado);
    }

    /**
     * Buscar apoderado por contacto
     */
    @GetMapping("/contacto/{contacto}")
    public ResponseEntity<Apoderado> getByContacto(@PathVariable String contacto) {
        Apoderado apoderado = apoderadoService.findByContacto(contacto);
        return ResponseEntity.ok(apoderado);
    }

    /**
     * Listar todos los apoderados
     */
    @GetMapping("/list-all")
    public ResponseEntity<List<Apoderado>> getAll() {
        return ResponseEntity.ok(apoderadoService.findAllWithAlumnos());
    }

    /**
     * Listar todos los apoderados con paginación
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Apoderado>> getAllPage(
            @PageableDefault(size = 10, sort = "parentesco") Pageable pageable) {
        return ResponseEntity.ok(apoderadoService.findAll(pageable));
    }

    /**
     * Buscar apoderados por parentesco
     */
    @GetMapping("/parentesco/{parentesco}")
    public ResponseEntity<List<Apoderado>> getByParentesco(@PathVariable String parentesco) {
        return ResponseEntity.ok(apoderadoService.findByParentesco(parentesco));
    }

    /**
     * Buscar apoderados por contacto (búsqueda parcial)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Apoderado>> searchByContacto(@RequestParam String contacto) {
        return ResponseEntity.ok(apoderadoService.searchByContacto(contacto));
    }

    /**
     * Buscar apoderados por alumno
     */
    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Apoderado>> getByAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(apoderadoService.findByAlumno(alumnoId));
    }

    /**
     * Buscar apoderados por RUT de alumno
     */
    @GetMapping("/alumno-rut/{rut}")
    public ResponseEntity<List<Apoderado>> getByAlumnoRut(@PathVariable String rut) {
        return ResponseEntity.ok(apoderadoService.findByAlumnoRut(rut));
    }

    /**
     * Buscar apoderados por grupo
     */
    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Apoderado>> getByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(apoderadoService.findByGrupo(grupoId));
    }

    /**
     * Eliminar un apoderado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        apoderadoService.deletePhysical(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Contar apoderados por parentesco
     */
    @GetMapping("/estadisticas/parentesco")
    public ResponseEntity<List<Object[]>> getCountByParentesco() {
        return ResponseEntity.ok(apoderadoService.countApoderadosByParentesco());
    }

    /**
     * Obtener apoderados con más alumnos
     */
    @GetMapping("/top-alumnos")
    public ResponseEntity<List<Object[]>> getApoderadosConMasAlumnos() {
        return ResponseEntity.ok(apoderadoService.findApoderadosConMasAlumnos());
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si existe un apoderado por ID
     */
    @GetMapping("/validar/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(apoderadoService.existsById(id));
    }

    /**
     * Verificar si existe un apoderado por contacto
     */
    @GetMapping("/validar/contacto")
    public ResponseEntity<Boolean> existsByContacto(@RequestParam String contacto) {
        return ResponseEntity.ok(apoderadoService.existsByContacto(contacto));
    }

    /**
     * Verificar si un apoderado tiene alumnos
     */
    @GetMapping("/validar/{id}/has-alumnos")
    public ResponseEntity<Boolean> hasAlumnos(@PathVariable Long id) {
        return ResponseEntity.ok(apoderadoService.hasAlumnos(id));
    }
}
