package com.example.ticketinios.jirapobre.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {
    private UUID id;
    private String nombreCompleto;
    private String username;
    private String email;
    private String telefono;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private Instant lastLogin;
    private LocalDateTime creadoEn;
    private List<String> permisos;
    private String token;
    private String message; 
}