package com.clubdeportivo.services.club.impl;

import com.clubdeportivo.dtos.club.EstadisticasClubDTO;
import com.clubdeportivo.models.Club;
import com.clubdeportivo.repositories.IClubRepository;
import com.clubdeportivo.services.club.IClubService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClubServiceImpl implements IClubService {
    private final IClubRepository clubRepo;

    // validaciones privadas
    private void validarEmail(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(!pattern.matcher(email).matches()){
            throw new RuntimeException("EL formato del email no es válido: " + email);
        }
    }

    private void validarCuit(String cuit) {
        if (cuit == null || cuit.trim().isEmpty()) {
            throw new RuntimeException("El CUIT es obligatorio");
        }

        String cuitRegex = "^\\d{2}-\\d{8}-\\d{1}$";
        Pattern pattern = Pattern.compile(cuitRegex);
        if (!pattern.matcher(cuit).matches()) {
            throw new RuntimeException("El formato del CUIT no es válido. Debe ser XX-XXXXXXXX-X");
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        // Solo números, guiones y espacios
        if (!telefono.matches("[0-9\\-\\s+]+")) {
            throw new RuntimeException("El teléfono solo debe contener números, guiones, espacios y el signo +");
        }
    }

    private void validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new RuntimeException("La dirección es obligatoria");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre del club es obligatorio");
        }
    }

    @Override
    public Club create(Club club) {
        // validamos el nombre
        validarNombre(club.getNombre());

        // Validamos que el nombre no exista
        if (clubRepo.existsByNombre(club.getNombre())) {
            throw new RuntimeException("Ya existe un club con el nombre: " + club.getNombre());
        }

        // Validar CUIT
        validarCuit(club.getCuit());

        // Validamos que el CUIT no exista
        if (clubRepo.existsByCuit(club.getCuit())) {
            throw new RuntimeException("Ya existe un club con el CUIT: " + club.getCuit());
        }

        // Validar email
        validarEmail(club.getEmail());

        // Validar que el email no exista
        if (clubRepo.existsByEmail(club.getEmail())) {
            throw new RuntimeException("Ya existe un club con el email: " + club.getEmail());
        }

        // Validar teléfono
        validarTelefono(club.getTelefono());

        // Validar dirección
        validarDireccion(club.getDireccion());

        // Si no se especifica fecha de creación, usar la actual
        if (club.getFechCreacion() == null) {
            club.setFechCreacion(LocalDate.now());
        }

        return clubRepo.save(club);
    }

    @Override
    public Club update(Long id, Club clubActualizado) {
        Club clubExiste = clubRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado: " + id));

        // validamos el nombre
        validarNombre(clubActualizado.getNombre());

        // Validamos que el nombre no exista en otro club
        if (clubRepo.existsByNombreAndIdNot(clubActualizado.getNombre(), id)) {
            throw new RuntimeException("Ya existe otro club con el nombre: " + clubActualizado.getNombre());
        }

        // Validamos el CUIT
        validarCuit(clubActualizado.getCuit());

        // Validamos que el CUIT no exista en otro club
        if (clubRepo.existsByCuitAndIdNot(clubActualizado.getCuit(), id)) {
            throw new RuntimeException("Ya existe otro club con el CUIT: " + clubActualizado.getCuit());
        }

        // Validamos el email
        validarEmail(clubActualizado.getEmail());

        // Validamos que el email no exista en otro club
        if (clubRepo.existsByEmailAndIdNot(clubActualizado.getEmail(), id)) {
            throw new RuntimeException("Ya existe otro club con el email: " + clubActualizado.getEmail());
        }

        // Validamos que el teléfono exista
        validarTelefono(clubActualizado.getTelefono());

        // Validamos la dirección
        validarDireccion(clubActualizado.getDireccion());

        clubExiste.setNombre(clubActualizado.getNombre());
        clubExiste.setCuit(clubActualizado.getCuit());
        clubExiste.setTelefono(clubActualizado.getTelefono());
        clubExiste.setEmail(clubActualizado.getEmail());
        clubExiste.setDireccion(clubActualizado.getDireccion());
        clubExiste.setActivo(clubActualizado.getActivo());

        return clubRepo.save(clubExiste);
    }

    @Override
    public void deleteLogical(Long id) {
        Club club = clubRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con ID: " + id));

        // Verificamos que si tiene sucursales asociadas
        if (clubRepo.hasSucursalesAsociadas(id)) {
            throw new RuntimeException("No se puede desactivar el club porque tiene sucursales asociadas");
        }

        // Verificar si tiene usuarios asociados
        if (clubRepo.hasUsuariosAsociados(id)) {
            throw new RuntimeException("No se puede desactivar el club porque tiene usuarios asociados");
        }

        club.setActivo(false);
        clubRepo.save(club);
    }

    @Override
    public Club findById(Long id) {
        return clubRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con ID: " + id));
    }

    @Override
    public Club findByNombre(String nombre) {
        return clubRepo.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con nombre: " + nombre));
    }

    @Override
    public Club findByCuit(String cuit) {
        return clubRepo.findByCuit(cuit)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con Cuit: " + cuit));
    }

    @Override
    public Club findByEmail(String email) {
        return clubRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con Email: " + email));
    }

    @Override
    public List<Club> findAll() {
        return clubRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public List<Club> findActivos() {
        return clubRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public List<Club> searchByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return clubRepo.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Club> searchByDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            return findAll();
        }
        return clubRepo.findByDireccionContainingIgnoreCase(direccion);
    }

    @Override
    public List<Club> findByFechCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("La fecha de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser después de la fecha de fin");
        }

        return clubRepo.findByFechCreacionBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Club> findActivosCreadosDespuesDe(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        return clubRepo.findByActivoTrueAndFechCreacionAfter(fecha);
    }

    @Override
    public List<Object[]> getClubesParaSelect() {
        return clubRepo.findIdAndNombreByActivoTrue();
    }

    @Override
    public List<Object[]> getClubesConCuitParaSelect() {
        return clubRepo.findIdNombreCuitByActivoTrue();
    }

    @Override
    public EstadisticasClubDTO getEstadisticas() {
        return EstadisticasClubDTO.builder()
                .total(clubRepo.countTotalClubes())
                .activos(clubRepo.countClubesActivos())
                .inactivos(clubRepo.countClubesInactivos())
                .clubesPorMesCreacion(clubRepo.countClubesByMesCreacion())
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return clubRepo.existsById(id);
    }

    @Override
    public boolean isActivo(Long id) {
        Boolean activo = clubRepo.isClubActivo(id);
        if (activo == null) {
            throw new RuntimeException("Club no encontrado con ID: " + id);
        }
        return activo;
    }

    @Override
    public boolean existsByEmail(String email) {
        return clubRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByCuit(String cuit) {
        return clubRepo.existsByCuit(cuit);
    }
}
