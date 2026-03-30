package com.example.ticketinios.jirapobre.controllers;

import com.example.ticketinios.jirapobre.dto.ApiResponse;
import com.example.ticketinios.jirapobre.dto.LoginRequest;
import com.example.ticketinios.jirapobre.dto.LoginResponse;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioDTO;
import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") 
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = authService.register(registerRequest);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        try {
            UsuarioDTO usuarioDTO = authService.login(loginRequest, request);

            LoginResponse loginResponse = LoginResponse.builder()
                .usuario(usuarioDTO)
                .build();

            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .statusCode(200)
                .opCode("OK")
                .message("Login exitoso")
                .data(List.of(loginResponse))
                .total(1)
                .timestamp(Instant.now())
                .build();

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            ApiResponse<LoginResponse> error = ApiResponse.<LoginResponse>builder()
                .statusCode(401)
                .opCode("UNAUTHORIZED")
                .message(e.getMessage())
                .errors(List.of(e.getMessage())) 
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}