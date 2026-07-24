package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.auth.LoginRequest;
import com.clubdeportivo.dtos.auth.LoginResponse;
import com.clubdeportivo.services.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@Tag
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request for user: {}", loginRequest.getEmail());

        LoginResponse response = authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        log.info("Logout request");

        // eliminamos el prefijo 'Bearer' si existe
        String jwt = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;

        authService.logout(jwt);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged  out successfully");

        return ResponseEntity.ok(response);
    }
}
