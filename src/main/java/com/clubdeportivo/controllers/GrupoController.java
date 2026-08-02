package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.grupo.EstadisticasGrupoDTO;
import com.clubdeportivo.models.Grupo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clubdeportivo.services.grupo.IGrupoService;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/grupos")
@Tag(name = "Grupos", description = "Endpoints for grupo")
public class GrupoController {

    @Autowired
    private IGrupoService grupoService;

    // crud de endpoints
    @GetMapping("/list-all")
    public ResponseEntity<List<Grupo>> getAll() {
        log.info("Listamos todos los grupos disponibles");
        return ResponseEntity.ok(grupoService.findAllWithCategoriaAndSucursal());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Grupo>> getActivos() {
        log.info("Verificamos que grupos están activos para mostrar");
        return ResponseEntity.ok(grupoService.findActivosWithCategoriaAndSucursal());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Grupo> getId(@PathVariable Long id) {
        log.info("Buscando un grupo en especifico con el ID: ", id);
        return ResponseEntity.ok(grupoService.findByIdWithCategoriaAndSucursal(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Grupo>> search(@RequestParam(required = false) String nombre) {
        log.info("Buscando grupo con el nombre: ", nombre);
        return ResponseEntity.ok(grupoService.searchByNombre(nombre));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Grupo> getByNombre(@PathVariable String nombre) {
        log.info("Validando grupo con el nombre: ", nombre);
        return ResponseEntity.ok(grupoService.findByNombre(nombre));
    }

    @GetMapping("/categoria/{cateId}")
    public ResponseEntity<List<Grupo>> getByCategoria(@PathVariable Long cateId) {
        log.info("Buscando grupo con la categoria: ", cateId);
        return ResponseEntity.ok(grupoService.findActivosByCategoria(cateId));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Grupo>> getBySucursal(@PathVariable Long sucursalId) {
        log.info("Buscando grupo con la sucursal: ", sucursalId);
        return ResponseEntity.ok(grupoService.findActivosBySucursal(sucursalId));
    }

    @GetMapping("/cate/{cateId}/sucursal/{sucursalId}")
    public ResponseEntity<List<Grupo>> getByCAtegoriaAndSucursal(@PathVariable Long cateId, @PathVariable Long sucursalId) {
        return ResponseEntity.ok(grupoService.findActivosByCategoriaAndSucursal(cateId, sucursalId));
    }

    @GetMapping("/precio")
    public ResponseEntity<List<Grupo>> getByPrecio(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return ResponseEntity.ok(grupoService.findByPrecioBetween(min, max));
    }

    @GetMapping("/precio")
    public ResponseEntity<List<Grupo>> getByCapacidad(@RequestParam int min, @RequestParam int max) {
        return ResponseEntity.ok(grupoService.findByCapacidadBetween(min, max));
    }

    @GetMapping("/horario")
    public ResponseEntity<List<Grupo>> getByHorario(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)LocalTime horaInicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)LocalTime horaFin) {
        return ResponseEntity.ok(grupoService.findByHorario(horaInicio, horaFin));
    }

    // POST
    @PostMapping("/create-grupo")
    public ResponseEntity<Grupo> create(@RequestBody Grupo grupo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(grupoService.create(grupo));
    }

    @PutMapping("/update-grupo/{id}")
    public ResponseEntity<Grupo> update(@PathVariable Long id, @RequestBody Grupo grupo) {
        return ResponseEntity.ok(grupoService.update(id, grupo));
    }

    @DeleteMapping("/delete-grupo/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        grupoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // select y dropdown
    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(grupoService.getGruposParaSelect());
    }

    @GetMapping("/select/categoria/{categoriaId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(grupoService.getGruposByCategoriaParaSelect(categoriaId));
    }

    @GetMapping("/select/sucursal/{sucursalId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(grupoService.getGruposBySucursalParaSelect(sucursalId));
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasGrupoDTO> getEstadisticas() {
        return ResponseEntity.ok(grupoService.getEstadisticas());
    }

    // validaciones
    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.isActivo(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.existsById(id));
    }

    @GetMapping("/{id}/cupos-disponibles")
    public ResponseEntity<Integer> getCuposDisponibles(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.getCuposDisponibles(id));
    }

    @GetMapping("/{id}/has-cupos")
    public ResponseEntity<Boolean> hasCuposDisponibles(@PathVariable Long id) {
        return ResponseEntity.ok(grupoService.hasCuposDisponibles(id));
    }
}
