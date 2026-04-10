package com.example.ticketinios.jirapobre.controllers;

import com.example.ticketinios.jirapobre.dto.ApiResponse;
import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.services.UsuarioService;
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

    @Value("${service.secret-key}")
    private String serviceSecretKey;

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
}