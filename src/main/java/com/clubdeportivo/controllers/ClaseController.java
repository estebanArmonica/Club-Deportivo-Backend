package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.clase.EstadisticasClaseDTO;
import com.clubdeportivo.models.Clase;
import com.clubdeportivo.services.clase.IClaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/clases")
@RequiredArgsConstructor
@Tag(name = "Clases", description = "Endpoints for clases")
public class ClaseController {
    private IClaseService claseService;

    // Metodo GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Clase>> getAll() {
        return ResponseEntity.ok(claseService.findAllWithGrupo());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Clase>> getActivas() {
        return ResponseEntity.ok(claseService.findActivasWithGrupo());
    }

    @GetMapping("/list-clase/{id}")
    public ResponseEntity<Clase> getById(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.findByIdWithAllRelations(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Clase>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(claseService.findByEstado(estado));
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Clase>> getByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(claseService.findActivasByGrupo(grupoId));
    }

    @GetMapping("/grupo/{grupoId}/fecha")
    public ResponseEntity<List<Clase>> getByGrupoAndFecha(
            @PathVariable Long grupoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(claseService.findByGrupoAndFecha(grupoId, fecha));
    }

    @GetMapping("/grupo/{grupoId}/fechas")
    public ResponseEntity<List<Clase>> getByGrupoAndFechas(
            @PathVariable Long grupoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(claseService.findByGrupoAndFechasBetween(grupoId, inicio, fin));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Clase>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(claseService.findByFechaWithGrupo(fecha));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Clase>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(claseService.findByFechasBetween(inicio, fin));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Clase>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(claseService.findActivasByCategoria(categoriaId));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Clase>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(claseService.findActivasBySucursal(sucursalId));
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Clase>> getByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(claseService.findActivasByClub(clubId));
    }

    // Metodo POST
    @PostMapping("/create-clase")
    public ResponseEntity<Clase> create(@RequestBody Clase clase) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(claseService.create(clase));
    }

    // Metodo PUT
    @PutMapping("/update-clase/{id}")
    public ResponseEntity<Clase> update(@PathVariable Long id, @RequestBody Clase clase) {
        return ResponseEntity.ok(claseService.update(id, clase));
    }

    // Metodo PATCH
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Clase> cambiarEstado(@PathVariable Long id,@RequestParam String estado) {
        return ResponseEntity.ok(claseService.cambiarEstado(id, estado));
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Clase> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.iniciar(id));
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Clase> completar(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.completar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Clase> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.cancelar(id));
    }

    // Metodo DELETE
    @DeleteMapping("/delete-clase/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        claseService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    // Validaciones
    @GetMapping("/validar/{id}/activa")
    public ResponseEntity<Boolean> isActiva(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.isActiva(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.existsById(id));
    }

    @GetMapping("/validar/horario")
    public ResponseEntity<Boolean> existsClaseEnHorario(
            @RequestParam Long grupoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin) {
        return ResponseEntity.ok(claseService.existsClaseEnHorario(grupoId, fecha, horaInicio, horaFin));
    }

    // Estadisticas
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasClaseDTO> getEstadisticas() {
        return ResponseEntity.ok(claseService.getEstadisticas());
    }
}
