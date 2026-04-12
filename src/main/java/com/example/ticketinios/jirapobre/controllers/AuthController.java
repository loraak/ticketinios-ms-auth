package com.example.ticketinios.jirapobre.controllers;

import com.example.ticketinios.jirapobre.dto.ApiResponse;
import com.example.ticketinios.jirapobre.dto.LoginRequest;
import com.example.ticketinios.jirapobre.dto.LoginResponse;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UpdateRequest;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-HEALTH-OK")
                .data(List.of(Map.of("status", "ok", "service", "auth")))
                .build());
    }

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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
            System.out.println("=== HEADERS RECIBIDOS ===");
            request.getHeaderNames().asIterator()
                .forEachRemaining(h -> System.out.println(h + ": " + request.getHeader(h)));
            System.out.println("=========================");
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
            int status = e.getMessage().contains("baja") ? 403 : 401;
            String opCode = e.getMessage().contains("baja")
                ? "MS-AUTH-LOGIN-FORBIDDEN"
                : "MS-AUTH-LOGIN-UNAUTHORIZED";

            ApiResponse<LoginResponse> error = ApiResponse.<LoginResponse>builder()
                .statusCode(status)
                .intOpCode(opCode)
                .data(List.of())
                .build();

            return ResponseEntity.status(status).body(error);
        }
    }

    @PutMapping("/update") 
    public ResponseEntity<ApiResponse<Map<String, String>>> updateUser(
        @RequestHeader("X-User-Id") String usuarioId,
        @Valid @RequestBody UpdateRequest request) {
        try {
            authService.update(UUID.fromString(usuarioId), request); // ← solo una vez

            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-UPDATE-OK")
                .data(List.of(Map.of("message", "Usuario actualizado exitosamente")))
                .build());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(409)
                    .intOpCode("MS-AUTH-UPDATE-CONFLICT")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }

    @PatchMapping("/baja")
    public ResponseEntity<ApiResponse<Map<String, String>>> darDeBaja(
            @RequestHeader("X-User-Id") String usuarioId) {
        try {
            authService.darDeBaja(UUID.fromString(usuarioId));

            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-BAJA-OK")
                .data(List.of(Map.of("message", "Usuario dado de baja exitosamente")))
                .build());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(404)
                    .intOpCode("MS-AUTH-BAJA-NOT-FOUND")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }
}