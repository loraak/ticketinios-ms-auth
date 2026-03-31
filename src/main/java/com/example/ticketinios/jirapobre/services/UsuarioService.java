package com.example.ticketinios.jirapobre.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ticketinios.jirapobre.repositories.PermisoRepository;
import com.example.ticketinios.jirapobre.repositories.UserRepository;
import com.example.ticketinios.jirapobre.repositories.UsuarioPermisoRepository;
import com.example.ticketinios.jirapobre.dto.EditarUsuarioRequest;
import com.example.ticketinios.jirapobre.dto.RegisterRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioAdminDTO;
import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.models.UsuarioPermiso;

@Service
public class UsuarioService {

    @Autowired private UserRepository userRepository;
    @Autowired private PermisoRepository permisoRepository;
    @Autowired private UsuarioPermisoRepository usuarioPermisoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Listar todos
    public List<UsuarioAdminDTO> listarTodos() {
        return userRepository.findAll().stream()
            .map(u -> new UsuarioAdminDTO(
                u.getId(), u.getNombreCompleto(), u.getEmail(),
                u.isActivo(), u.getPermisos()
            ))
            .collect(Collectors.toList());
    }

    public UsuarioAdminDTO crear(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email()))
            throw new IllegalStateException("El email ya está registrado.");

        User user = new User();
        user.setUsuario(request.usuario());
        user.setNombreCompleto(request.nombreCompleto());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDireccion(request.direccion());
        user.setTelefono(request.telefono());
        user.setFechaNacimiento(LocalDate.parse(request.fechaNacimiento()));
        user.setActivo(true);

        User saved = userRepository.save(user);
        return new UsuarioAdminDTO(saved.getId(), saved.getNombreCompleto(),
            saved.getEmail(), saved.isActivo(), saved.getPermisos());
    }

    // Editar usuario
    public UsuarioAdminDTO editar(UUID id, EditarUsuarioRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));

        user.setNombreCompleto(request.nombreCompleto());
        user.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        User saved = userRepository.save(user);
        return new UsuarioAdminDTO(saved.getId(), saved.getNombreCompleto(),
            saved.getEmail(), saved.isActivo(), saved.getPermisos());
    }

    // Dar de baja
    public void darDeBaja(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));
        user.setActivo(false);
        userRepository.save(user);
    }

    // Gestionar permisos
    public void actualizarPermisos(UUID id, List<String> permisos) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));

        // Borrar permisos actuales
        usuarioPermisoRepository.deleteByUsuarioId(id);

        // Insertar los nuevos
        permisos.forEach(nombrePermiso -> {
            permisoRepository.findByNombre(nombrePermiso).ifPresent(permiso -> {
                UsuarioPermiso up = new UsuarioPermiso();
                up.setUsuario(user);
                up.setPermiso(permiso);
                up.setAsignadoEn(LocalDateTime.now());
                usuarioPermisoRepository.save(up);
            });
        });
    }
}