package com.example.ticketinios.jirapobre.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketinios.jirapobre.models.User;
import com.example.ticketinios.jirapobre.models.UsuarioPermiso;

@Repository
public interface UsuarioPermisoRepository extends JpaRepository<UsuarioPermiso, UUID> {
    void deleteByUsuarioId(UUID usuarioId);

    void deleteByUsuario(User usuario);
}
