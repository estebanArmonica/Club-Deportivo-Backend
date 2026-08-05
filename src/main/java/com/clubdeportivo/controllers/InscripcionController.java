package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.inscripcion.EstadisticasInscripcionDTO;
import com.clubdeportivo.models.Inscripcion;
import com.clubdeportivo.services.inscripcion.IInscripcionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/inscripciones")
@RequiredArgsConstructor
@Tag(name = "Inscripciones", description = "Endpoints for inscripciones")
public class InscripcionController {
    private IInscripcionService inscripcionService;

    // Metodo GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Inscripcion>> getAll() {
        return ResponseEntity.ok(inscripcionService.findAllWithAlumnoAndGrupo());
    }

    @GetMapping("/list-inscripcion/{id}")
    public ResponseEntity<Inscripcion> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.findByIdWithAllRelations(id));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Inscripcion>> getActivas() {
        return ResponseEntity.ok(inscripcionService.findActivasWithAlumnoAndGrupo());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Inscripcion>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(inscripcionService.findByEstado(estado));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Inscripcion>> getByAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(inscripcionService.findActivasByAlumno(alumnoId));
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Inscripcion>> getByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(inscripcionService.findActivasByGrupo(grupoId));
    }

    @GetMapping("/apoderado/{apoderadoId}")
    public ResponseEntity<List<Inscripcion>> getByApoderado(@PathVariable Long apoderadoId) {
        return ResponseEntity.ok(inscripcionService.findActivasByApoderado(apoderadoId));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Inscripcion>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(inscripcionService.findActivasByCategoria(categoriaId));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Inscripcion>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(inscripcionService.findActivasBySucursal(sucursalId));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Inscripcion>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(inscripcionService.findByFechasInscripcionBetween(inicio, fin));
    }

    @PostMapping("/create-inscripcion")
    public ResponseEntity<Inscripcion> create(@RequestBody Inscripcion inscripcion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.create(inscripcion));
    }

    @PutMapping("/update-inscripcion/{id}")
    public ResponseEntity<Inscripcion> update(@PathVariable Long id, @RequestBody Inscripcion inscripcion) {
        return ResponseEntity.ok(inscripcionService.update(id, inscripcion));
    }

    @PatchMapping("/cambiar/{id}/estado")
    public ResponseEntity<Inscripcion> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(inscripcionService.cambiarEstado(id, estado));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Inscripcion> activar(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.activar(id));
    }

    @PatchMapping("/sus/{id}/suspender")
    public ResponseEntity<Inscripcion> suspender(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.suspender(id));
    }

    @PatchMapping("/final/{id}/finalizar")
    public ResponseEntity<Inscripcion> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.finalizar(id));
    }

    @DeleteMapping("/delete-inscripcion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscripcionService.deletePhysical(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @GetMapping("/validar/alumno-grupo")
    public ResponseEntity<Boolean> isAlumnoInscritoEnGrupo(
            @RequestParam Long alumnoId,
            @RequestParam Long grupoId) {
        return ResponseEntity.ok(inscripcionService.isAlumnoInscritoEnGrupo(alumnoId, grupoId));
    }

    @GetMapping("/validar/grupo/{grupoId}/inscripciones-activas")
    public ResponseEntity<Boolean> hasInscripcionesActivas(@PathVariable Long grupoId) {
        return ResponseEntity.ok(inscripcionService.hasInscripcionesActivas(grupoId));
    }

    @GetMapping("/validar/alumno/{alumnoId}/inscripciones-activas")
    public ResponseEntity<Boolean> hasInscripcionesActivasByAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(inscripcionService.hasInscripcionesActivasByAlumno(alumnoId));
    }

    @GetMapping("/validar/{id}/activa")
    public ResponseEntity<Boolean> isInscripcionActiva(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.isInscripcionActiva(id));
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasInscripcionDTO> getEstadisticas() {
        return ResponseEntity.ok(inscripcionService.getEstadisticas());
    }
}
