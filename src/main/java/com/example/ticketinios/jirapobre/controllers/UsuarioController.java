package com.example.ticketinios.jirapobre.controllers;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticketinios.jirapobre.services.UsuarioService;

import jakarta.validation.Valid;

import com.example.ticketinios.jirapobre.dto.ApiResponse;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;

    /* 
    @GetMapping
    public ResponseEntity<ApiResponse<UsuarioAdminDTO>> listar() {
        List<UsuarioAdminDTO> lista = usuarioService.listarTodos();
        return ResponseEntity.ok(ApiResponse.<UsuarioAdminDTO>builder()
            .statusCode(200)
            .opCode("OK")
            .message("Usuarios obtenidos exitosamente")
            .errors(List.of())
            .data(lista)
            .total(lista.size())
            .timestamp(Instant.now())
            .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioAdminDTO>> crear(@Valid @RequestBody RegisterRequest request) {
        try {
            UsuarioAdminDTO creado = usuarioService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<UsuarioAdminDTO>builder()
                .statusCode(201)
                .opCode("CREATED")
                .message("Usuario creado exitosamente")
                .errors(List.of())
                .data(List.of(creado))
                .total(1)
                .timestamp(Instant.now())
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<UsuarioAdminDTO>builder()
                .statusCode(409)
                .opCode("CONFLICT")
                .message(e.getMessage())
                .errors(List.of(e.getMessage()))
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioAdminDTO>> editar(@PathVariable UUID id, @Valid @RequestBody EditarUsuarioRequest request) {
        try {
            UsuarioAdminDTO editado = usuarioService.editar(id, request);
            return ResponseEntity.ok(ApiResponse.<UsuarioAdminDTO>builder()
                .statusCode(200)
                .opCode("OK")
                .message("Usuario actualizado exitosamente")
                .errors(List.of())
                .data(List.of(editado))
                .total(1)
                .timestamp(Instant.now())
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<UsuarioAdminDTO>builder()
                .statusCode(400)
                .opCode("BAD_REQUEST")
                .message(e.getMessage())
                .errors(List.of(e.getMessage()))
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build());
        }
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<ApiResponse<Void>> darDeBaja(@PathVariable UUID id) {
        try {
            usuarioService.darDeBaja(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                .statusCode(200)
                .opCode("OK")
                .message("Usuario dado de baja exitosamente")
                .errors(List.of())
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                .statusCode(400)
                .opCode("BAD_REQUEST")
                .message(e.getMessage())
                .errors(List.of(e.getMessage()))
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build());
        }
    }

    @PutMapping("/{id}/permisos")
    public ResponseEntity<ApiResponse<Void>> actualizarPermisos(@PathVariable UUID id, @RequestBody List<String> permisos) {
        try {
            usuarioService.actualizarPermisos(id, permisos);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                .statusCode(200)
                .opCode("OK")
                .message("Permisos actualizados exitosamente")
                .errors(List.of())
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                .statusCode(400)
                .opCode("BAD_REQUEST")
                .message(e.getMessage())
                .errors(List.of(e.getMessage()))
                .data(List.of())
                .total(0)
                .timestamp(Instant.now())
                .build());
        }
    }
*/
}