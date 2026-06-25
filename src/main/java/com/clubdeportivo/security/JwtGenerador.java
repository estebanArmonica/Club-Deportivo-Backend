package com.clubdeportivo.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtGenerador {
    
    private final SecurityContants securityConstants;

    /**
     * Genera un token de acceso a partir de la autenticación
    */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    /**
     * Genera un token de acceso usando email y roles
    */
    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = buildAccessClaims(email, authorities);
        return buildToken(claims, email, securityConstants.getJwtExpiration());
    }

    /**
     * Genera un token de acceso con claims personalizados
     */
    public String generateToken(String email, String nombre, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("nombre", nombre);
        claims.put("roles", roles);
        claims.put("type", "access");

        return buildToken(claims, email, securityConstants.getJwtExpiration());
    }
    
    /**
     * Genera un token de refresco
    */
    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("email", email);
        
        return buildToken(claims, email, securityConstants.getJwtRefreshExpiration());
    }

    // metodos de aydua para extraer la informacion del token

    /**
     * Obtiene el email (subject) del token
    */
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Obtiene los roles del token
    */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("roles", List.class);
    }

    /**
     * Obtiene el tipo de token (access/refresh)
    */
    public String getTokenType(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("type", String.class);
    }

    /**
     * Obtiene la fecha de expiración del token
    */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * Verifica si el token es de refresco
    */
    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el token es de acceso
    */
    public boolean isAccessToken(String token) {
        try {
            return "access".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos de validaciones del token 
    /**
     * Valida el token verificando firma y expiración
    */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(securityConstants.getJwtSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Valida que el token no haya expirado
    */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // Métodos privados para ayuda del generador del token
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(securityConstants.getJwtSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private Map<String, Object> buildAccessClaims(String email, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        
        claims.put("email", email);
        claims.put("roles", roles);
        claims.put("type", "access");
        
        return claims;
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(now)
            .issuer("CLUB-DEPORTIVO")
            .audience().add("CLUB-API").and()
            .expiration(expiryDate)
            .signWith(securityConstants.getJwtSigningKey())
            .compact();
    }

    // metodos de ayuda y vida al token
    
    /**
     * Obtiene el tiempo restante de vida del token en milisegundos
    */
    public long getRemainingTime(String token) {
        Date expiration = getExpirationDateFromToken(token);
        long remaining = expiration.getTime() - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    /**
     * Verifica si el token puede ser renovado (si es de refresco y no ha expirado)
    */
    public boolean canRefreshToken(String token) {
        return isRefreshToken(token) && !isTokenExpired(token);
    }
}
