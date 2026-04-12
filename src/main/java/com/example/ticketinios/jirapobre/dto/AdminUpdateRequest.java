package com.example.ticketinios.jirapobre.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminUpdateRequest(
    @NotBlank String nombreCompleto,
    @NotBlank String email,
    @NotBlank String usuario
) {}