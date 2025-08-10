package com.example.demo.service;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.util.SenhaUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository repository, ModelMapper mapper, PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Usuario buscarPorLogin(String login) {
        return repository.findByCpfOrEmailOrTelefone(login, login, login).orElse(null);
    }

    public void registrarUltimoAcesso(Usuario usuario) {
        usuario.setUltimoAcesso(LocalDateTime.now());
        repository.save(usuario);
    }

    public String criarUsuarioMaster(UsuarioDTO dto) {
        if (repository.existsByPerfil(Perfil.MASTER)) {
            return "Usuário master já existe";
        }

        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setPerfil(Perfil.MASTER);
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        usuario.setSenha(passwordEncoder.encode(senha));
        repository.save(usuario);
        emailService.enviarSenha(usuario.getEmail(), senha);
        return "Usuário master criado com sucesso";
    }

    public String reenviarSenha(String login) {
        return repository.findByCpfOrEmailOrTelefone(login, login, login)
                .map(usuario -> {
                    String novaSenha = SenhaUtil.gerarSenhaNumerica(6);
                    usuario.setSenha(passwordEncoder.encode(novaSenha));
                    repository.save(usuario);
                    emailService.enviarSenha(usuario.getEmail(), novaSenha);
                    return "Senha reenviada com sucesso";
                }).orElse("Usuário não encontrado");
    }

    public String alterarSenha(String login, String senhaAtual, String novaSenha) {
        return repository.findByCpfOrEmailOrTelefone(login, login, login)
                .map(usuario -> {
                    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
                        return "Senha atual incorreta";
                    }
                    usuario.setSenha(passwordEncoder.encode(novaSenha));
                    repository.save(usuario);
                    return "Senha alterada com sucesso";
                }).orElse("Usuário não encontrado");
    }

    public ApiResponse<UsuarioDTO> buscarUsuarioLogado() {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();

        if (usuarioLogado == null) {
            return new ApiResponse<>(false, "Usuário não autenticado", null, null);
        }

        return repository.findByUuid(usuarioLogado.getUuid())
                .map(u -> new ApiResponse<>(true, null, mapper.map(u, UsuarioDTO.class), null))
                .orElse(new ApiResponse<>(false, "Usuário não encontrado", null, null));
    }
}
