package com.example.ticketinios.jirapobre.repositories;

import com.example.ticketinios.jirapobre.models.User; 
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsuario(String usuario);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
