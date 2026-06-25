package com.clubdeportivo.dtos.auth;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;

    @Builder.Default
    private String type = "Bearer";

    private String nombre;
    private String email;
    private List<String> roles;
    private Long expiresIn;
}
