package com.clubdeportivo.services.user.utils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.clubdeportivo.models.Club;
import com.clubdeportivo.models.Rol;
import com.clubdeportivo.repositories.IClubRepository;
import com.clubdeportivo.repositories.IRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IUsuarioRepository;
import com.clubdeportivo.services.user.IUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {
    
    private final IUsuarioRepository usuarioRepository;
    private final IClubRepository clubRepository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario createUser(Usuario usuario) {
        log.debug("Creating user: {}", usuario.getEmail());

        if (existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email already exists: " + usuario.getEmail());
        }

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setIsActive(true);

        // cargamos los clubes
        if (usuario.getClubes() != null && !usuario.getClubes().isEmpty()) {
            Set<Club> clubesGestionados = new HashSet<>();
            for (Club club : usuario.getClubes()) {
                if (club.getId() != null) {
                    // Buscar el club existente en la base de datos
                    Club clubExistente = clubRepository.findById(club.getId())
                            .orElseThrow(() -> new RuntimeException("Club no encontrado con ID: " + club.getId()));
                    clubesGestionados.add(clubExistente);
                } else {
                    // Si el club no tiene ID, es nuevo - pero normalmente no se crean clubes desde aquí
                    throw new RuntimeException("No se pueden crear clubes al registrar un usuario. El club debe existir.");
                }
            }
            usuario.setClubes(clubesGestionados);
        }

        // asignamos el rol al usuario
        Set<Rol> rolesGestionados = new HashSet<>();

        // si el usuario ya tiene roles, lo gestionamos
        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            for (Rol rol : usuario.getRoles()) {
                if (rol.getId() != null) {
                    // buscamos el rol existente en la base de datos
                    Rol rolExistente = rolRepository.findById(rol.getId())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado con el ID: " + rol.getId()));
                    rolesGestionados.add(rolExistente);
                } else {
                    // si el rol no tiene ID, buscamos por nombre o lanzamos el error
                    if (rol.getTipoRol() != null) {
                        Rol rolExistente = rolRepository.findByTipoRol(rol.getTipoRol())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rol.getTipoRol()));
                        rolesGestionados.add(rolExistente);
                    } else {
                        throw new RuntimeException("El rol no tiene ID ni nombre válido");
                    }
                }
            }
        }

        // por defecto asignamos siempre el rol de usuario (para pruebas)
        boolean tieneRolUser = rolesGestionados.stream()
                .anyMatch(r -> r.getId() != null && r.getId() == 2L);

        if (!tieneRolUser) {
            Rol rolUser = rolRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Rol USER (ID 2) no encontrado"));
            rolesGestionados.add(rolUser);
        }

        usuario.setRoles(rolesGestionados);

        Usuario saved = usuarioRepository.save(usuario);
        log.info("User created successfully with id: {}", saved.getId());

        return saved;
    }

    @Override
    @Transactional
    public Usuario updateUser(Long id, Usuario usuario) {
        log.debug("Updating user with id: {}", id);

        Usuario existing = findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (usuario.getEmail() != null && !usuario.getEmail().equals(existing.getEmail())) {
            if (existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("Email already exists: " + usuario.getEmail());
            }
            existing.setEmail(usuario.getEmail());
        }

        Usuario updated = usuarioRepository.save(existing);
        log.info("User updated successfully with id: {}", id);

        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        log.debug("Finding user by id: {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        log.debug("Finding all users with pagination");
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);
        
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        usuarioRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        log.debug("Changing password for user id: {}", id);
        
        Usuario user = findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);
        
        log.info("Password changed successfully for user id: {}", id);
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        log.debug("Activating user with id: {}", id);
        
        Usuario user = findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setIsActive(true);
        usuarioRepository.save(user);
        
        log.info("User activated successfully with id: {}", id);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        log.debug("Deactivating user with id: {}", id);
        
        Usuario user = findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setIsActive(false);
        usuarioRepository.save(user);
        
        log.info("User deactivated successfully with id: {}", id);
    }
}
