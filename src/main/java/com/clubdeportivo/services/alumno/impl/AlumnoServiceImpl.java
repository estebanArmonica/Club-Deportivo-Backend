package com.clubdeportivo.services.alumno.impl;

import com.clubdeportivo.models.Alumno;
import com.clubdeportivo.models.Apoderado;
import com.clubdeportivo.repositories.IAlumnoRepository;
import com.clubdeportivo.repositories.IApoderadoRepository;
import com.clubdeportivo.services.alumno.IAlumnoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AlumnoServiceImpl implements IAlumnoService {

    private IAlumnoRepository alumnoRepository;
    private IApoderadoRepository apoderadoRepository;

    // realizamos validaciones privadas
    private void validarRut(String rut) {
        if(rut == null || rut.trim().isEmpty()){
            throw new RuntimeException("El RUT es obligatorio");
        }

        // realizamos una validación básica del rut chileno
        if(!rut.matches("^\\d{1,2}\\.?\\d{3}\\.?\\d{3}-[\\dkK]$") && !rut.matches("^\\d{7,8}-[\\dkK]$")){
            throw new RuntimeException("El formato del RUT no es válido");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
    }

    private void validarApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de nacimiento no puede ser futura");
        }

        // Validar edad mínima (por ejemplo, 4 años)
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 4) {
            throw new RuntimeException("El alumno debe tener al menos 4 años");
        }
    }

    private void validarApoderados(Alumno alumno) {
        if (alumno.getApoderados() == null || alumno.getApoderados().isEmpty()) {
            throw new RuntimeException("El alumno debe tener al menos un apoderado");
        }

        for (Apoderado apoderado : alumno.getApoderados()) {
            if (apoderado.getId() == null) {
                throw new RuntimeException("El apoderado no tiene ID válido");
            }

            if (!apoderadoRepository.existsById(apoderado.getId())) {
                throw new RuntimeException("Apoderado no encontrado con ID: " + apoderado.getId());
            }
        }
    }

    // crud de logica de negocio
    @Override
    public Alumno create(Alumno alumno) {
        // validamos el rut
        validarRut(alumno.getRut());

        // validamos que el rut ingresado no exista
        if(alumnoRepository.existsByRut(alumno.getRut())) {
            throw new RuntimeException("Ya existe un alumno con el RUT: " + alumno.getRut());
        }

        // validamos el nombre
        validarNombre(alumno.getNombre());

        // validamos el apellido
        validarApellido(alumno.getApellido());

        // validamos la fecha de nacimiento
        validarFechaNacimiento(alumno.getFechNacimiento());

        // validamos el apoderado del alumno
        validarApoderados(alumno);

        // en caso de que no se especifique la fecha de inscripcion, usamos la actual
        if(alumno.getFechInscripcion() == null){
            alumno.setFechInscripcion(LocalDate.now());
        }

        // validamos las observaciones
        if(alumno.getSaludObservaciones() == null) {
            alumno.setSaludObservaciones("");
        }

        return alumnoRepository.save(alumno);
    }

    @Override
    public Alumno update(Long id, Alumno alumnoActualizado) {
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));

        // validamos el RUT
        validarRut(alumnoActualizado.getRut());

        // validamos que el rut no exista en otro alumno
        if(!alumnoExistente.getRut().equals(alumnoActualizado.getRut()) && alumnoRepository.existsByRut(alumnoActualizado.getRut())) {
            throw new RuntimeException("Ya existe otro alumno con el RUT: " + alumnoActualizado.getRut());
        }

        // validamos el nombre
        validarNombre(alumnoActualizado.getNombre());

        // validamos el apellido
        validarApellido(alumnoActualizado.getApellido());

        // validamos la fecha de nacimiento
        validarFechaNacimiento(alumnoActualizado.getFechNacimiento());

        // validamos el apoderado
        if(alumnoActualizado.getApoderados() != null){
            validarApoderados(alumnoActualizado);
            alumnoExistente.setApoderados(alumnoActualizado.getApoderados());
        }

        // actualizamos los campos
        alumnoExistente.setNombre(alumnoActualizado.getNombre());
        alumnoExistente.setApellido(alumnoActualizado.getApellido());
        alumnoExistente.setRut(alumnoActualizado.getRut());
        alumnoExistente.setFechNacimiento(alumnoActualizado.getFechNacimiento());
        alumnoExistente.setSaludObservaciones(alumnoActualizado.getSaludObservaciones());
        alumnoExistente.setActivo(alumnoActualizado.getActivo());

        return alumnoRepository.save(alumnoExistente);
    }

    @Override
    public void deleteLogical(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El ALumno no fue encontrado con el ID: " + id));

        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }

    @Override
    public Alumno findById(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
    }

    @Override
    public Alumno findByIdWithApoderados(Long id) {
        return alumnoRepository.findByIdWithApoderados(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
    }

    @Override
    public Alumno findByRut(String rut) {
        return alumnoRepository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con RUT: " + rut));
    }

    @Override
    public List<Alumno> findAll() {
        return alumnoRepository.findAllByOrderByNombreAsc();
    }

    @Override
    public Page<Alumno> findAll(Pageable pageable) {
        return alumnoRepository.findAll(pageable);
    }

    @Override
    public List<Alumno> findAllWithApoderados() {
        return alumnoRepository.findAllWithApoderados();
    }

    @Override
    public List<Alumno> findActivos() {
        return alumnoRepository.findByActivoTrue();
    }

    @Override
    public List<Alumno> findInactivos() {
        return alumnoRepository.findByActivoFalse();
    }

    @Override
    public List<Alumno> searchByNombreOrApellido(String nombre, String apellido) {
        if ((nombre == null || nombre.trim().isEmpty()) &&
                (apellido == null || apellido.trim().isEmpty())) {
            return findAll();
        }
        return alumnoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                nombre != null ? nombre : "",
                apellido != null ? apellido : ""
        );
    }

    @Override
    public List<Alumno> findByFechInscripcionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return alumnoRepository.findByFechInscripcionBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Alumno> findByApoderado(Long apoderadoId) {
        return alumnoRepository.findAlumnosByApoderadoId(apoderadoId);
    }

    @Override
    public List<Alumno> findAlumnosActivosByGrupo(Long grupoId) {
        return alumnoRepository.findAlumnosActivosByGrupoId(grupoId);
    }

    @Override
    public List<Alumno> findAlumnosQueAsistieronAClase(Long claseId) {
        return alumnoRepository.findAlumnosQueAsistieronAClase(claseId);
    }

    @Override
    public List<Alumno> findAlumnosQueNoAsistieronAClase(Long claseId) {
        return alumnoRepository.findAlumnosQueNoAsistieronAClase(claseId);
    }

    @Override
    public List<Alumno> findByRangoEdad(int edadMin, int edadMax) {
        if (edadMin > edadMax) {
            throw new RuntimeException("La edad mínima no puede ser mayor a la edad máxima");
        }
        return alumnoRepository.findAlumnosByRangoEdad(edadMin, edadMax);
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @Override
    public boolean existsById(Long id) {
        return alumnoRepository.existsById(id);
    }

    @Override
    public boolean existsByRut(String rut) {
        return alumnoRepository.existsByRut(rut);
    }

    @Override
    public boolean isActivo(Long id) {
        return alumnoRepository.findById(id)
                .map(Alumno::getActivo)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
    }

    @Override
    public boolean isAlumnoInscritoEnGrupo(Long alumnoId, Long grupoId) {
        return alumnoRepository.isAlumnoInscritoEnGrupo(alumnoId, grupoId);
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    @Override
    public Long countAlumnosActivos() {
        return alumnoRepository.countAlumnosActivos();
    }

    @Override
    public List<Object[]> findAlumnosConMasAsistencias() {
        return alumnoRepository.findAlumnosConMasAsistencias();
    }
}
