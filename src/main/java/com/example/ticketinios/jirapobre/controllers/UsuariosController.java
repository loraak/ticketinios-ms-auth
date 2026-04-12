package com.example.ticketinios.jirapobre.controllers;

import com.example.ticketinios.jirapobre.dto.AdminUpdateRequest;
import com.example.ticketinios.jirapobre.dto.ApiResponse;
import com.example.ticketinios.jirapobre.dto.UpdateRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioDTO;
import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.repositories.PermisoRepository;
import com.example.ticketinios.jirapobre.services.AuthService;
import com.example.ticketinios.jirapobre.services.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private AuthService authService;

    @Value("${service.secret-key}")
    private String serviceSecretKey;

    @GetMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> listarUsuarios() {
        var usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(ApiResponse.<UsuarioDTO>builder()
            .statusCode(200)
            .intOpCode("MS-AUTH-USUARIOS-OK")
            .data(usuarios)
            .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> obtenerUsuario(
            @PathVariable UUID id,
            @RequestHeader("X-Service-Key") String serviceKey) {

        if (!serviceKey.equals(serviceSecretKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            User user = usuarioService.findById(id);
            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-USER-OK")
                .data(List.of(Map.of(
                    "id", user.getId().toString(),
                    "nombreCompleto", user.getNombreCompleto()
                )))
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(404)
                    .intOpCode("MS-AUTH-USER-NOT-FOUND")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }

    //  Administración de usuarios. 
    @GetMapping("/permisos")
    public ResponseEntity<ApiResponse<Map<String, String>>> listarPermisos() {
        var permisos = permisoRepository.findAll().stream()
            .map(p -> Map.of(
                "id",          p.getId().toString(),
                "nombre",      p.getNombre(),
                "descripcion", p.getDescripcion() != null ? p.getDescripcion() : ""
            ))
            .toList();
        return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
            .statusCode(200)
            .intOpCode("MS-AUTH-PERMISOS-LIST-OK")
            .data(permisos)
            .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> editarUsuario(
            @PathVariable UUID id,
            @Valid @RequestBody AdminUpdateRequest request) {
        try {
            usuarioService.actualizarBasico(id, request);
            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-USUARIO-UPDATE-OK")
                .data(List.of(Map.of("message", "Usuario actualizado exitosamente")))
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(409)
                    .intOpCode("MS-AUTH-USUARIO-UPDATE-CONFLICT")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<ApiResponse<Map<String, String>>> darDeBaja(
            @PathVariable UUID id) {
        try {
            authService.darDeBaja(id);
            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-USUARIO-BAJA-OK")
                .data(List.of(Map.of("message", "Usuario dado de baja exitosamente")))
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(404)
                    .intOpCode("MS-AUTH-USUARIO-BAJA-NOT-FOUND")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<ApiResponse<Map<String, String>>> reactivar(
            @PathVariable UUID id) {
        try {
            usuarioService.reactivar(id);
            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-USUARIO-ACTIVAR-OK")
                .data(List.of(Map.of("message", "Usuario reactivado exitosamente")))
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(404)
                    .intOpCode("MS-AUTH-USUARIO-ACTIVAR-NOT-FOUND")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }

    @PutMapping("/{id}/permisos")
    public ResponseEntity<ApiResponse<Map<String, String>>> actualizarPermisos(
            @PathVariable UUID id,
            @RequestBody Map<String, List<String>> body) {
        try {
            List<String> permisos = body.get("permisos");
            usuarioService.actualizarPermisos(id, permisos);
            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .statusCode(200)
                .intOpCode("MS-AUTH-USUARIO-PERMISOS-OK")
                .data(List.of(Map.of("message", "Permisos actualizados exitosamente")))
                .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.<Map<String, String>>builder()
                    .statusCode(404)
                    .intOpCode("MS-AUTH-USUARIO-PERMISOS-NOT-FOUND")
                    .data(List.of(Map.of("message", e.getMessage())))
                    .build());
        }
    }
}