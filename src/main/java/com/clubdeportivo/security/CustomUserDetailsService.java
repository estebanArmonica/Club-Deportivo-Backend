package com.clubdeportivo.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IUsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // buscamos por email en lugar de username
        Usuario user = userRepo.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("User not found with email: {}", email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });

        // validamos si el usuario está activo
        if(!user.getIsActive()){
            log.warn("User is disabled: {}", email);
            throw new UsernameNotFoundException("User account is disabled: " + email);
        }

        // convertimos los roles a GrantedAuthority
        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(rol -> {
                // aseguramos que el rol tenga el prefijo ROLE_ si es necesario
                String roleName = rol.getTipoRol();
                if (!roleName.startsWith("ROLE_")) {
                    roleName = "ROLE_" + roleName;
                }
                return new SimpleGrantedAuthority(roleName);
            }).collect(Collectors.toSet());

        log.debug("User loaded successfully: {} with roles: {}", email, authorities);
        
        // Creamos UserDetails con email como username
        return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.getIsActive())
            .build();
    }

    /**
     * Método adicional para cargar usuario por ID (útil para otros casos)
    */
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        Usuario user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new UsernameNotFoundException("User not found with ID: " + userId);
                });

        return loadUserByUsername(user.getEmail());
    }

    /**
     * Método para verificar si un email existe
    */
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}
