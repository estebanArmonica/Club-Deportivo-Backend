package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.club.EstadisticasClubDTO;
import com.clubdeportivo.models.Club;
import com.clubdeportivo.services.club.IClubService;
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
@RequestMapping("api/v1/clubes")
@RequiredArgsConstructor
@Tag(name = "Clubes", description = "Endpoints for clubes")
public class ClubController {
    private IClubService clubService;

    // endpintos GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Club>> getAll() {
        return ResponseEntity.ok(clubService.findAll());
    }

    @GetMapping("/list-club/{id}")
    public ResponseEntity<Club> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.findById(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Club>> getActivos() {
        return ResponseEntity.ok(clubService.findActivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Club>> search(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(clubService.searchByNombre(nombre));
    }

    @GetMapping("/buscar-por-direccion")
    public ResponseEntity<List<Club>> searchByDireccion(@RequestParam(required = false) String direccion) {
        return ResponseEntity.ok(clubService.searchByDireccion(direccion));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Club> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(clubService.findByNombre(nombre));
    }

    @GetMapping("/cuit/{cuit}")
    public ResponseEntity<Club> getByCuit(@PathVariable String cuit) {
        return ResponseEntity.ok(clubService.findByCuit(cuit));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Club> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clubService.findByEmail(email));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Club>> getByFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(clubService.findByFechCreacionBetween(inicio, fin));
    }

    @GetMapping("/creados-despues")
    public ResponseEntity<List<Club>> getCreadosDespuesDe(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(clubService.findActivosCreadosDespuesDe(fecha));
    }

    @PostMapping("/create-club")
    public ResponseEntity<Club> create(@RequestBody Club club) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubService.create(club));
    }

    @PutMapping("/update-club/{id}")
    public ResponseEntity<Club> update(@PathVariable Long id, @RequestBody Club club) {
        return ResponseEntity.ok(clubService.update(id, club));
    }

    @DeleteMapping("/delete-club/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clubService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    // Selects y Estadisticas
    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(clubService.getClubesParaSelect());
    }

    @GetMapping("/select-con-cuit")
    public ResponseEntity<List<Object[]>> getSelectOptionsWithCuit() {
        return ResponseEntity.ok(clubService.getClubesConCuitParaSelect());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasClubDTO> getEstadisticas() {
        return ResponseEntity.ok(clubService.getEstadisticas());
    }

    // validaciones
    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.isActivo(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.existsById(id));
    }

    @GetMapping("/validar/email")
    public ResponseEntity<Boolean> validarEmail(@RequestParam String email) {
        return ResponseEntity.ok(clubService.existsByEmail(email));
    }

    @GetMapping("/validar/cuit")
    public ResponseEntity<Boolean> validarCuit(@RequestParam String cuit) {
        return ResponseEntity.ok(clubService.existsByCuit(cuit));
    }
}
