package com.example.ticketinios.jirapobre.repositories;

import com.example.ticketinios.jirapobre.models.User; 
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsuario(String usuario);
    Optional<User> findByEmail(String email);
}
