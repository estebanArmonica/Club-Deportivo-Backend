package com.clubdeportivo.dtos.auth;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    @Column(name = "nombre", nullable = false, length = 35)
    private String nombre;

    @Column(name = "email", nullable = false, length = 200)
    private String email;
    
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    
    @Column(name = "role", nullable = false)
    private String role;
}
