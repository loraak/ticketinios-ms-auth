package com.example.ticketinios.jirapobre.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private UsuarioDTO usuario;
}