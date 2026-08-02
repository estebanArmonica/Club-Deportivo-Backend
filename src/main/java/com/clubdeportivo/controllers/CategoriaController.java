package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.categoria.EstadisticasCategoriaDTO;
import com.clubdeportivo.models.Categoria;
import com.clubdeportivo.services.categoria.ICategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/categorias")
@Tag(name = "Categorias", description = "Endpoints for categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService cateService;

    // logica de endpoints en categoria
    @GetMapping("/list-all")
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(cateService.findAllWithDeporte());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> getActivas() {
        return ResponseEntity.ok(cateService.findActivasWithDeporte());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cateService.findByIdWithDeporte(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> search(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(cateService.searchByNombre(nombre));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categoria> getByNombre(@PathVariable String nombre){
        return ResponseEntity.ok(cateService.findByNombre(nombre));
    }

    @GetMapping("/deporte/{deporteId}")
    public ResponseEntity<List<Categoria>> getByDeporte(@PathVariable Long deporteId) {
        return ResponseEntity.ok(cateService.findActivasByDeporte(deporteId));
    }

    @GetMapping("/por-edad")
    public ResponseEntity<List<Categoria>> getByEdad(@RequestParam int edad) {
        return ResponseEntity.ok(cateService.findByEdad(edad));
    }

    @GetMapping("/deporte/{deporteId}/edad")
    public ResponseEntity<List<Categoria>> getByDeporteAndEdad(
            @PathVariable Long deporteId,
            @RequestParam int edad) {
        return ResponseEntity.ok(cateService.findByDeporteAndEdad(deporteId, edad));
    }

    @PostMapping
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cateService.create(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(cateService.update(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cateService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/fisico")
    public ResponseEntity<Void> deletePhysical(@PathVariable Long id) {
        cateService.deletePhysical(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // SELECTS Y ESTADÍSTICAS
    // ============================================================

    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(cateService.getCategoriasParaSelect());
    }

    @GetMapping("/select/deporte/{deporteId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsByDeporte(@PathVariable Long deporteId) {
        return ResponseEntity.ok(cateService.getCategoriasByDeporteParaSelect(deporteId));
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasCategoriaDTO> getEstadisticas() {
        return ResponseEntity.ok(cateService.getEstadisticas());
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @GetMapping("/validar/{id}/activa")
    public ResponseEntity<Boolean> isActiva(@PathVariable Long id) {
        return ResponseEntity.ok(cateService.isActiva(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(cateService.existsById(id));
    }
}
