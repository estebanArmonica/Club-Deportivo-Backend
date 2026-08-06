package com.clubdeportivo.controllers;

import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.services.user.IUsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints for usuarios")
public class UsuarioController {
    private IUsuarioService usuarioService;

    // Metodo POST
    @PostMapping("/create-user")
    public ResponseEntity<Usuario> create(@Valid @RequestBody Usuario usuario) {
        Usuario created = usuarioService.createUser(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Metodo PUT
    @PutMapping("/update-user/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        Usuario updated = usuarioService.updateUser(id, usuario);
        return ResponseEntity.ok(updated);
    }

    // Metodo GET
    @GetMapping("/list-user/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getByEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.findByEmail(email);
        return usuario.map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @GetMapping("/list-all")
    public ResponseEntity<Page<Usuario>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.findAll(pageable);
        return ResponseEntity.ok(usuarios);
    }

    // Metodo DELETE
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // otros endpoints
    @PatchMapping("/{id}/cambiar-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        usuarioService.changePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        usuarioService.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        usuarioService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    // Validaciones
    @GetMapping("/validar/email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.existsByEmail(email));
    }

    @GetMapping("/validar/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id).isPresent());
    }
}
