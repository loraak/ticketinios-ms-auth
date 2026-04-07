package com.example.ticketinios.jirapobre.services;

import com.example.ticketinios.jirapobre.dto.LoginRequest;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioDTO;
import com.example.ticketinios.jirapobre.models.User; 
import com.example.ticketinios.jirapobre.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

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

    public UsuarioDTO login(LoginRequest request, HttpServletRequest httpRequest) {
    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new IllegalStateException("Credenciales inválidas."));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
        throw new IllegalStateException("Credenciales inválidas.");
    }

    String token = jwtService.generateToken(user);

    user.setLastLogin(LocalDateTime.now());
    userRepository.save(user);

    // Registrar en SecurityContext
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
            user.getEmail(),
            null,
            List.of()
        );
    SecurityContextHolder.getContext().setAuthentication(authToken);

    // Una sola sesión con ambos atributos
    HttpSession session = httpRequest.getSession(true);
    session.setAttribute("userId", user.getId());
    session.setAttribute("email", user.getEmail());
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        SecurityContextHolder.getContext()
    );
    return UsuarioDTO.builder()
        .id(user.getId())
        .nombreCompleto(user.getNombreCompleto())
        .username(user.getUsuario())
        .email(user.getEmail())
        .telefono(user.getTelefono())
        .fechaNacimiento(user.getFechaNacimiento())
        .creadoEn(user.getCreadoEn())
        .permisos(user.getPermisos())
        .token(token)
        .build();
}
}