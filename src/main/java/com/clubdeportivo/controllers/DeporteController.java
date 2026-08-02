package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.deporte.EstadisticasDeporteDTO;
import com.clubdeportivo.models.Deporte;
import com.clubdeportivo.services.deporte.IDeporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/deportes")
@Tag(name = "Deportes", description = "Endpoints for deportes")
public class DeporteController {

    private IDeporteService deporteService;

    @Autowired
    private DeporteController(IDeporteService deporteService) {
        deporteService = deporteService;
    }

    @GetMapping("/list-all")
    @Operation(
            summary = "LIst All deportes and return Arrays",
            description = "returns a complete list of the entered sports",
            tags = {"Deportes"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Example of a successful sports list",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Deporte.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "sample with no data"
                    )
            }
    )
    public ResponseEntity<List<Deporte>> getAll() {
        return ResponseEntity.ok(deporteService.findAll());
    }

    @GetMapping("/list-activos")
    public ResponseEntity<List<Deporte>> getActivos() {
        return ResponseEntity.ok(deporteService.findActivos());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Deporte> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deporteService.findById(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Deporte>> search(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(deporteService.searchByNombre(nombre));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Deporte> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(deporteService.findByNombre(nombre));
    }

    @PostMapping("/create-deporte")
    public ResponseEntity<Deporte> create(@RequestBody Deporte deporte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deporteService.create(deporte));
    }

    @PutMapping("/update-deporte/{id}")
    public ResponseEntity<Deporte> update(@PathVariable Long id, @RequestBody Deporte deporte) {
        return ResponseEntity.ok(deporteService.update(id, deporte));
    }

    @DeleteMapping("/delete-deporte/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deporteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(deporteService.getDeportesParaSelect());
    }

    @GetMapping("/select/todos")
    public ResponseEntity<List<Object[]>> getTodosSelectOptions() {
        return ResponseEntity.ok(deporteService.getTodosDeportesParaSelect());
    }

    @GetMapping("/estadistica")
    public ResponseEntity<EstadisticasDeporteDTO> getEstadistica(){
        return ResponseEntity.ok(deporteService.getEstadisticas());
    }

    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(deporteService.isActivo(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(deporteService.existsById(id));
    }
}
