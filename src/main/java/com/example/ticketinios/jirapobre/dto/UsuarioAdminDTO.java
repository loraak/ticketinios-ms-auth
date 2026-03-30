package com.example.ticketinios.jirapobre.dto;

import java.util.List;
import java.util.UUID;

public record UsuarioAdminDTO(
    UUID id,
    String nombreCompleto,
    String email,
    boolean activo,
    List<String> permisos
) {}