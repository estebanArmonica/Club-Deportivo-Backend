package com.clubdeportivo.services.apoderado.impl;

import com.clubdeportivo.models.Apoderado;
import com.clubdeportivo.repositories.IApoderadoRepository;
import com.clubdeportivo.services.apoderado.IApoderadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ApoderadoServiceImpl implements IApoderadoService {
    private final IApoderadoRepository apoderadoRepository;

    // validaciones privadas
    private void validarParentesco(String parentesco) {
        if (parentesco == null || parentesco.trim().isEmpty()) {
            throw new RuntimeException("El parentesco es obligatorio");
        }
    }

    private void validarContacto(String contacto) {
        if (contacto == null || contacto.trim().isEmpty()) {
            throw new RuntimeException("El contacto es obligatorio");
        }
    }

    // logica del crud
    @Override
    public Apoderado create(Apoderado apoderado) {
        // validamos el parentesco
        validarParentesco(apoderado.getParentesco());

        // validamos el contacto
        validarContacto(apoderado.getContacto());

        // validamos que el contacto no exista
        if(apoderadoRepository.existsByContacto(apoderado.getContacto())){
            throw new RuntimeException("Ya existe un apoderado con el contacto: " + apoderado.getContacto());
        }

        return apoderadoRepository.save(apoderado);
    }

    @Override
    public Apoderado update(Long id, Apoderado apoderadoActualizado) {
        Apoderado apoderadoExistente = apoderadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado con ID: " + id));

        // validamos el parentesco
        validarParentesco(apoderadoActualizado.getParentesco());

        // validamos el contacto
        validarContacto(apoderadoActualizado.getContacto());

        // validamos que el contacto no exista en otro apoderado
        if (!apoderadoExistente.getContacto().equals(apoderadoActualizado.getContacto()) &&
                apoderadoRepository.existsByContacto(apoderadoActualizado.getContacto())) {
            throw new RuntimeException("Ya existe otro apoderado con el contacto: " + apoderadoActualizado.getContacto());
        }

        // actualizamos los campos necesarios
        apoderadoExistente.setParentesco(apoderadoActualizado.getParentesco());
        apoderadoExistente.setContacto(apoderadoActualizado.getContacto());

        // si se actualizan los alumnos
        if (apoderadoActualizado.getAlumnos() != null) {
            apoderadoExistente.setAlumnos(apoderadoActualizado.getAlumnos());
        }

        return apoderadoRepository.save(apoderadoExistente);
    }

    @Override
    public void deletePhysical(Long id) {
        if (!apoderadoRepository.existsById(id)) {
            throw new RuntimeException("Apoderado no encontrado con ID: " + id);
        }

        // Verificar si tiene alumnos asociados
        if (apoderadoRepository.hasAlumnos(id)) {
            throw new RuntimeException("No se puede eliminar el apoderado porque tiene alumnos asociados");
        }

        apoderadoRepository.deleteById(id);
    }

    @Override
    public Apoderado findById(Long id) {
        return apoderadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado con ID: " + id));
    }

    @Override
    public Apoderado findByIdWithAlumnos(Long id) {
        return apoderadoRepository.findByIdWithAlumnos(id)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado con ID: " + id));
    }

    @Override
    public Apoderado findByContacto(String contacto) {
        return apoderadoRepository.findByContacto(contacto)
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado con contacto: " + contacto));
    }

    @Override
    public List<Apoderado> findAll() {
        return apoderadoRepository.findAllByOrderByParentescoAsc();
    }

    @Override
    public Page<Apoderado> findAll(Pageable pageable) {
        return apoderadoRepository.findAll(pageable);
    }

    @Override
    public List<Apoderado> findAllWithAlumnos() {
        return apoderadoRepository.findAllWithAlumnos();
    }

    @Override
    public List<Apoderado> findByParentesco(String parentesco) {
        if (parentesco == null || parentesco.trim().isEmpty()) {
            return findAll();
        }
        return apoderadoRepository.findByParentesco(parentesco);
    }

    @Override
    public List<Apoderado> searchByContacto(String contacto) {
        if (contacto == null || contacto.trim().isEmpty()) {
            return findAll();
        }
        return apoderadoRepository.findByContactoContaining(contacto);
    }

    @Override
    public List<Apoderado> findByAlumno(Long alumnoId) {
        return apoderadoRepository.findApoderadosByAlumnoId(alumnoId);
    }

    @Override
    public List<Apoderado> findByAlumnoRut(String rut) {
        return apoderadoRepository.findApoderadosByAlumnoRut(rut);
    }

    @Override
    public List<Apoderado> findByGrupo(Long grupoId) {
        return apoderadoRepository.findApoderadosByGrupoId(grupoId);
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    @Override
    public List<Object[]> countApoderadosByParentesco() {
        return apoderadoRepository.countApoderadosByParentesco();
    }

    @Override
    public List<Object[]> findApoderadosConMasAlumnos() {
        return apoderadoRepository.findApoderadosConMasAlumnos();
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @Override
    public boolean existsById(Long id) {
        return apoderadoRepository.existsById(id);
    }

    @Override
    public boolean existsByContacto(String contacto) {
        return apoderadoRepository.existsByContacto(contacto);
    }

    @Override
    public boolean hasAlumnos(Long apoderadoId) {
        return apoderadoRepository.hasAlumnos(apoderadoId);
    }
}
