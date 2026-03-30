package com.example.ticketinios.jirapobre.controllers;

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

import com.example.ticketinios.jirapobre.dto.EditarUsuarioRequest;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioAdminDTO;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioAdminDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody RegisterRequest request) {
        try {
            return new ResponseEntity<>(usuarioService.crear(request), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable UUID id,
                                    @Valid @RequestBody EditarUsuarioRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.editar(id, request));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<?> darDeBaja(@PathVariable UUID id) {
        try {
            usuarioService.darDeBaja(id);
            return ResponseEntity.ok(Map.of("message", "Usuario dado de baja."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/permisos")
    public ResponseEntity<?> actualizarPermisos(@PathVariable UUID id,
                                                @RequestBody List<String> permisos) {
        try {
            usuarioService.actualizarPermisos(id, permisos);
            return ResponseEntity.ok(Map.of("message", "Permisos actualizados."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}