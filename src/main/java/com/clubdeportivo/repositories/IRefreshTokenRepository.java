package com.clubdeportivo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubdeportivo.models.RefreshToken;
import com.clubdeportivo.models.Usuario;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}
