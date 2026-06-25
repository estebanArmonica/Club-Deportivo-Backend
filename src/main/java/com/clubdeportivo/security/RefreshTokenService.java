package com.clubdeportivo.security;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clubdeportivo.models.RefreshToken;
import com.clubdeportivo.models.Usuario;
import com.clubdeportivo.repositories.IRefreshTokenRepository;
import com.clubdeportivo.repositories.IUsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final IUsuarioRepository usuarioRepository;
    //private final JwtGenerador jwtGenerator;

    @Transactional
    public RefreshToken createRefreshToken(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Delete existing refresh token
        refreshTokenRepository.deleteByUsuario(usuario);
        
        RefreshToken refreshToken = RefreshToken.builder()
            .token(UUID.randomUUID().toString())
            .usuario(usuario)
            .expiryDate(LocalDateTime.now().plusDays(7))
            .isRevoked(false)
            .build();
        
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new login request");
        }
        return token;
    }
    
    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            refreshToken.setIsRevoked(true);
            refreshTokenRepository.save(refreshToken);
        });
    }
}
