package com.example.ticketinios.jirapobre.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank String usuario,
    @NotBlank String nombreCompleto,
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String direccion,
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank String fechaNacimiento, 
    @NotBlank String telefono
) {}
