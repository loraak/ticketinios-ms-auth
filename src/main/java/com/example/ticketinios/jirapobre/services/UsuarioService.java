package com.example.ticketinios.jirapobre.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketinios.jirapobre.repositories.UserRepository;
import com.example.ticketinios.jirapobre.models.User;

@Service
public class UsuarioService {
    @Autowired
    private UserRepository userRepository;

    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
    }
}