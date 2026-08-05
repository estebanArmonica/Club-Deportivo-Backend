package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.sucursal.EstadisticasSucursalDTO;
import com.clubdeportivo.models.Sucursal;
import com.clubdeportivo.services.sucursal.ISucursalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "Endpoints for sucursales")
public class SucursalController {
    private ISucursalService sucursalService;

    // Endpoints GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Sucursal>> getAll() {
        log.info("Listando todas las Sucursales disponibles");
        return ResponseEntity.ok(sucursalService.findAllWithClub());
    }

    @GetMapping("/list-sucursal/{id}")
    public ResponseEntity<Sucursal> getById(@PathVariable Long id) {
        log.info("Buscando la Sucursal {} ", id);
        return ResponseEntity.ok(sucursalService.findByIdWithClub(id));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Sucursal>> getActivas() {
        log.info("Listando todas las Sucursales activas disponibles");
        return ResponseEntity.ok(sucursalService.findActivasWithClub());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Sucursal>> search(@RequestParam(required = false) String nombre) {
        log.info("Buscando por el nombre {}", nombre);
        return ResponseEntity.ok(sucursalService.searchByNombre(nombre));
    }

    @GetMapping("/buscar-por-direccion")
    public ResponseEntity<List<Sucursal>> searchByDireccion(@RequestParam String direccion) {
        log.info("Buscando por la dirección {}", direccion);
        return ResponseEntity.ok(sucursalService.searchByDireccion(direccion));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Sucursal> getByNombre(@PathVariable String nombre) {
        log.info("Listando todas las Sucursales con el nombre {} ", nombre);
        return ResponseEntity.ok(sucursalService.findByNombre(nombre));
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Sucursal> getByTelefono(@PathVariable String telefono) {
        log.info("Listando todas las Sucursales con el número de telefono {}", telefono);
        return ResponseEntity.ok(sucursalService.findByTelefono(telefono));
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Sucursal>> getByClub(@PathVariable Long clubId) {
        log.info("Buscando el Club con la Sucursal asignada {}", clubId);
        return ResponseEntity.ok(sucursalService.findActivasByClub(clubId));
    }

    // Metodo POST
    @PostMapping("/create-sucursal")
    public ResponseEntity<Sucursal> create(@RequestBody Sucursal sucursal){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sucursalService.create(sucursal));
    }

    // Metodo PUT
    @PutMapping("/update-sucursal/{id]")
    public ResponseEntity<Sucursal> update(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        log.info("Buscando sucursal con el id {}", id);
        return ResponseEntity.ok(sucursalService.update(id, sucursal));
    }

    // Metodo DELETE
    @DeleteMapping("/delete-sucursal/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("ELiminando sucursal con le ID {}", id);
        sucursalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Estadisticas
    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(sucursalService.getSucursalesParaSelect());
    }

    @GetMapping("/select/club/{clubId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(sucursalService.getSucursalesByClubParaSelect(clubId));
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasSucursalDTO> getEstadisticas() {
        return ResponseEntity.ok(sucursalService.getEstadisticas());
    }

    // Validaciones
    @GetMapping("/validar/{id}/activa")
    public ResponseEntity<Boolean> isActiva(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.isActiva(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.existsById(id));
    }
}
