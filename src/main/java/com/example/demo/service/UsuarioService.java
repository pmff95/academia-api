package com.example.demo.service;

import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.dto.AlunoPagamentoDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Tema;
import com.example.demo.common.util.SenhaUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AlunoPagamentoService alunoPagamentoService;

    public UsuarioService(UsuarioRepository repository, ModelMapper mapper, PasswordEncoder passwordEncoder,
                          EmailService emailService, AlunoPagamentoService alunoPagamentoService) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.alunoPagamentoService = alunoPagamentoService;
    }

    public Usuario buscarPorLogin(String login) {
        return repository.findByCpfOrEmailOrTelefoneOrNick(login, login, login, login).orElse(null);
    }

    @Transactional
    public String registrarUltimoAcesso(Usuario usuario) {
        usuario.setUltimoAcesso(LocalDateTime.now());
        repository.save(usuario);
        return "Acesso registrado";
    }

    @Transactional
    public String criarUsuarioMaster() {
        Usuario usuario = new Usuario();
        usuario.setPerfil(Perfil.MASTER);
        usuario.setNumero("119");
        usuario.setCep("60320740");
        usuario.setNome("Paulo Mesquita");
        usuario.setCpf("60929106326");
        usuario.setLogradouro("Rua José Alexandre");
        usuario.setLogradouro("paulomesquita0@gmail.com");
        usuario.setUf("CE");
        usuario.setCidade("Fortaleza");
        String senha = "123456";
        usuario.setSenha(passwordEncoder.encode(senha));
        repository.save(usuario);
        emailService.enviarSenha(usuario.getEmail(), senha);
        return "Usuário master criado com sucesso";
    }

    @Transactional
    public String reenviarSenha(String login) {
        return repository.findByCpfOrEmailOrTelefoneOrNick(login, login, login, login)
                .map(usuario -> {
                    String novaSenha = SenhaUtil.gerarSenhaNumerica(6);
                    usuario.setSenha(passwordEncoder.encode(novaSenha));
                    repository.save(usuario);
                    emailService.enviarSenha(usuario.getEmail(), novaSenha);
                    return "Senha reenviada com sucesso";
                }).orElse("Usuário não encontrado");
    }

    @Transactional
    public String alterarSenha(String login, String senhaAtual, String novaSenha) {
        return repository.findByCpfOrEmailOrTelefoneOrNick(login, login, login, login)
                .map(usuario -> {
                    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
                        return "Senha atual incorreta";
                    }
                    usuario.setSenha(passwordEncoder.encode(novaSenha));
                    repository.save(usuario);
                    return "Senha alterada com sucesso";
                }).orElse("Usuário não encontrado");
    }

    @Transactional
    public String alterarAtivo(UUID uuid, boolean ativo) {
        return repository.findByUuid(uuid)
                .map(usuario -> {
                    usuario.setAtivo(ativo);
                    repository.save(usuario);
                    return ativo ? "Usuário ativado" : "Usuário desativado";
                }).orElse("Usuário não encontrado");
    }

    @Transactional
    public String alterarTema(Tema tema) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuarioLogado == null) {
            return "Usuário não autenticado";
        }
        return repository.findByUuid(usuarioLogado.getUuid())
                .map(usuario -> {
                    usuario.setTema(tema);
                    repository.save(usuario);
                    return "Tema atualizado";
                }).orElse("Usuário não encontrado");
    }

    public ApiReturn<UsuarioDTO> buscarUsuarioLogado() {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();

        if (usuarioLogado == null) {
            throw EurekaException.ofUnauthorized("Usuário não autenticado");
        }

        return repository.findByUuid(usuarioLogado.getUuid())
                .map(u -> {
                    UsuarioDTO dto = mapper.map(u, UsuarioDTO.class);

                    if (!Perfil.MASTER.equals(u.getPerfil())) {
                        boolean exibirPatrocinadores = false;
                        boolean exibirMarketplace = false;

                        if (u.getAcademia() != null) {
                            exibirPatrocinadores = u.getAcademia().isExibirPatrocinadores();
                            exibirMarketplace = u.getAcademia().isExibirMarketplace();
                        }

                        dto.setExibirPatrocinadores(exibirPatrocinadores);
                        dto.setExibirMarketplace(exibirMarketplace);

                        if (Perfil.ALUNO.equals(u.getPerfil())) {
                            AlunoPagamentoDTO pagamento = alunoPagamentoService.buscarUltimoPagamento(u.getUuid());
                            dto.setUltimoPagamento(pagamento);
                        }
                    }

                    return ApiReturn.of(dto);
                })
                .orElseThrow(() -> EurekaException.ofNotFound("Usuário não encontrado"));
    }
}
