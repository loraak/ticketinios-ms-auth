package com.example.ticketinios.jirapobre.controllers;

import com.example.ticketinios.jirapobre.dto.ApiResponse;
import com.example.ticketinios.jirapobre.dto.LoginRequest;
import com.example.ticketinios.jirapobre.dto.LoginResponse;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioDTO;
import com.example.ticketinios.jirapobre.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, String>>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);

            ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .statusCode(201)
                .intOpCode("MS-AUTH-REGISTER-CREATED")
                .data(List.of(Map.of("message", "Usuario registrado exitosamente")))
                .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalStateException e) {

            ApiResponse<Map<String, String>> error = ApiResponse.<Map<String, String>>builder()
                .statusCode(409)
                .intOpCode("MS-AUTH-REGISTER-CONFLICT")
                .data(List.of(Map.of("message", e.getMessage())))
                .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Map<String, String>>> updateUser(
            @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);

            ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .statusCode(201)
                .intOpCode("MS-AUTH-REGISTER-CREATED")
                .data(List.of(Map.of("message", "Usuario registrado exitosamente")))
                .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalStateException e) {

            ApiResponse<Map<String, String>> error = ApiResponse.<Map<String, String>>builder()
                .statusCode(409)
                .intOpCode("MS-AUTH-REGISTER-CONFLICT")
                .data(List.of(Map.of("message", e.getMessage())))
                .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
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
                .intOpCode("MS-AUTH-LOGIN-SUCCESS")
                .data(List.of(loginResponse))
                .build();

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            ApiResponse<LoginResponse> error = ApiResponse.<LoginResponse>builder()
                .statusCode(401)
                .intOpCode("MS-AUTH-LOGIN-UNAUTHORIZED")
                .data(List.of())
                .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}