package com.example.ticketinios.jirapobre.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketinios.jirapobre.models.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, UUID> {
    Optional<Permiso> findByNombre(String nombre);

    List<Permiso> findByNombreIn(List<String> nombres);
}