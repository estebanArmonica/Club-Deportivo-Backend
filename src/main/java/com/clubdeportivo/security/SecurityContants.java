package com.clubdeportivo.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Component
@Getter
public class SecurityContants {
    @Value("${JWT_EXPIRATION}")
    private long jwtExpiration;

    @Value("${JWT_REFRESH_EXPIRATION}")
    private long jwtRefreshExpiration;

    @Value("${JWT_FIRMA}")
    private String jwtSecret;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public SecretKey getJwtSigningKey() {
        return signingKey;
    }
}
