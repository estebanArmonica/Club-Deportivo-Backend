package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.asistencia.EstadisticasAsistenciaDTO;
import com.clubdeportivo.models.Asistencia;
import com.clubdeportivo.services.asistencia.IAsistenciaService;
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
@RequestMapping("api/v1/asistencia")
@RequiredArgsConstructor
@Tag(name = "Asistencias", description = "Endpoints for Asistencias")
public class AsistenciaController {
    private IAsistenciaService asistenciaService;

    // metodo GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Asistencia>> getAll() {
        return ResponseEntity.ok(asistenciaService.findAllWithClaseAndPago());
    }

    @GetMapping("/list-asistencia/{id}")
    public ResponseEntity<Asistencia> getById(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.findByIdWithAllRelations(id));
    }

    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<Asistencia>> getByClase(@PathVariable Long claseId) {
        return ResponseEntity.ok(asistenciaService.findByClase(claseId));
    }

    @GetMapping("/clase/{claseId}/presentes")
    public ResponseEntity<List<Asistencia>> getPresentesByClase(@PathVariable Long claseId) {
        return ResponseEntity.ok(asistenciaService.findPresentesByClase(claseId));
    }

    @GetMapping("/clase/{claseId}/ausentes")
    public ResponseEntity<List<Asistencia>> getAusentesByClase(@PathVariable Long claseId) {
        return ResponseEntity.ok(asistenciaService.findAusentesByClase(claseId));
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Asistencia>> getByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(asistenciaService.findByGrupo(grupoId));
    }

    @GetMapping("/pago/{pagoId}")
    public ResponseEntity<List<Asistencia>> getByPago(@PathVariable Long pagoId) {
        return ResponseEntity.ok(asistenciaService.findByPago(pagoId));
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<List<Asistencia>> getByAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(asistenciaService.findByAlumno(alumnoId));
    }

    @GetMapping("/alumno/{alumnoId}/presentes")
    public ResponseEntity<List<Asistencia>> getPresentesByAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(asistenciaService.findPresentesByAlumno(alumnoId));
    }

    @GetMapping("/alumno/{alumnoId}/ausentes")
    public ResponseEntity<List<Asistencia>> getAusentesByAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(asistenciaService.findAusentesByAlumno(alumnoId));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Asistencia>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(asistenciaService.findByFecha(fecha));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Asistencia>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(asistenciaService.findByFechasBetween(inicio, fin));
    }

    // metodo POST
    @PostMapping("/create-asistencia")
    public ResponseEntity<Asistencia> create(@RequestBody Asistencia asistencia) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(asistenciaService.create(asistencia));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Asistencia> registrarAsistencia(@RequestParam Long claseId,@RequestParam Long pagoId,@RequestParam Boolean asistio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(asistenciaService.registrarAsistencia(claseId, pagoId, asistio));
    }

    // metodo PUT
    @PutMapping("/update-asistencia/{id}")
    public ResponseEntity<Asistencia> update(@PathVariable Long id, @RequestBody Asistencia asistencia) {
        return ResponseEntity.ok(asistenciaService.update(id, asistencia));
    }

    // metodo PATCH
    @PatchMapping("/{id}/presente")
    public ResponseEntity<Asistencia> marcarPresente(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.marcarPresente(id));
    }

    @PatchMapping("/{id}/ausente")
    public ResponseEntity<Asistencia> marcarAusente(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.marcarAusente(id));
    }

    // metodo DELETE
    @DeleteMapping("/delete-asistencia/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        asistenciaService.deletePhysical(id);
        return ResponseEntity.noContent().build();
    }

    // Validaciones
    @GetMapping("/validar/existe")
    public ResponseEntity<Boolean> existsByClaseAndPago(@RequestParam Long claseId,@RequestParam Long pagoId) {
        return ResponseEntity.ok(asistenciaService.existsByClaseAndPago(claseId, pagoId));
    }

    // Estadisticas
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasAsistenciaDTO> getEstadisticas() {
        return ResponseEntity.ok(asistenciaService.getEstadisticas());
    }
}
