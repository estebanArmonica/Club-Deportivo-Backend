package com.clubdeportivo.services.auth.utils;

import com.clubdeportivo.dtos.auth.LoginRequest;
import com.clubdeportivo.dtos.auth.LoginResponse;
import com.clubdeportivo.dtos.auth.RegisterRequest;
import com.clubdeportivo.models.Rol;
import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IRolRepository;
import com.clubdeportivo.repositories.IUsuarioRepository;
import com.clubdeportivo.security.JwtGenerador;
import com.clubdeportivo.services.auth.IAuthService;
import com.clubdeportivo.services.user.IUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerador jwtGenerador;
    private final IUsuarioService usuarioService;
    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.debug("Login attempt for user: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerador.generateToken(authentication);
        String refreshToken = jwtGenerador.generateRefreshToken(loginRequest.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .nombre(usuario.getEmail())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream()
                        .map(Rol::getTipoRol)
                        .collect(Collectors.toList()))
                .expiresIn(jwtGenerador.getExpirationDateFromToken(token).getTime())
                .build();

        log.info("User logged in successfully: {}", loginRequest.getEmail());

        return response;
    }

    @Override
    public LoginResponse register(RegisterRequest registerRequest) {
        log.debug("Register attempt for user: {}", registerRequest.getEmail());

        if (usuarioService.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        Usuario usuario = Usuario.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(true)
                .build();

        // Asignar roles
        Set<Rol> roles = new HashSet<>();

        if (registerRequest.getRole() != null && !registerRequest.getRole().isEmpty()) {
            Rol userRole = rolRepository.findByTipoRol(registerRequest.getRole())
                    .orElseThrow(() -> new RuntimeException("Error: Role not found: " + registerRequest.getRole()));
            roles.add(userRole);
        } else {
            // Rol por defecto: USER
            Rol defaultRole = rolRepository.findByTipoRol("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Default role not found"));
            roles.add(defaultRole);
        }

        usuario.setRoles(roles);

        Usuario savedUser = usuarioService.createUser(usuario);

        // Generar token automáticamente después del registro
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerador.generateToken(authentication);
        String refreshToken = jwtGenerador.generateRefreshToken(registerRequest.getEmail());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .nombre(savedUser.getEmail())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream()
                        .map(Rol::getTipoRol)
                        .collect(Collectors.toList()))
                .expiresIn(jwtGenerador.getExpirationDateFromToken(token).getTime())
                .build();

        log.info("User registered successfully: {}", registerRequest.getEmail());

        return response;
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        log.debug("Refresh token request");

        if (!jwtGenerador.validateToken(refreshToken) || !jwtGenerador.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtGenerador.getEmailFromToken(refreshToken);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = jwtGenerador.generateToken(
                usuario.getEmail(),
                usuario.getEmail(),
                usuario.getRoles().stream().map(Rol::getTipoRol).collect(Collectors.toList())
        );

        return LoginResponse.builder()
                .token(newToken)
                .type("Bearer")
                .nombre(usuario.getEmail())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream().map(Rol::getTipoRol).collect(Collectors.toList()))
                .expiresIn(jwtGenerador.getExpirationDateFromToken(newToken).getTime())
                .build();
    }

    @Override
    public void logout(String token) {
        log.debug("Logout request");
        // Aquí puedes implementar lógica de blacklist de tokens si lo deseas
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
    }

    @Override
    public boolean validateToken(String token) {
        return jwtGenerador.validateToken(token);
    }
    
}
