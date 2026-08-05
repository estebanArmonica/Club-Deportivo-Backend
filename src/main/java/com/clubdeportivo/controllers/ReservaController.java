package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.reserva.EstadisticasReservaDTO;
import com.clubdeportivo.models.Reserva;
import com.clubdeportivo.services.reserva.IReservaService;
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
@RequestMapping("api/v1/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Endpoints for reservas")
public class ReservaController {
    private IReservaService reservaService;

    /**
     * Crud de endpoints
     */

    // Metodo GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Reserva>> getAll() {
        return ResponseEntity.ok(reservaService.findAllWithEquipoAndCancha());
    }

    @GetMapping("/list-reserva/{id}")
    public ResponseEntity<Reserva> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findByIdWithAllRelations(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> getByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(reservaService.findByEstadoWithEquipoAndCancha(estado));
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<Reserva>> getByEquipo(@PathVariable Long equipoId) {
        return ResponseEntity.ok(reservaService.findByEquipo(equipoId));
    }

    @GetMapping("/equipo/{equipoId}/activas")
    public ResponseEntity<List<Reserva>> getActivasByEquipo(@PathVariable Long equipoId) {
        return ResponseEntity.ok(reservaService.findActivasByEquipo(equipoId));
    }

    @GetMapping("/cancha/{canchaId}")
    public ResponseEntity<List<Reserva>> getByCancha(@PathVariable Long canchaId) {
        return ResponseEntity.ok(reservaService.findByCancha(canchaId));
    }

    @GetMapping("/cancha/{canchaId}/activas")
    public ResponseEntity<List<Reserva>> getActivasByCancha(@PathVariable Long canchaId) {
        return ResponseEntity.ok(reservaService.findActivasByCancha(canchaId));
    }

    @GetMapping("/cancha/{canchaId}/fecha")
    public ResponseEntity<List<Reserva>> getByCanchaAndFecha(@PathVariable Long canchaId,@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(reservaService.findByCanchaAndFecha(canchaId, fecha));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Reserva>> getByFechas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(reservaService.findByFechasBetween(inicio, fin));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Reserva>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(reservaService.findBySucursal(sucursalId));
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Reserva>> getByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(reservaService.findByClub(clubId));
    }

    // Metodo POST
    @PostMapping("/create-reserva")
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.create(reserva));
    }

    // Metodo PUT
    @PutMapping("/update-reserva/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.update(id, reserva));
    }

    // Metodo PATCH
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reserva> cambiarEstado(@PathVariable Long id,@RequestParam String estado) {
        return ResponseEntity.ok(reservaService.cambiarEstado(id, estado));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Reserva> completar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.completar(id));
    }

    // Metodo DELETE
    @DeleteMapping("/delete-reserva/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Validaciones
     */
    @GetMapping("/validar/disponibilidad")
    public ResponseEntity<Boolean> validarDisponibilidad(
            @RequestParam Long canchaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin) {
        return ResponseEntity.ok(reservaService.isCanchaDisponible(canchaId, fecha, horaInicio, horaFin));
    }

    @GetMapping("/validar/equipo/{equipoId}/reservas-activas")
    public ResponseEntity<Boolean> hasReservasActivas(@PathVariable Long equipoId) {
        return ResponseEntity.ok(reservaService.hasReservasActivas(equipoId));
    }

    /**
     * Estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasReservaDTO> getEstadisticas() {
        return ResponseEntity.ok(reservaService.getEstadisticas());
    }
}
