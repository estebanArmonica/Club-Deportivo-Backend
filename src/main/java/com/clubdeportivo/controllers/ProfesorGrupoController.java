package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.profesorgrupo.EstadisticasProfesorGrupoDTO;
import com.clubdeportivo.models.ProfesorGrupo;
import com.clubdeportivo.services.profesorgrupo.IProfesorGrupoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/profesor-grupos")
@RequiredArgsConstructor
@Tag(name = "ProfesorGrupo", description = "Endpoints for Profesor Grupos")
public class ProfesorGrupoController {
    private final IProfesorGrupoService profesorGrupoService;

    /**
     * ==================================================================================
     * Crud en endpoints
     * ==================================================================================
     */

    /**
     * Listamos todas las asignaciones del profesor a grupos
     * @return retorna un objeto de todas las asignaciones
     */
    @GetMapping("/list-all")
    public ResponseEntity<List<ProfesorGrupo>> getAll() {
        return ResponseEntity.ok(profesorGrupoService.findAllWithUsuarioAndGrupo());
    }

    /**
     * Buscamos a travez del ID la asignacion de un profesor
     * @param id identificador a buscar
     * @return una sola busqueda especifica
     */
    @GetMapping("/list-asignaciones/{id}")
    public ResponseEntity<ProfesorGrupo> getById(@PathVariable Long id) {
        ProfesorGrupo profesorGrupo = profesorGrupoService.findByIdWithAllRelations(id);
        return ResponseEntity.ok(profesorGrupo);
    }

    /**
     * Buscamos asignaciones que sean activas
     * @return asignaciones con el estado activo
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ProfesorGrupo>> getActivos() {
        return ResponseEntity.ok(profesorGrupoService.findActivosWithUsuarioAndGrupo());
    }

    /**
     * Buscamos asignaciones por grupo
     * @param grupoId grupo a buscar
     * @return retornar las asignaciones con su grupo correspondientes
     */
    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<ProfesorGrupo>> getByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(profesorGrupoService.findActivosByGrupo(grupoId));
    }

    /**
     * Buscar asignaciones por usuario (profesor)
     * @param usuarioId profesor a buscar
     * @return retorna las asignaciones por profesor
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ProfesorGrupo>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(profesorGrupoService.findActivosByUsuario(usuarioId));
    }

    /**
     * Buscar asignaciones por categoría (a través de grupo)
     * @param categoriaId categoria a buscar
     * @return retorna la asignacion con dicha categoria
     */
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProfesorGrupo>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(profesorGrupoService.findActivosByCategoria(categoriaId));
    }

    /**
     * Buscar asignaciones por sucursal (a través de grupo)
     * @param sucursalId id a buscar
     * @return retorna la sucursal buscada
     */
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<ProfesorGrupo>> getBySucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(profesorGrupoService.findActivosBySucursal(sucursalId));
    }

    /**
     * Buscar asignaciones por club (a través de grupo -> sucursal)
     * @param clubId club a buscar
     * @return retorna el club de la sucursal
     */
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<ProfesorGrupo>> getByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(profesorGrupoService.findActivosByClub(clubId));
    }

    /**
     * ==================================================================================
     * Post method
     * ==================================================================================
     */

    /**
     * Creamos una nueva asignación de profesor a un grupo
     * @param profesorGrupo profesor a asingacion de un grupo
     * @return retornar un objeto de asignaciones nuevas
     */
    @PostMapping("/create-asignacion")
    public ResponseEntity<ProfesorGrupo> create(@RequestBody ProfesorGrupo profesorGrupo) {
        ProfesorGrupo created = profesorGrupoService.create(profesorGrupo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * ==================================================================================
     * Put method
     * ==================================================================================
     */

    /**
     * Actualizamos un profesor de una asignacion por su id
     * @param id identificador que busca una asignacion
     * @param profesorGrupo profesor a modificar
     * @return datos actualizados
     */
    @PutMapping("/update-asignacion/{id}")
    public ResponseEntity<ProfesorGrupo> update(@PathVariable Long id, @RequestBody ProfesorGrupo profesorGrupo) {
        ProfesorGrupo updated = profesorGrupoService.update(id, profesorGrupo);
        return ResponseEntity.ok(updated);
    }

    /**
     * ==================================================================================
     * Delete method
     * ==================================================================================
     */

    /**
     * Desactivamos una asignación
     * @param id asignacion a desactivar
     * @return retornar un no content
     */
    @DeleteMapping("/delete-asignacion/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profesorGrupoService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ==================================================================================
     * Validaciones
     * ==================================================================================
     */

    /**
     * Verificar si una asignación existe
     */
    @GetMapping("/validar/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(profesorGrupoService.existsById(id));
    }

    /**
     * Verificar si una asignación está activa
     */
    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(profesorGrupoService.isActivo(id));
    }

    /**
     * Verificar si un usuario ya está asignado a un grupo
     */
    @GetMapping("/validar/asignacion")
    public ResponseEntity<Boolean> isUsuarioAsignadoGrupo(@RequestParam Long usuarioId,@RequestParam Long grupoId) {
        return ResponseEntity.ok(profesorGrupoService.isUsuarioAsignadoGrupo(usuarioId, grupoId));
    }

    /**
     * Verificar si un grupo tiene profesores activos
     */
    @GetMapping("/validar/grupo/{grupoId}/profesores")
    public ResponseEntity<Boolean> hasProfesoresActivos(@PathVariable Long grupoId) {
        return ResponseEntity.ok(profesorGrupoService.hasProfesoresActivos(grupoId));
    }

    /**
     * ==================================================================================
     * Estadisticas
     * ==================================================================================
     */

    /**
     * Obtener estadísticas de asignaciones
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasProfesorGrupoDTO> getEstadisticas() {
        return ResponseEntity.ok(profesorGrupoService.getEstadisticas());
    }
}
