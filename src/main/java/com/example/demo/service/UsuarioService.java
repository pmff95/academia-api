package com.example.demo.service;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String registrarUltimoAcesso(String login) {
        return repository.findByCpfOrEmailOrTelefone(login, login, login)
                .map(usuario -> {
                    usuario.setUltimoAcesso(LocalDateTime.now());
                    repository.save(usuario);
                    return "Último acesso registrado";
                }).orElse("Usuário não encontrado");
    }

    public String criarUsuarioMaster(UsuarioDTO dto) {
        if (repository.existsByPerfil(Perfil.MASTER)) {
            return "Usuário master já existe";
        }

        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setPerfil(Perfil.MASTER);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        repository.save(usuario);
        return "Usuário master criado com sucesso";
    }
}
