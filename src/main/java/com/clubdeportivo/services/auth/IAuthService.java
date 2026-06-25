package com.clubdeportivo.services.auth;

import com.clubdeportivo.dtos.auth.LoginRequest;
import com.clubdeportivo.dtos.auth.LoginResponse;
import com.clubdeportivo.dtos.auth.RegisterRequest;

public interface IAuthService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(RegisterRequest registerRequest);
    LoginResponse refreshToken(String refreshToken);
    void logout(String token);
    boolean validateToken(String token);
}
