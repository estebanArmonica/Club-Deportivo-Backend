package com.clubdeportivo.controllers;

import com.clubdeportivo.dtos.auth.LoginRequest;
import com.clubdeportivo.dtos.auth.LoginResponse;
import com.clubdeportivo.services.auth.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description="Endpoints for authentication and user management")
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and return JWT Token",
            description = "Authentication that returns a unique token to the user",
            tags = {"Authentication"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "An email and password are required to obtain a token",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful user authentication",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request for user: {}", loginRequest.getEmail());

        LoginResponse response = authService.login(loginRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Log out for a user",
            description = "Log out for a user using their unique token",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The session was a success"
                    )
            }
    )
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
