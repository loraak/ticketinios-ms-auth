package com.example.ticketinios.jirapobre.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketinios.jirapobre.repositories.PermisoRepository;
import com.example.ticketinios.jirapobre.repositories.UserRepository;
import com.example.ticketinios.jirapobre.repositories.UsuarioPermisoRepository;

import jakarta.transaction.Transactional;

import com.example.ticketinios.jirapobre.dto.AdminUpdateRequest;
import com.example.ticketinios.jirapobre.dto.UsuarioDTO;
import com.example.ticketinios.jirapobre.models.Permiso;
import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.models.UsuarioPermiso;

@Service
public class UsuarioService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private UsuarioPermisoRepository usuarioPermisoRepository;

    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
    }

    public List<UsuarioDTO> listarUsuarios() {
        return userRepository.findAll().stream()
            .map(u -> UsuarioDTO.builder()
                .id(u.getId())
                .nombreCompleto(u.getNombreCompleto())
                .username(u.getUsuario())
                .email(u.getEmail())
                .activo(u.isActivo())
                .permisos(u.getPermisos())
                .build()
            ).toList();
    }

    @Transactional
    public void actualizarPermisos(UUID usuarioId, List<String> nombresPermisos) {
        User user = userRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        usuarioPermisoRepository.deleteByUsuario(user);

        List<Permiso> permisos = permisoRepository.findByNombreIn(nombresPermisos);

        List<UsuarioPermiso> nuevos = permisos.stream()
            .map(p -> {
                UsuarioPermiso up = new UsuarioPermiso();
                up.setUsuario(user);
                up.setPermiso(p);
                up.setAsignadoEn(LocalDateTime.now());
                return up;
            })
            .toList();

        usuarioPermisoRepository.saveAll(nuevos);
    }

    public void actualizarBasico(UUID id, AdminUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        user.setNombreCompleto(request.nombreCompleto());
        user.setEmail(request.email());
        user.setUsuario(request.usuario());

        userRepository.save(user);
    }

    public void reactivar(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        user.setActivo(true);
        userRepository.save(user);
    }
}