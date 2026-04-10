package com.example.ticketinios.jirapobre.services;

import com.example.ticketinios.jirapobre.dto.LoginRequest;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UpdateRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioDTO;
import com.example.ticketinios.jirapobre.models.Permiso;
import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.models.UsuarioPermiso;
import com.example.ticketinios.jirapobre.repositories.PermisoRepository;
import com.example.ticketinios.jirapobre.repositories.UserRepository;
import com.example.ticketinios.jirapobre.repositories.UsuarioPermisoRepository;

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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private UsuarioPermisoRepository usuarioPermisoRepository;

    private static final List<String> PERMISOS_DEFAULT = List.of(
        "grupos:ver_menu",
        "grupos:ver",
        "grupos:verespecifico",
        "perfil:ver_menu",
        "perfil:ver",
        "perfil:editar",
        "perfil:eliminar"
    );

    private void asignarPermisos(User user, List<String> nombresPermisos) {
        List<Permiso> permisos = permisoRepository.findByNombreIn(nombresPermisos);

        List<UsuarioPermiso> usuarioPermisos = permisos.stream().map(permiso -> {
            UsuarioPermiso up = new UsuarioPermiso();
            up.setUsuario(user);
            up.setPermiso(permiso);
            up.setAsignadoEn(LocalDateTime.now());
            return up;
        }).collect(Collectors.toList());

        usuarioPermisoRepository.saveAll(usuarioPermisos);
    }

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

        User savedUser = userRepository.save(user);
        asignarPermisos(savedUser, PERMISOS_DEFAULT);
        return savedUser;
    }

    public UsuarioDTO login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalStateException("Credenciales inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalStateException("Credenciales inválidas.");
        }

        if (!user.isActivo()) {
            throw new IllegalStateException("Tu cuenta ha sido dada de baja.");
        }

        String token = jwtService.generateToken(user);

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of()
            );
        SecurityContextHolder.getContext().setAuthentication(authToken);

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
            .direccion(user.getDireccion())
            .activo(user.isActivo())
            .fechaNacimiento(user.getFechaNacimiento())
            .creadoEn(user.getCreadoEn())
            .permisos(user.getPermisos())
            .token(token)
            .build();
    }

    public User update(UUID id, UpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));

        // Validar que el nuevo username/email no lo use OTRO usuario
        userRepository.findByUsuario(request.usuario())
            .filter(u -> !u.getId().equals(id))
            .ifPresent(u -> { throw new IllegalStateException("El nombre de usuario ya está en uso."); });

        userRepository.findByEmail(request.email())
            .filter(u -> !u.getId().equals(id))
            .ifPresent(u -> { throw new IllegalStateException("El correo electrónico ya está registrado."); });

        user.setNombreCompleto(request.nombreCompleto());
        user.setUsuario(request.usuario());
        user.setEmail(request.email());
        user.setDireccion(request.direccion());
        user.setTelefono(request.telefono());

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            user.setFechaNacimiento(LocalDate.parse(request.fechaNacimiento(), formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Formato de fecha inválido. Use dd/MM/yyyy.");
        }

        return userRepository.save(user);
    }

    public void darDeBaja(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));
    user.setActivo(false);
    userRepository.save(user);
}

    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
    }
}