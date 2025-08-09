package com.example.demo.service.usuario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.service.common.PasswordService;
import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.MetodoAutenticacao;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.dto.parametro.ParamsView;
import com.example.demo.dto.projection.usuario.UsuarioFull;
import com.example.demo.dto.projection.usuario.UsuarioSummary;
import com.example.demo.dto.usuario.CurrentUserView;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.repository.aluno.ResponsavelAlunoRepository;
import com.example.demo.repository.instituicao.EscolaRepository;
import java.time.LocalDate;
import com.example.demo.repository.instituicao.EscolaModuloRepository;
import com.example.demo.domain.model.instituicao.EscolaModulo;
import com.example.demo.domain.enums.NomeModulo;
import com.example.demo.repository.specification.UsuarioSpecification;
import com.example.demo.repository.usuario.UsuarioRepository;
import com.example.demo.service.common.StorageService;
import com.example.demo.service.controle.ControleMatriculaService;
import com.example.demo.service.parametro.ParametroService;
import com.example.demo.service.usuario.BaseUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioCrudService {

    private final PasswordService passwordService;
    private final EscolaRepository escolaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ResponsavelAlunoRepository responsavelAlunoRepository;
    private final StorageService storageService;
    private final ControleMatriculaService controleMatriculaService;
    private final ParametroService parametroService;
    private final UsuarioEmailService usuarioEmailService;
    private final BaseUsuarioService baseUsuarioService;
    private final EscolaModuloRepository escolaModuloRepository;

    @Transactional
    public UUID createUser(UsuarioRequest request) {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        Perfil perfil = request.perfil();
        validateProfileAssignmentPermission(currentUser, perfil);

        StorageOutput output = storageService.uploadFile(request.foto());

        String senha = passwordService.generateNumericPassword(8);

        Escola escola = getEscola(request.escolaId() != null ? request.escolaId() : currentUser.getEscola().getUuid());

        if (perfil == Perfil.ADMIN && usuarioRepository.findByEscolaIdAndPerfil(escola.getUuid(), Perfil.ADMIN).isPresent()) {
            throw EurekaException.ofValidation("Escola já possui um administrador.");
        }

        Usuario user = baseUsuarioService.construirUsuario(
                new Usuario(),
                escola,
                perfil,
                request.nome(),
                null,
                null,
                request.email(),
                request.cpf(),
                null,
                null,
                request.telefone(),
                output != null ? output.getUrl() : null,
                request.matricula(),
                senha
        );
        usuarioRepository.save(user);

        String infoLogin = usuarioEmailService.montarInfoLogin(user);
        usuarioEmailService.enviarEmailNovoUsuario(user.getNome(), user.getEmail(), senha, infoLogin);

        return user.getUuid();
    }

    @Transactional
    public void registrarUltimoAcesso(String matricula) {
        Usuario usuario = usuarioRepository.buscarUsuarioAtivoComEscolaPorMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com matricula: " + matricula));

        usuario.setUltimoAcesso(java.time.LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    public void updateBasicInfo(Usuario usuario, String nome, String email, String cpf, LocalDate dataNascimento, Sexo sexo, String telefone) {
        String trimmedEmail = email != null ? email.trim() : null;
        if (trimmedEmail != null && !trimmedEmail.isBlank() &&
                usuarioRepository.existsByEmailAndEscolaUuidAndUuidNot(trimmedEmail,
                        usuario.getEscola().getUuid(), usuario.getUuid())) {
            throw EurekaException.ofValidation("O email " + trimmedEmail + " já está cadastrado para esta escola");
        }

        String cleanedCpf = cpf != null ? cpf.trim().replaceAll("\\D", "") : null;
        if (cleanedCpf != null && !cleanedCpf.isBlank() &&
                usuarioRepository.existsByCpfAndEscolaUuidAndUuidNot(cleanedCpf, usuario.getEscola().getUuid(), usuario.getUuid())) {
            throw EurekaException.ofValidation("CPF: "+ cleanedCpf + " já está cadastrado para esta escola");
        }

        String cleanedTelefone = telefone != null ? telefone.trim().replaceAll("\\D", "") : null;
        if (cleanedTelefone != null && !cleanedTelefone.isBlank() &&
                usuarioRepository.existsByTelefoneAndUuidNot(cleanedTelefone, usuario.getUuid())) {
            throw EurekaException.ofValidation(cleanedTelefone + " já está cadastrado");
        }

        usuario.setNome(nome);
        usuario.setEmail(trimmedEmail);
        usuario.setCpf(cleanedCpf);
        if (dataNascimento != null) {
            usuario.setDataNascimento(dataNascimento);
        }
        if (sexo != null) {
            usuario.setSexo(sexo);
        }
        usuario.setTelefone(cleanedTelefone);
    }

    public void updateUser(UUID uuid, UsuarioRequest request) {
        Usuario user = findByUuid(uuid);

        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        Perfil perfil = request.perfil();
        validateProfileAssignmentPermission(currentUser, perfil);

        updateBasicInfo(user, request.nome(), request.email(), request.cpf(), null, null, request.telefone());

        UUID escolaId = request.escolaId() != null ?
                request.escolaId() :
                currentUser.getEscola() != null ? currentUser.getEscola().getUuid() : null;

        Escola escola = getEscola(escolaId);

        if (perfil == Perfil.ADMIN) {
            usuarioRepository.findByEscolaIdAndPerfil(escola.getUuid(), Perfil.ADMIN)
                    .filter(admin -> !admin.getUuid().equals(user.getUuid()))
                    .ifPresent(a -> { throw EurekaException.ofValidation("Escola já possui um administrador."); });
        }

        StorageOutput output = storageService.uploadFile(request.foto());

        user.setEscola(escola);
        user.setPerfil(perfil);
        if (output != null) {
            user.setFoto(output.getUrl());
        }

        usuarioRepository.save(user);
    }

    public Usuario findByUuid(UUID uuid) {
        return usuarioRepository.findByUuid(uuid).orElseThrow(
                () -> EurekaException.ofNotFound("Usuário não encontrado."));
    }

    public void changeUserStatus(UUID uuid, Status status) {
        Usuario user = findByUuid(uuid);

        if (user.getStatus() != status) {
            if (status == Status.INATIVO) {
                // TODO verificar dependentes
            }
            user.setStatus(status);
            usuarioRepository.save(user);
        }
    }

    public Usuario findByMatriculaComEscola(String matricula) {
        return usuarioRepository.buscarUsuarioAtivoComEscolaPorMatricula(matricula).orElseThrow(
                () -> EurekaException.ofNotFound("Usuário não encontrado."));
    }

    public Page<UsuarioSummary> findAll(UsuarioSpecification specification, Pageable pageable) {
        return usuarioRepository.findAllProjected(specification, pageable, UsuarioSummary.class);
    }

    public <T> T findByUuid(UUID uuid, Class<T> clazz) {
        return usuarioRepository
                .findByUuid(uuid, clazz)
                .orElseThrow(() -> EurekaException.ofNotFound("Usuario não encontrado."));
    }

    public Optional<UsuarioFull> findByEscolaIdAndPerfil(UUID escolaId, Perfil perfil) {
        return usuarioRepository.findByEscolaIdAndPerfil(escolaId, perfil);
    }

    public CurrentUserView findCurrent() {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        List<ParamsView> params = new ArrayList<>();
        String escolaNome = null;
        String logoUrl = null;
        String fotoUrl = usuarioLogado.getFoto();
        GrupoSanguineo grupoSanguineo = usuarioLogado.getGrupoSanguineo();
        LocalDate dataNascimento = usuarioLogado.getDataNascimento();
        List<NomeModulo> modulos = new ArrayList<>();
        if (usuarioLogado.getEscola() != null) {
            params = parametroService.listarParametrosPorEscolaPorTipo(usuarioLogado.getEscola().getUuid(), TipoParametro.VISUAL);
            escolaNome = usuarioLogado.getEscola().getNome();
            if (usuarioLogado.getPerfil() == Perfil.ADMIN) {
                logoUrl = parametroService.retornaValorParametro("logo_url", false).valor();
            }
            modulos = escolaModuloRepository.findByEscolaUuid(usuarioLogado.getEscola().getUuid()).stream()
                    .filter(EscolaModulo::isValido)
                    .map(EscolaModulo::getModulo)
                    .toList();
        }
        if (logoUrl == null) {
            logoUrl = fotoUrl;
        }
        return new CurrentUserView(
                usuarioLogado.getName(),
                usuarioLogado.getEmail(),
                usuarioLogado.getCpf(),
                usuarioLogado.getPrimeiroAcesso(),
                usuarioLogado.getPerfil(),
                usuarioLogado.getStatus(),
                usuarioLogado.getMatricula(),
                usuarioLogado.getTelefone(),
                usuarioLogado.getMatriculaManual(),
                escolaNome,
                params,
                logoUrl,
                fotoUrl,
                grupoSanguineo,
                dataNascimento,
                modulos,
                usuarioLogado.getUuid());
    }

    public void save(Usuario user) {
        usuarioRepository.save(user);
    }

    public void trocarResponsavelDaEscola(UUID uuid) {
        Usuario user = usuarioRepository.findByUuid(uuid).orElseThrow(
                () -> EurekaException.ofNotFound("Usuário não encontrado."));
        user.setPerfil(Perfil.ADMIN);
        usuarioRepository.save(user);
    }

    private Escola getEscola(UUID uuid) {
        return escolaRepository.findByUuid(uuid).orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));
    }

    private void validateProfileAssignmentPermission(UsuarioLogado currentUser, Perfil requestedPerfil) {
        if (currentUser.possuiPerfil(Perfil.ADMIN) && requestedPerfil == Perfil.MASTER) {
            throw EurekaException.ofValidation("Operação não permitida");
        }

        if (currentUser.possuiPerfil(Perfil.FUNCIONARIO) && requestedPerfil == Perfil.ADMIN && !currentUser.possuiPerfil(Perfil.ADMIN)) {
            throw EurekaException.ofValidation("Operação não permitida");
        }
    }
}
