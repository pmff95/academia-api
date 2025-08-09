package com.example.demo.service.usuario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.JwtService;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.usuario.*;
import com.example.demo.service.common.PasswordService;
import com.example.demo.repository.aluno.ResponsavelAlunoRepository;
import com.example.demo.repository.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;
    private final ResponsavelAlunoRepository responsavelAlunoRepository;

    public JwtAuthenticationResponse signin(LoginRequest request) {
        log.info("Tentativa de login para o usuário: {}", request.login());

        // Verifica se o usuário existe antes de autenticar
        usuarioRepository.buscarUsuarioAtivoComEscolaPorLogin(request.login())
                .orElseThrow(() -> EurekaException.ofNotFound("Usuário não encontrado."));

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.login(), request.password())
            );
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            log.warn("Senha inválida para o usuário: {}", request.login());
            throw EurekaException.ofValidation("Senha inválida");
        } catch (UsernameNotFoundException ex) {
            // Caso o usuário não seja encontrado durante a autenticação
            log.warn("Usuário não encontrado: {}", request.login());
            throw EurekaException.ofNotFound("Usuário não encontrado.");
        }

        UsuarioLogado usuarioLogado = (UsuarioLogado) auth.getPrincipal();

        String token = jwtService.generateToken(usuarioLogado);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), usuarioLogado);

        usuarioService.registrarUltimoAcesso(usuarioLogado.getUsername());

        log.info("Login bem-sucedido para o usuário: {}", request.login());

        return new JwtAuthenticationResponse(token, refreshToken);
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String login = this.jwtService.extractUsername(request.token());

        Usuario user = this.usuarioService.findByMatriculaComEscola(login);
        UsuarioLogado usuarioLogado = user.toUsuarioLogado();

        if (!jwtService.isTokenValid(request.token())) {
            log.warn("Tentativa de refresh token inválido para o usuário: {}", login);
            throw new IllegalArgumentException("Token inválido");
        }

        String token = jwtService.generateToken(usuarioLogado);

        log.info("Refresh token gerado com sucesso para o usuário: {}", login);

        return new JwtAuthenticationResponse(token, request.token());
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Iniciando recuperação de senha para a matrícula: {}", request.matricula());

        Usuario usuario;

        // Fluxo utilizando email
        if (request.email() != null && !request.email().isBlank() && request.dataNascimento() == null) {
            usuario = usuarioRepository.buscarUsuarioAtivoComEscolaPorLogin(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            if (usuario.getEmail() == null || !usuario.getEmail().equalsIgnoreCase(request.email())) {
                throw EurekaException.ofValidation("Email inválido");
            }

            String novaSenha = passwordService.generateTemporaryPassword();
            usuario.setSenha(passwordService.validateAndEncode(novaSenha));
            usuario.setPrimeiroAcesso(true);
            usuarioService.save(usuario);
            usuarioService.enviarEmailRecuperacaoSenha(usuario.getNome(), usuario.getEmail(), novaSenha);

            log.info("Senha redefinida e enviada para o email do usuário {}", usuario.getMatricula());

            return;
        }

        usuario = usuarioRepository.buscarUsuarioAtivoComEscolaPorLogin(request.matricula())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (usuario.getDataNascimento() == null) {
            log.warn("Usuário {} não possui data de nascimento cadastrada.", request.matricula());
            throw EurekaException.ofException("Data de nascimento não cadastrada para este usuário.");
        }
        if (request.dataNascimento() == null) {
            throw EurekaException.ofValidation("Data de nascimento deve ser informada");
        }

        if (!usuario.getDataNascimento().equals(request.dataNascimento())) {
            throw EurekaException.ofValidation("Data de nascimento inválida");
        }

        boolean validado = false;
        String cpf = usuario.getCpf();
        if (cpf != null && !cpf.isBlank() && request.cpfTresDigitos() != null && !request.cpfTresDigitos().isBlank()) {
            String cpfLimpo = cpf.replaceAll("\\D", "");
            validado = cpfLimpo.startsWith(request.cpfTresDigitos());
        }

        if (!validado && request.ultimoNomeMae() != null && usuario.getMae() != null) {
            String[] partes = usuario.getMae().trim().split("\\s+");
            String ultimoNome = partes[partes.length - 1];
            validado = ultimoNome.equalsIgnoreCase(request.ultimoNomeMae());
        }

        if (!validado) {
            throw EurekaException.ofValidation("Informações para validação não conferem");
        }

        if (request.novaSenha() == null || request.novaSenha().isBlank()) {
            throw EurekaException.ofValidation("Nova senha deve ser informada");
        }

        String novaSenha = request.novaSenha();
        usuario.setSenha(passwordService.validateAndEncode(novaSenha));
        usuarioService.save(usuario);

        log.info("Senha redefinida com sucesso para o usuário {}", usuario.getMatricula());
    }

    public void alterarSenha(TrocarSenhaRequest request) {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();

//        if (currentUser.possuiPerfil(Perfil.ALUNO)) {
//            Boolean alunoPodeTrocarSenha =
//                    responsavelAlunoRepository.existsPeloMenosUmResponsavelComPrimeiroAcesso(currentUser.getUuid());
//            if (alunoPodeTrocarSenha != null && !alunoPodeTrocarSenha)
//                throw EurekaException.ofValidation("O responsável ainda não habilitou sua conta.");
//        }

        if (!passwordService.matches(request.senhaAntiga(), currentUser.getPassword()))
            throw EurekaException.ofValidation("Senha incorreta.");

        if (!request.novaSenha().equals(request.confirmarNovaSenha()))
            throw EurekaException.ofValidation("Senha e confirmação não conferem.");

        Usuario usuario = usuarioRepository.findByUuid(currentUser.getUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Usuário não encontrado."));
        usuario.setPrimeiroAcesso(false);
        usuario.setSenha(passwordService.validateAndEncode(request.novaSenha()));

        usuarioRepository.save(usuario);
    }

    public void definirSenha(UUID uuid, String senha) {
        if (senha == null || !senha.matches("\\d{6,}")) {
            throw EurekaException.ofValidation("Senha deve ser numérica e possuir no mínimo 6 dígitos");
        }

        Usuario usuario = usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Usuário não encontrado."));
        usuario.setSenha(passwordService.validateAndEncode(senha));
        usuario.setPrimeiroAcesso(false);
        usuarioRepository.save(usuario);
    }

}
