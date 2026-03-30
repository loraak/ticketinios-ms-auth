package com.example.ticketinios.jirapobre.dto;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String opCode;
    private String message;
    private List<T> data;
    private List<String> errors;
    private int total;
    private Instant timestamp;
}
