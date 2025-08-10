package com.example.demo.service;

import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public String registrarUltimoAcesso(String login) {
        return repository.findByCpfOrEmailOrTelefone(login, login, login)
                .map(usuario -> {
                    usuario.setUltimoAcesso(LocalDateTime.now());
                    repository.save(usuario);
                    return "Último acesso registrado";
                }).orElse("Usuário não encontrado");
    }
}
