package com.example.ticketinios.jirapobre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EditarUsuarioRequest(
    @NotBlank String nombreCompleto,
    @NotBlank @Email String email,
    String password // opcional
) {}