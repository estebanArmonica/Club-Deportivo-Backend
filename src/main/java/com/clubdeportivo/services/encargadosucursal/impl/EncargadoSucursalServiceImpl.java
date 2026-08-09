package com.clubdeportivo.services.encargadosucursal.impl;

import com.clubdeportivo.dtos.encargadosucursal.EstadisticasEncargadoSucursalDTO;
import com.clubdeportivo.models.EncargadoSucursal;
import com.clubdeportivo.models.Sucursal;
import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IEncargadoSucursalRepository;
import com.clubdeportivo.repositories.ISucursalRepository;
import com.clubdeportivo.repositories.IUsuarioRepository;
import com.clubdeportivo.services.encargadosucursal.IEncargadoSucursalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EncargadoSucursalServiceImpl implements IEncargadoSucursalService {
    private final IEncargadoSucursalRepository encargadoSucursalRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ISucursalRepository sucursalRepository;

    // validaciones privadas
    private void validarTurno(String turno) {
        if (turno == null || turno.trim().isEmpty()) {
            throw new RuntimeException("El turno es obligatorio");
        }

        String turnoLower = turno.toLowerCase();
        if (!turnoLower.equals("diurno") &&
                !turnoLower.equals("nocturno") &&
                !turnoLower.equals("completo")) {
            throw new RuntimeException("Turno no válido. Debe ser: diurno, nocturno o completo");
        }
    }

    private Usuario validarUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("La asignación debe tener un usuario (encargado) asociado");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        if (!usuario.getIsActive()) {
            throw new RuntimeException("No se puede asignar un usuario inactivo como encargado");
        }

        return usuario;
    }

    private Sucursal validarSucursal(Long sucursalId) {
        if (sucursalId == null) {
            throw new RuntimeException("La asignación debe tener una sucursal asociada");
        }

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + sucursalId));

        if (!sucursal.getActivo()) {
            throw new RuntimeException("No se puede asignar un encargado a una sucursal inactiva");
        }

        return sucursal;
    }

    private void validarAsignacionUnica(Long usuarioId, Long sucursalId, String turno, Long encargadoSucursalId) {
        // Verificar que el usuario no esté ya asignado a esta sucursal con este turno
        if (encargadoSucursalRepository.existsUsuarioAsignadoSucursalTurno(usuarioId, sucursalId, turno, encargadoSucursalId)) {
            throw new RuntimeException("El usuario ya está asignado como encargado de esta sucursal en el turno " + turno);
        }

        // Verificar que la sucursal no tenga ya un encargado activo para este turno
        if (encargadoSucursalRepository.existsSucursalConEncargadoTurno(sucursalId, turno, encargadoSucursalId)) {
            throw new RuntimeException("La sucursal ya tiene un encargado activo para el turno " + turno);
        }
    }

    // crud logico
    @Override
    public EncargadoSucursal create(EncargadoSucursal encargadoSucursal) {
        // Validar turno
        validarTurno(encargadoSucursal.getTurno());

        // Validar usuario (encargado)
        Long usuarioId = encargadoSucursal.getUser() != null ?
                encargadoSucursal.getUser().getId() : null;
        Usuario usuario = validarUsuario(usuarioId);

        // Validar sucursal
        Long sucursalId = encargadoSucursal.getSucursal() != null ?
                encargadoSucursal.getSucursal().getId() : null;
        Sucursal sucursal = validarSucursal(sucursalId);

        // Validar asignación única
        validarAsignacionUnica(usuarioId, sucursalId, encargadoSucursal.getTurno(), null);

        // Si no se especifica fecha de asignación, usar la actual
        if (encargadoSucursal.getFechAsignacion() == null) {
            encargadoSucursal.setFechAsignacion(LocalDate.now());
        }

        encargadoSucursal.setUser(usuario);
        encargadoSucursal.setSucursal(sucursal);

        return encargadoSucursalRepository.save(encargadoSucursal);
    }

    @Override
    public EncargadoSucursal update(Long id, EncargadoSucursal encargadoSucursalActualizado) {
        EncargadoSucursal encargadoSucursalExistente = encargadoSucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        // Validar turno
        validarTurno(encargadoSucursalActualizado.getTurno());

        // Si se cambia el usuario, validar que exista y esté activo
        Long nuevoUsuarioId = encargadoSucursalActualizado.getUser() != null ?
                encargadoSucursalActualizado.getUser().getId() : null;
        Long usuarioActualId = encargadoSucursalExistente.getUser().getId();

        if (nuevoUsuarioId != null && !nuevoUsuarioId.equals(usuarioActualId)) {
            Usuario usuario = validarUsuario(nuevoUsuarioId);

            // Verificar que el nuevo usuario no esté ya asignado a esta sucursal con este turno
            Long sucursalId = encargadoSucursalActualizado.getSucursal() != null &&
                    encargadoSucursalActualizado.getSucursal().getId() != null ?
                    encargadoSucursalActualizado.getSucursal().getId() :
                    encargadoSucursalExistente.getSucursal().getId();

            validarAsignacionUnica(nuevoUsuarioId, sucursalId, encargadoSucursalActualizado.getTurno(), id);

            encargadoSucursalExistente.setUser(usuario);
        }

        // Si se cambia la sucursal, validar que exista y esté activa
        Long nuevaSucursalId = encargadoSucursalActualizado.getSucursal() != null ?
                encargadoSucursalActualizado.getSucursal().getId() : null;
        Long sucursalActualId = encargadoSucursalExistente.getSucursal().getId();

        if (nuevaSucursalId != null && !nuevaSucursalId.equals(sucursalActualId)) {
            Sucursal sucursal = validarSucursal(nuevaSucursalId);

            // Verificar que la nueva sucursal no tenga ya un encargado activo para este turno
            validarAsignacionUnica(
                    encargadoSucursalExistente.getUser().getId(),
                    nuevaSucursalId,
                    encargadoSucursalActualizado.getTurno(),
                    id
            );

            encargadoSucursalExistente.setSucursal(sucursal);
        } else {
            // Verificar que la sucursal no tenga ya un encargado activo para este turno
            validarAsignacionUnica(
                    encargadoSucursalExistente.getUser().getId(),
                    sucursalActualId,
                    encargadoSucursalActualizado.getTurno(),
                    id
            );
        }

        encargadoSucursalExistente.setTurno(encargadoSucursalActualizado.getTurno());
        encargadoSucursalExistente.setFechAsignacion(encargadoSucursalActualizado.getFechAsignacion());
        encargadoSucursalExistente.setActivo(encargadoSucursalActualizado.getActivo());

        return encargadoSucursalRepository.save(encargadoSucursalExistente);
    }

    @Override
    public void deleteLogical(Long id) {
        EncargadoSucursal encargadoSucursal = encargadoSucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));

        encargadoSucursal.setActivo(false);
        encargadoSucursalRepository.save(encargadoSucursal);
    }

    @Override
    public EncargadoSucursal findById(Long id) {
        return encargadoSucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));
    }

    @Override
    public EncargadoSucursal findByIdWithUsuarioAndSucursal(Long id) {
        return encargadoSucursalRepository.findByIdWithUsuarioAndSucursal(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));
    }

    @Override
    public EncargadoSucursal findByIdWithAllRelations(Long id) {
        return encargadoSucursalRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + id));
    }

    @Override
    public List<EncargadoSucursal> findAll() {
        return encargadoSucursalRepository.findAllByOrderByFechAsignacionDesc();
    }

    @Override
    public List<EncargadoSucursal> findAllWithUsuarioAndSucursal() {
        return encargadoSucursalRepository.findAllWithUsuarioAndSucursal();
    }

    @Override
    public List<EncargadoSucursal> findActivos() {
        return encargadoSucursalRepository.findByActivoTrueOrderByFechAsignacionDesc();
    }

    @Override
    public List<EncargadoSucursal> findActivosWithUsuarioAndSucursal() {
        return encargadoSucursalRepository.findAllActivosWithUsuarioAndSucursal();
    }

    @Override
    public List<EncargadoSucursal> findBySucursal(Long sucursalId) {
        return encargadoSucursalRepository.findBySucursalIdOrderByFechAsignacionDesc(sucursalId);
    }

    @Override
    public List<EncargadoSucursal> findActivosBySucursal(Long sucursalId) {
        return encargadoSucursalRepository.findActivosBySucursalId(sucursalId);
    }

    @Override
    public List<EncargadoSucursal> findByUsuario(Long usuarioId) {
        return encargadoSucursalRepository.findByUsuarioIdOrderByFechAsignacionDesc(usuarioId);
    }

    @Override
    public List<EncargadoSucursal> findActivosByUsuario(Long usuarioId) {
        return encargadoSucursalRepository.findActivosByUsuarioId(usuarioId);
    }

    @Override
    public List<EncargadoSucursal> findByTurno(String turno) {
        return encargadoSucursalRepository.findByTurno(turno);
    }

    @Override
    public List<EncargadoSucursal> findBySucursalAndTurno(Long sucursalId, String turno) {
        return encargadoSucursalRepository.findBySucursalIdAndTurno(sucursalId, turno);
    }

    @Override
    public List<EncargadoSucursal> findActivosBySucursalAndTurno(Long sucursalId, String turno) {
        return encargadoSucursalRepository.findActivosBySucursalIdAndTurno(sucursalId, turno);
    }

    @Override
    public List<EncargadoSucursal> findByClub(Long clubId) {
        return encargadoSucursalRepository.findByClubId(clubId);
    }

    @Override
    public List<EncargadoSucursal> findActivosByClub(Long clubId) {
        return encargadoSucursalRepository.findActivosByClubId(clubId);
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    @Override
    public boolean existsById(Long id) {
        return encargadoSucursalRepository.existsById(id);
    }

    @Override
    public boolean isActivo(Long id) {
        Boolean activo = encargadoSucursalRepository.isRegistroActivo(id);
        if (activo == null) {
            throw new RuntimeException("Asignación no encontrada con ID: " + id);
        }
        return activo;
    }

    @Override
    public boolean existsUsuarioAsignadoSucursalTurno(Long usuarioId, Long sucursalId, String turno) {
        return encargadoSucursalRepository.existsUsuarioAsignadoSucursalTurno(usuarioId, sucursalId, turno, null);
    }

    // ============================================================
    // ESTADÍSTICAS
    // ============================================================

    @Override
    public EstadisticasEncargadoSucursalDTO getEstadisticas() {
        return EstadisticasEncargadoSucursalDTO.builder()
                .total(encargadoSucursalRepository.count())
                .activos(encargadoSucursalRepository.countRegistrosActivos())
                .inactivos(encargadoSucursalRepository.countRegistrosInactivos())
                .asignacionesPorTurno(encargadoSucursalRepository.countRegistrosByTurno())
                .asignacionesActivasPorTurno(encargadoSucursalRepository.countRegistrosActivosByTurno())
                .asignacionesPorSucursal(encargadoSucursalRepository.countRegistrosBySucursal())
                .asignacionesActivasPorSucursal(encargadoSucursalRepository.countRegistrosActivosBySucursal())
                .asignacionesPorUsuario(encargadoSucursalRepository.countRegistrosByUsuario())
                .asignacionesPorMes(encargadoSucursalRepository.countRegistrosByMes())
                .build();
    }
}
