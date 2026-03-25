package com.example.ticketinios.jirapobre.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank String usuario,
    @NotBlank String nombreCompleto,
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String direccion,
    @NotBlank String fechaNacimiento, 
    @NotBlank String telefono
) {}
