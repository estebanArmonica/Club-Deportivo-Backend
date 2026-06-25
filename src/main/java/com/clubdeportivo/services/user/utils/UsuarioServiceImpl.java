package com.clubdeportivo.services.user.utils;

import java.util.Optional;

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
public class UsuarioServiceImpl implements IUsuarioService{
    
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
