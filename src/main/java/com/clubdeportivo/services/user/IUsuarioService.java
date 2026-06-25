package com.clubdeportivo.services.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clubdeportivo.models.Usuario;

public interface IUsuarioService {
    Usuario createUser(Usuario usuario);
    Usuario updateUser(Long id, Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Page<Usuario> findAll(Pageable pageable);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
    void changePassword(Long id, String newPassword);
    void activateUser(Long id);
    void deactivateUser(Long id);
}
