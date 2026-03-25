package com.example.ticketinios.jirapobre.services;

import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.models.User; 
import com.example.ticketinios.jirapobre.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.findByUsuario(request.usuario()).isPresent()) {
            throw new IllegalStateException("El nombre de usuario ya está en uso.");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("El correo electrónico ya está registrado.");
        }

        User user = new User();
        user.setUsuario(request.usuario());
        user.setNombreCompleto(request.nombreCompleto());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDireccion(request.direccion());
        user.setTelefono(request.telefono());

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            user.setFechaNacimiento(LocalDate.parse(request.fechaNacimiento(), formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Formato de fecha de nacimiento inválido. Use dd/MM/yyyy.");
        }

        return userRepository.save(user);
    }
}
