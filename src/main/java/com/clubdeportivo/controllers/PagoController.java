package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.pago.EstadisticasPagoDTO;
import com.clubdeportivo.models.Pago;
import com.clubdeportivo.services.pago.IPagoService;
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
@RequestMapping("api/v1/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Endpoints for Pagos")
public class PagoController {
    private final IPagoService pagoService;

    // ============================================================
    // CRUD
    // ============================================================

    @GetMapping("/list-all")
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @GetMapping("/list-pago/{id}")
    public ResponseEntity<Pago> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findById(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pagoService.findByEstado(estado));
    }

    @GetMapping("/metodo-pago/{metodoPago}")
    public ResponseEntity<List<Pago>> getByMetodoPago(@PathVariable String metodoPago) {
        return ResponseEntity.ok(pagoService.findByMetodoPago(metodoPago));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Pago>> getByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(pagoService.findByFecha(fecha));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Pago>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(pagoService.findByFechasBetween(inicio, fin));
    }

    @GetMapping("/monto")
    public ResponseEntity<List<Pago>> getByMonto(
            @RequestParam int min,
            @RequestParam int max) {
        return ResponseEntity.ok(pagoService.findByMontoBetween(min, max));
    }

    @GetMapping("/estado/{estado}/metodo-pago/{metodoPago}")
    public ResponseEntity<List<Pago>> getByEstadoAndMetodoPago(
            @PathVariable String estado,
            @PathVariable String metodoPago) {
        return ResponseEntity.ok(pagoService.findByEstadoAndMetodoPago(estado, metodoPago));
    }

    @GetMapping("/del-dia")
    public ResponseEntity<List<Pago>> getPagosDelDia() {
        return ResponseEntity.ok(pagoService.findPagosDelDia());
    }

    @GetMapping("/de-la-semana")
    public ResponseEntity<List<Pago>> getPagosDeLaSemana() {
        return ResponseEntity.ok(pagoService.findPagosDeLaSemana());
    }

    @GetMapping("/del-mes")
    public ResponseEntity<List<Pago>> getPagosDelMes() {
        return ResponseEntity.ok(pagoService.findPagosDelMes());
    }

    @PostMapping("/create-pago")
    public ResponseEntity<Pago> create(@RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.create(pago));
    }

    @PutMapping("/update-pago/{id}")
    public ResponseEntity<Pago> update(@PathVariable Long id, @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.update(id, pago));
    }

    @PatchMapping("/cambiar/{id}/estado")
    public ResponseEntity<Pago> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(pagoService.cambiarEstado(id, estado));
    }

    @PatchMapping("/pagado/{id}")
    public ResponseEntity<Pago> marcarComoPagado(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.marcarComoPagado(id));
    }

    @PatchMapping("/cancelado/{id}")
    public ResponseEntity<Pago> marcarComoCancelado(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.marcarComoCancelado(id));
    }

    @PatchMapping("/reembolsado/{id}")
    public ResponseEntity<Pago> marcarComoReembolsado(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.marcarComoReembolsado(id));
    }

    @DeleteMapping("/delete-pago/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ESTADÍSTICAS Y REPORTES
    // ============================================================

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasPagoDTO> getEstadisticas() {
        return ResponseEntity.ok(pagoService.getEstadisticas());
    }

    @GetMapping("/resumen-diario")
    public ResponseEntity<List<Object[]>> getResumenDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(pagoService.getResumenDiario(inicio, fin));
    }

    @GetMapping("/total-fechas")
    public ResponseEntity<Long> getTotalByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(pagoService.getTotalPagosByFechas(inicio, fin));
    }

    @GetMapping("/total-estado-fechas")
    public ResponseEntity<Long> getTotalByEstadoAndFechas(
            @RequestParam String estado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(pagoService.getTotalPagosByEstadoAndFechas(estado, inicio, fin));
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @GetMapping("/validar/pagado/{id}")
    public ResponseEntity<Boolean> isPagado(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.isPagado(id));
    }

    @GetMapping("/validar/pendiente/{id}")
    public ResponseEntity<Boolean> isPendiente(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.isPendiente(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.existsById(id));
    }
}
