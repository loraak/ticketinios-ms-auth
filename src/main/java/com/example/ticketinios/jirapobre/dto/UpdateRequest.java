package com.example.ticketinios.jirapobre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateRequest(
    @NotBlank String nombreCompleto,
    @NotBlank String usuario,
    @NotBlank @Email String email,
    @NotBlank String direccion,
    @NotBlank String telefono,
    @NotBlank String fechaNacimiento
) {}