package com.clubdeportivo.controllers;

import com.clubdeportivo.models.Alumno;
import com.clubdeportivo.services.alumno.IAlumnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/alumnos")
@RequiredArgsConstructor
@Tag(name = "Alumnos", description = "Endpoints for Alumnos")
public class AlumnoController {
    private final IAlumnoService alumnoService;

    // Metodo GET

    /**
     *  Lista a todos los alumons disponibles en el sistema
     * @return una lista de objetos
     */
    @GetMapping("/list-all")
    public ResponseEntity<List<Alumno>> getAll() {
        return ResponseEntity.ok(alumnoService.findAllWithApoderados());
    }

    /**
     *  Buscamos a un alumno por el ID
     * @param id: identificador a buscar del alumno
     * @return un objecto del alumno buscado
     */
    @GetMapping("/list-alumno/{id}")
    public ResponseEntity<Alumno> getById(@PathVariable Long id) {
        Alumno alumno = alumnoService.findByIdWithApoderados(id);
        return ResponseEntity.ok(alumno);
    }

    /**
     * Buscamos a un alumno a travez de su RUT
     * @param rut: rol unico del alumno a buscar
     * @return un objeto del alumon buscado por el RUT
     */
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Alumno> getByRut(@PathVariable String rut) {
        Alumno alumno = alumnoService.findByRut(rut);
        return ResponseEntity.ok(alumno);
    }

    /**
     * Listamos a todos los alumnos disponibles en el sistema por paginación
     * @param pageable: objeto que pagina a cada alumno
     * @return: un objeto para la paginacion de todos los alumnos disponibles en el sistema
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Alumno>> getAllPage(@PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(alumnoService.findAll(pageable));
    }

    /**
     * Listamos a todos aquellos alumnos que tienen su rol activo
     * @return un objeto con alumnos aún activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Alumno>> getActivos() {
        return ResponseEntity.ok(alumnoService.findActivos());
    }

    /**
     * Listamos a todos aquellos alumnos que tienen su rol inactivo
     * @return un objeto con alumnos inactivos
     */
    @GetMapping("/inactivos")
    public ResponseEntity<List<Alumno>> getInactivos() {
        return ResponseEntity.ok(alumnoService.findInactivos());
    }

    /**
     * Buscamos a ún o más alumnos por su nombre o apellido
     * @param nombre nombre del alumno
     * @param apellido apellido del alumno
     * @return un objeto de la busqueda especifica por nombre y apellido
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Alumno>> search(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido) {
        return ResponseEntity.ok(alumnoService.searchByNombreOrApellido(nombre, apellido));
    }

    /**
     * Buscamos a un alumno a travez de un rango de edad
     * @param inicio edad minimo
     * @param fin edad final del rango
     * @return un objeto del rango de inicio y fin
     */
    @GetMapping("/fechas-inscripcion")
    public ResponseEntity<List<Alumno>> getByFechasInscripcion(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(alumnoService.findByFechInscripcionBetween(inicio, fin));
    }

    /**
     * Buscar alumnos por apoderado
     */
    @GetMapping("/apoderado/{apoderadoId}")
    public ResponseEntity<List<Alumno>> getByApoderado(@PathVariable Long apoderadoId) {
        return ResponseEntity.ok(alumnoService.findByApoderado(apoderadoId));
    }

    /**
     * Buscar alumnos activos por grupo
     */
    @GetMapping("/grupo/{grupoId}/activos")
    public ResponseEntity<List<Alumno>> getActivosByGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(alumnoService.findAlumnosActivosByGrupo(grupoId));
    }

    /**
     * Buscar alumnos que asistieron a una clase
     */
    @GetMapping("/clase/{claseId}/asistieron")
    public ResponseEntity<List<Alumno>> getQueAsistieronAClase(@PathVariable Long claseId) {
        return ResponseEntity.ok(alumnoService.findAlumnosQueAsistieronAClase(claseId));
    }

    /**
     * Buscar alumnos que NO asistieron a una clase
     */
    @GetMapping("/clase/{claseId}/no-asistieron")
    public ResponseEntity<List<Alumno>> getQueNoAsistieronAClase(@PathVariable Long claseId) {
        return ResponseEntity.ok(alumnoService.findAlumnosQueNoAsistieronAClase(claseId));
    }

    /**
     * Buscar alumnos por rango de edad
     */
    @GetMapping("/edad")
    public ResponseEntity<List<Alumno>> getByEdad(
            @RequestParam int edadMin,
            @RequestParam int edadMax) {
        return ResponseEntity.ok(alumnoService.findByRangoEdad(edadMin, edadMax));
    }

    // Metodo POST

    /**
     * Creamos a un nuevo alumnos a registrar
     * @param aLumno objeto del alumno que se va a agregar
     * @return returna un objeto nuevo
     */
    @PostMapping("/create-alumno")
    public ResponseEntity<Alumno> create(@Valid @RequestBody Alumno aLumno) {
        Alumno created = alumnoService.create(aLumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Metodo PUT

    /**
     * Buscamos un alumno por el id para actualizar los datos necesarios
     * @param id identificador a buscar
     * @param alumno alumno a actualizar
     * @return un objeto de un alumno actualizado
     */
    @PutMapping("/update-alumno/{id}")
    public ResponseEntity<Alumno> update(@PathVariable Long id, @RequestBody Alumno alumno) {
        Alumno updated = alumnoService.update(id, alumno);
        return ResponseEntity.ok(updated);
    }

    /**
     * Verificar si existe un alumno por ID
     */
    @GetMapping("/validar/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.existsById(id));
    }

    /**
     * Verificar si existe un alumno por RUT
     */
    @GetMapping("/validar/rut")
    public ResponseEntity<Boolean> existsByRut(@RequestParam String rut) {
        return ResponseEntity.ok(alumnoService.existsByRut(rut));
    }

    /**
     * Verificar si un alumno está activo
     */
    @GetMapping("/validar/{id}/activo")
    public ResponseEntity<Boolean> isActivo(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.isActivo(id));
    }

    /**
     * Verificar si un alumno está inscrito en un grupo
     */
    @GetMapping("/validar/inscripcion")
    public ResponseEntity<Boolean> isAlumnoInscritoEnGrupo(
            @RequestParam Long alumnoId,
            @RequestParam Long grupoId) {
        return ResponseEntity.ok(alumnoService.isAlumnoInscritoEnGrupo(alumnoId, grupoId));
    }

    // Metodo DELETE
    @DeleteMapping("/delete-alumno/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alumnoService.deleteLogical(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    /**
     * Contar alumnos activos
     */
    @GetMapping("/contar-activos")
    public ResponseEntity<Long> countAlumnosActivos() {
        return ResponseEntity.ok(alumnoService.countAlumnosActivos());
    }

    /**
     * Obtener alumnos con más asistencias
     */
    @GetMapping("/top-asistencias")
    public ResponseEntity<List<Object[]>> getAlumnosConMasAsistencias() {
        return ResponseEntity.ok(alumnoService.findAlumnosConMasAsistencias());
    }
}
