package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.equipo.EstadisticasEquipoDTO;
import com.clubdeportivo.models.Equipo;
import com.clubdeportivo.services.equipo.IEquipoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/equipos")
@RequiredArgsConstructor
@Tag(name = "Equipos", description = "Endpoints for equipos")
public class EquipoController {

    private IEquipoService equipoService;

    /**
     * Logica de CRUD
     */

    // GET
    @GetMapping("/list-all")
    public ResponseEntity<List<Equipo>> getAll() {
        return ResponseEntity.ok(equipoService.findAllWithUsuarioAndGrupo());
    }

    @GetMapping("/list-equipo/{id}")
    public ResponseEntity<Equipo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.findByIdWithAllRelations(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Equipo>> getActivos() {
        return ResponseEntity.ok(equipoService.findActivosWithUsuarioAndGrupo());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Equipo>> search(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(equipoService.searchByNombre(nombre));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Equipo> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(equipoService.findByNombre(nombre));
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Equipo>> getByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(equipoService.findActivosByGrupo(grupoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Equipo>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(equipoService.findActivosByUsuario(usuarioId));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Equipo>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(equipoService.findActivosByCategoria(categoriaId));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Equipo>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(equipoService.findActivosBySucursal(sucursalId));
    }

    // POST
    @PostMapping("/create-equipo")
    public ResponseEntity<Equipo> create(@RequestBody Equipo equipo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipoService.create(equipo));
    }

    // PUT
    @PutMapping("/update-equipo/{id}")
    public ResponseEntity<Equipo> update(@PathVariable Long id, @RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.update(id, equipo));
    }

    // PATCH
    @PatchMapping("/{equipoId}/capitan/{usuarioId}")
    public ResponseEntity<Equipo> asignarCapitan(@PathVariable Long equipoId,@PathVariable Long usuarioId) {
        return ResponseEntity.ok(equipoService.asignarCapitan(equipoId, usuarioId));
    }

    // DELETE
    @DeleteMapping("/delete-equipo/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipoService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Selects y Estadisticas
     */
    @GetMapping("/select")
    public ResponseEntity<List<Object[]>> getSelectOptions() {
        return ResponseEntity.ok(equipoService.getEquiposParaSelect());
    }

    @GetMapping("/select/grupo/{grupoId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(equipoService.getEquiposByGrupoParaSelect(grupoId));
    }

    @GetMapping("/select/usuario/{usuarioId}")
    public ResponseEntity<List<Object[]>> getSelectOptionsByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(equipoService.getEquiposByUsuarioParaSelect(usuarioId));
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasEquipoDTO> getEstadisticas() {
        return ResponseEntity.ok(equipoService.getEstadisticas());
    }

    /**
     * Validaciones
     */
    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.isActivo(id));
    }

    @GetMapping("/validar/existe/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.existsById(id));
    }

    @GetMapping("/validar/{id}/capitan")
    public ResponseEntity<Boolean> hasCapitanAsignado(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.hasCapitanAsignado(id));
    }

    @GetMapping("/validar/usuario-capitan")
    public ResponseEntity<Boolean> isUsuarioCapitanEnGrupo(@RequestParam Long usuarioId,@RequestParam Long grupoId) {
        return ResponseEntity.ok(equipoService.isUsuarioCapitanEnGrupo(usuarioId, grupoId));
    }
}
