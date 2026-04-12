package com.example.ticketinios.jirapobre.config;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ticketinios.jirapobre.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<Map<String, String>> errores = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> Map.of(
                "campo", e.getField(),
                "message", e.getDefaultMessage() != null ? e.getDefaultMessage() : "Campo inválido"
            ))
            .toList();

        return ResponseEntity.badRequest().body(
            ApiResponse.<Map<String, String>>builder()
                .statusCode(400)
                .intOpCode("MS-AUTH-VALIDATION-ERROR")
                .data(errores)
                .build()
        );
    }
}