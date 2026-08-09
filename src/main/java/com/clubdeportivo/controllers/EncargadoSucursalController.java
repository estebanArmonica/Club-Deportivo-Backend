package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.encargadosucursal.EstadisticasEncargadoSucursalDTO;
import com.clubdeportivo.models.EncargadoSucursal;
import com.clubdeportivo.services.encargadosucursal.IEncargadoSucursalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/encargado-sucursal")
@RequiredArgsConstructor
@Tag(name = "EncargadosSucursales", description = "Endpoints for Encargados Sucursales")
public class EncargadoSucursalController {
    private final IEncargadoSucursalService encargadoSucursalService;

    /**
     * Crear una nueva asignación de encargado a sucursal
     */
    @PostMapping("/create-encargado")
    public ResponseEntity<EncargadoSucursal> create(@RequestBody EncargadoSucursal encargadoSucursal) {
        EncargadoSucursal created = encargadoSucursalService.create(encargadoSucursal);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualizar una asignación existente
     */
    @PutMapping("/update-encargado/{id}")
    public ResponseEntity<EncargadoSucursal> update(
            @PathVariable Long id,
            @RequestBody EncargadoSucursal encargadoSucursal) {
        EncargadoSucursal updated = encargadoSucursalService.update(id, encargadoSucursal);
        return ResponseEntity.ok(updated);
    }

    /**
     * Buscar asignación por ID
     */
    @GetMapping("/list-encargado/{id}")
    public ResponseEntity<EncargadoSucursal> getById(@PathVariable Long id) {
        EncargadoSucursal encargadoSucursal = encargadoSucursalService.findByIdWithAllRelations(id);
        return ResponseEntity.ok(encargadoSucursal);
    }

    /**
     * Listar todas las asignaciones
     */
    @GetMapping("/list-all")
    public ResponseEntity<List<EncargadoSucursal>> getAll() {
        return ResponseEntity.ok(encargadoSucursalService.findAllWithUsuarioAndSucursal());
    }

    /**
     * Listar asignaciones activas
     */
    @GetMapping("/activos")
    public ResponseEntity<List<EncargadoSucursal>> getActivos() {
        return ResponseEntity.ok(encargadoSucursalService.findActivosWithUsuarioAndSucursal());
    }

    /**
     * Buscar asignaciones por sucursal
     */
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<EncargadoSucursal>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(encargadoSucursalService.findActivosBySucursal(sucursalId));
    }

    /**
     * Buscar asignaciones por usuario (encargado)
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EncargadoSucursal>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(encargadoSucursalService.findActivosByUsuario(usuarioId));
    }

    /**
     * Buscar asignaciones por turno
     */
    @GetMapping("/turno/{turno}")
    public ResponseEntity<List<EncargadoSucursal>> getByTurno(@PathVariable String turno) {
        return ResponseEntity.ok(encargadoSucursalService.findByTurno(turno));
    }

    /**
     * Buscar asignaciones por sucursal y turno
     */
    @GetMapping("/sucursal/{sucursalId}/turno/{turno}")
    public ResponseEntity<List<EncargadoSucursal>> getBySucursalAndTurno(
            @PathVariable Long sucursalId,
            @PathVariable String turno) {
        return ResponseEntity.ok(encargadoSucursalService.findActivosBySucursalAndTurno(sucursalId, turno));
    }

    /**
     * Buscar asignaciones por club
     */
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<EncargadoSucursal>> getByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(encargadoSucursalService.findActivosByClub(clubId));
    }

    /**
     * Desactivar una asignación (borrado lógico)
     */
    @DeleteMapping("/delete-encargado/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        encargadoSucursalService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    /**
     * Verificar si una asignación existe
     */
    @GetMapping("/validar/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(encargadoSucursalService.existsById(id));
    }

    /**
     * Verificar si una asignación está activa
     */
    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(encargadoSucursalService.isActivo(id));
    }

    /**
     * Verificar si un usuario ya está asignado a una sucursal con un turno específico
     */
    @GetMapping("/validar/asignacion")
    public ResponseEntity<Boolean> existsUsuarioAsignadoSucursalTurno(
            @RequestParam Long usuarioId,
            @RequestParam Long sucursalId,
            @RequestParam String turno) {
        return ResponseEntity.ok(encargadoSucursalService.existsUsuarioAsignadoSucursalTurno(usuarioId, sucursalId, turno));
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Obtener estadísticas de asignaciones
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasEncargadoSucursalDTO> getEstadisticas() {
        return ResponseEntity.ok(encargadoSucursalService.getEstadisticas());
    }
}
