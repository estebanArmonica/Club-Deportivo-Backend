package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.cancha.EstadisticasCanchaDTO;
import com.clubdeportivo.models.Cancha;
import com.clubdeportivo.services.cancha.ICanchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/cancha")
@RequiredArgsConstructor
@Tag(name = "Canchas", description = "Endpoints for Canchas")
public class CanchaController {
    private ICanchaService canchaService;

    // Metodo GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Cancha>> getAll() {
        return ResponseEntity.ok(canchaService.findAllWithSucursal());
    }

    @GetMapping("/list-cancha/{id}")
    public ResponseEntity<Cancha> getById(@PathVariable Long id) {
        return ResponseEntity.ok(canchaService.findByIdWithSucursal(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Cancha>> getDisponibles() {
        return ResponseEntity.ok(canchaService.findDisponiblesWithSucursal());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cancha>> search(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(canchaService.searchByNombre(nombre));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Cancha> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(canchaService.findByNombre(nombre));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Cancha>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(canchaService.findDisponiblesBySucursal(sucursalId));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Cancha>> getByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(canchaService.findDisponiblesByTipo(tipo));
    }

    @GetMapping("/sucursal/{sucursalId}/tipo/{tipo}")
    public ResponseEntity<List<Cancha>> getBySucursalAndTipo(@PathVariable Long sucursalId, @PathVariable String tipo) {
        return ResponseEntity.ok(canchaService.findDisponiblesBySucursalAndTipo(sucursalId, tipo));
    }

    @GetMapping("/capacidad")
    public ResponseEntity<List<Cancha>> getByCapacidad(@RequestParam int min, @RequestParam int max) {
        return ResponseEntity.ok(canchaService.findByCapacidadBetween(min, max));
    }

    // Metodo POST
    @PostMapping("/create-cancha")
    public ResponseEntity<Cancha> create(@RequestBody Cancha cancha) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(canchaService.create(cancha));
    }

    // Metodo PUT
    @PutMapping("/update-cancha/{id}")
    public ResponseEntity<Cancha> update(@PathVariable Long id, @RequestBody Cancha cancha) {
        return ResponseEntity.ok(canchaService.update(id, cancha));
    }

    // Metodo PATCH
    @PatchMapping("/disponibilidad/{id}")
    public ResponseEntity<Cancha> cambiarDisponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        return ResponseEntity.ok(canchaService.cambiarDisponibilidad(id, disponible));
    }

    // Metodo DELETE
    @DeleteMapping("/delete-cancha/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        canchaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Estadisticas
    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(canchaService.getCanchasParaSelect());
    }

    @GetMapping("/select/sucursal/{sucursalId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(canchaService.getCanchasBySucursalParaSelect(sucursalId));
    }

    @GetMapping("/select/sucursal/{sucursalId}/con-tipo")
    public ResponseEntity<List<Object[]>> getSelectOptionsWithTipoBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(canchaService.getCanchasConTipoBySucursalParaSelect(sucursalId));
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> getTiposDisponibles() {
        return ResponseEntity.ok(canchaService.getTiposDisponibles());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasCanchaDTO> getEstadisticas() {
        return ResponseEntity.ok(canchaService.getEstadisticas());
    }

    // Validaciones
    @GetMapping("/validar/{id}/disponible")
    public ResponseEntity<Boolean> isDisponible(@PathVariable Long id) {
        return ResponseEntity.ok(canchaService.isDisponible(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(canchaService.existsById(id));
    }
}
