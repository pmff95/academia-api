package com.example.demo.service.aluno;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.domain.model.academico.intinerario.GrupoItinerario;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.aluno.ResponsavelAluno;
import com.example.demo.domain.model.carteira.Cartao;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.aluno.*;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.dto.projection.aluno.AlunoFull;
import com.example.demo.dto.projection.aluno.AlunoSummary;
import com.example.demo.dto.projection.TurmaSummary;
import com.example.demo.dto.projection.aluno.AlunoSummaryImpl;
import com.example.demo.repository.academico.GrupoItinerarioRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.carteira.CartaoRepository;
import com.example.demo.repository.specification.AlunoSpecification;
import com.example.demo.service.academico.itinerario.AlunoGrupoItinerarioService;
import com.example.demo.service.academico.TurmaService;
import com.example.demo.service.carteira.CarteiraService;
import com.example.demo.service.common.PasswordService;
import com.example.demo.service.common.StorageService;
import com.example.demo.service.usuario.BaseUsuarioService;
import com.example.demo.service.usuario.UsuarioCrudService;
import com.example.demo.service.usuario.UsuarioEmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PasswordService passwordService;
    private final CarteiraService carteiraService;
    private final UsuarioEmailService usuarioEmailService;
    private final ResponsavelAlunoService responsavelAlunoService;
    private final StorageService storageService;
    private final TurmaService turmaService;
    private final UsuarioCrudService usuarioCrudService;
    private final BaseUsuarioService baseUsuarioService;
    private final AlunoGrupoItinerarioService alunoGrupoItinerarioService;
    private final GrupoItinerarioRepository grupoItinerarioRepository;
    private final CartaoRepository cartaoRepository;
    private final AlergiaService alergiaService;
    private final MedicamentoService medicamentoService;

    public Aluno findByUuid(UUID alunoId) {
        return this.alunoRepository.findByUuid(alunoId).orElseThrow(
                () -> EurekaException.ofNotFound("Aluno não encontrado."));
    }

    public Aluno findStudentWithResponsaveisByUuid(UUID alunoId) {
        return this.alunoRepository.findWithResponsaveisByUuid(alunoId).orElseThrow(
                () -> EurekaException.ofNotFound("Aluno não encontrado."));
    }

    @Transactional
    public String create(AlunoRequest request) {

        validarResponsaveisDoRequest(request);

        String senha = passwordService.generateTemporaryPassword();
        String senhaCartao = passwordService.generateTemporaryPin();

        AtomicReference<String> fotoKey = new AtomicReference<>();
        Aluno aluno;
        try {
            aluno = cadastrarAluno(request, senha, fotoKey);

            Cartao novoCartao = carteiraService.criarCarteiraParaAluno(aluno, request.numeroCartao(), senhaCartao);

            cadastrarResponsavel(request, aluno);

            // TODO adicionar if parametro escola
            enviarEmails(request, aluno, novoCartao, senha, senhaCartao);
            return "Aluno cadastrado com sucesso.";
        } catch (RuntimeException e) {
            storageService.deleteFile(fotoKey.get());
            throw e;
        }
    }

    public void update(UUID uuid, AlunoRequest request) {

        Aluno student = this.findStudentWithResponsaveisByUuid(uuid);

        validarResponsaveisDoRequest(request);

        validarDadosAlunoComResponsaveis(student, request.email(), request.cpf());

        usuarioCrudService.updateBasicInfo(student, request.nome(), request.email(), request.cpf(), request.dataNascimento(), null, request.telefone());
        student.setMatriculaManual(request.matricula());
        student.setGrupoSanguineo(request.grupoSanguineo());

        StorageOutput output = storageService.uploadFile(request.foto());
        if (output != null) {
            String previous = student.getFoto();
            if (previous != null && !previous.isBlank()) {
                int lastSlash = previous.lastIndexOf('/');
                String key = lastSlash >= 0 ? previous.substring(lastSlash + 1) : previous;
                storageService.deleteFile(key);
            }
            student.setFoto(output.getUrl());
        }

        this.alunoRepository.save(student);

        vincularItinerario(request, student);

        if (request.responsavelAlunoRequest() != null) {
            responsavelAlunoService.substituirResponsaveis(uuid, request.responsavelAlunoRequest());
        }

        if (request.alergias() != null) {
            alergiaService.substituirLista(uuid, request.alergias());
        }

        if (request.medicamentos() != null) {
            medicamentoService.substituirLista(uuid, request.medicamentos());
        }
    }

//    public Page<AlunoSummary> findAll(AlunoSpecification specification, Pageable pageable) {
//        Sort sort = pageable.getSort().isSorted()
//                ? pageable.getSort()
//                : Sort.by("nome");
//        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
//        //   return this.alunoRepository.findAllProjected(specification, sortedPageable, AlunoSummary.class);
//        //   deu uma cagada aqui em cima, mudei pra opçao de baixo, depois seria bom verificar
//        Page<AlunoSummary> page = this.alunoRepository.findAllProjected(specification, sortedPageable, AlunoSummary.class);
//        return page.map(AlunoSummaryImpl::new);
//    }

    public Page<AlunoSummaryDTO> findAll(AlunoSpecification specification, Pageable pageable) {
        Sort sort = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by("nome");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Aluno> page = this.alunoRepository.findAll(specification, sortedPageable);

        int anoLetivoAtual = Year.now().getValue();

        return page.map(aluno -> {
            Serie serie = aluno.getTurmas().stream()
                    .filter(t -> t.getAnoLetivo() != null && t.getAnoLetivo().equals(anoLetivoAtual))
                    .findFirst()
                    .map(Turma::getSerie)
                    .orElse(null);

            return new AlunoSummaryDTO(
                    aluno.getUuid(),
                    aluno.getNome(),
                    aluno.getFoto(),
                    aluno.getMatricula(),
                    aluno.getCpf(),
                    aluno.getDataNascimento(),
                    serie,
                    aluno.getStatus()
            );
        });
    }

    public <T> T findByUuid(UUID uuid, Class<T> clazz) {
        return this.alunoRepository
                .findByUuid(uuid, clazz)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
    }

    public AlunoFullResponse findFull(UUID uuid) {
        AlunoFull projection = this.alunoRepository
                .findByUuid(uuid, AlunoFull.class)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        String numeroCartao = cartaoRepository
                .findByStatusAndCarteira_Aluno_Uuid(Status.ATIVO, uuid)
                .map(Cartao::getNumero)
                .orElse(null);

        List<ResponsavelAlunoResponse> responsaveis = projection.getResponsaveis()
                .stream()
                .map(r -> new ResponsavelAlunoResponse(
                        r.getResponsavel().getUuid(),
                        r.getResponsavel().getNome(),
                        r.getResponsavel().getEmail(),
                        r.getResponsavel().getCpf(),
                        r.getResponsavel().getTelefone(),
                        r.getGrauParentesco()
                ))
                .toList();

        List<TurmaSummary> turmas = projection.getTurmas();

        List<AlergiaResponse> alergias = alergiaService.listarPorAluno(uuid)
                .stream()
                .map(a -> new AlergiaResponse(
                        a.getUuid(),
                        a.getTipo(),
                        a.getSubstancia(),
                        a.getGravidade(),
                        a.getObservacoes(),
                        a.getDataDiagnostico(),
                        a.getCuidadosEmergenciais()
                ))
                .toList();

        List<MedicamentoResponse> medicamentos = medicamentoService.listarPorAluno(uuid)
                .stream()
                .map(m -> new MedicamentoResponse(
                        m.getUuid(),
                        m.getNome(),
                        m.getDosagem(),
                        m.getFrequencia(),
                        m.getObservacoes()
                ))
                .toList();

        return new AlunoFullResponse(
                projection.getUuid(),
                projection.getNome(),
                projection.getEmail(),
                projection.getCpf(),
                projection.getSexo(),
                projection.getTelefone(),
                projection.getDataNascimento() != null ? projection.getDataNascimento().toString() : null,
                projection.getGrupoSanguineo(),
                projection.getMatricula(),
                projection.getFoto(),
                projection.getStatus(),
                numeroCartao,
                responsaveis,
                turmas,
                alergias,
                medicamentos

        );
    }

    public void changeStudentStatus(UUID uuid, Status status) {

        Aluno student = this.findByUuid(uuid);

        if (student.getStatus() != status) {
            student.setStatus(status);
            this.alunoRepository.save(student);
        }
    }

    private void validarResponsaveisDoRequest(AlunoRequest request) {
        if (request == null || request.responsavelAlunoRequest() == null) {
            return;
        }

        UsuarioRequest resp1 = request.responsavelAlunoRequest().responsavel();
        UsuarioRequest resp2 = request.responsavelAlunoRequest().responsavel2();

        String emailAluno = request.email() != null ? request.email().trim() : null;
        String cpfAluno = request.cpf() != null ? request.cpf().trim().replaceAll("\\D", "") : null;

        validarDadosComAluno(emailAluno, cpfAluno, resp1, "responsável");

        if (resp2 != null) {
            validarDadosComAluno(emailAluno, cpfAluno, resp2, "responsável 2");
            validarDadosEntreResponsaveis(resp1, resp2);
        }
    }

    private void validarDadosComAluno(String emailAluno, String cpfAluno, UsuarioRequest resp, String label) {
        if (resp == null) return;

        String emailResp = resp.email() != null ? resp.email().trim() : null;
        String cpfResp = resp.cpf() != null ? resp.cpf().trim().replaceAll("\\D", "") : null;

        if (emailResp != null && emailAluno != null && emailResp.equalsIgnoreCase(emailAluno)) {
            throw EurekaException.ofValidation("Email do " + label + " não pode ser igual ao email do aluno.");
        }

        if (cpfResp != null && cpfAluno != null && cpfResp.equals(cpfAluno)) {
            throw EurekaException.ofValidation("CPF do " + label + " não pode ser igual ao CPF do aluno.");
        }
    }

    private void validarDadosEntreResponsaveis(UsuarioRequest r1, UsuarioRequest r2) {
        if (r1 == null || r2 == null) return;

        String email1 = r1.email() != null ? r1.email().trim() : null;
        String email2 = r2.email() != null ? r2.email().trim() : null;
        if (email1 != null && email2 != null && email1.equalsIgnoreCase(email2)) {
            throw EurekaException.ofValidation("Emails dos responsáveis não podem ser iguais.");
        }

        String cpf1 = r1.cpf() != null ? r1.cpf().trim().replaceAll("\\D", "") : null;
        String cpf2 = r2.cpf() != null ? r2.cpf().trim().replaceAll("\\D", "") : null;
        if (cpf1 != null && cpf2 != null && cpf1.equals(cpf2)) {
            throw EurekaException.ofValidation("CPFs dos responsáveis não podem ser iguais.");
        }
    }

    private void validarDadosAlunoComResponsaveis(Aluno aluno, String email, String cpf) {
        String trimmedEmail = email != null ? email.trim() : null;
        String cleanedCpf = cpf != null ? cpf.trim().replaceAll("\\D", "") : null;

        if (aluno.getResponsaveis() == null) {
            return;
        }

        for (ResponsavelAluno r : aluno.getResponsaveis()) {
            Usuario resp = r.getResponsavel();
            if (resp == null) {
                continue;
            }

            if (trimmedEmail != null && resp.getEmail() != null && trimmedEmail.equalsIgnoreCase(resp.getEmail())) {
                throw EurekaException.ofValidation("Email do aluno não pode ser igual ao do responsável.");
            }

            if (cleanedCpf != null && resp.getCpf() != null && cleanedCpf.equals(resp.getCpf().replaceAll("\\D", ""))) {
                throw EurekaException.ofValidation("CPF do aluno não pode ser igual ao do responsável.");
            }
        }
    }


    private Aluno cadastrarAluno(AlunoRequest request, String senha, AtomicReference<String> fotoKey) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        Escola escola = usuarioLogado.getEscola();

        String fotoUrl = null;
        if (request.foto() != null && !request.foto().isEmpty()) {
            StorageOutput upload = storageService.uploadFile(request.foto());
            fotoUrl = upload.getUrl();
            fotoKey.set(upload.getKey());
        }

        Turma turma = turmaService.findByUuid(request.turmaUuid(), Turma.class);

        Aluno aluno = baseUsuarioService.construirUsuario(
                new Aluno(),
                escola,
                Perfil.ALUNO,
                request.nome(),
                null,
                null,
                request.email(),
                request.cpf(),
                request.dataNascimento(),
                request.sexo(),
                request.telefone(),
                fotoUrl,
                request.matricula(),
                senha
        );

        aluno.setGrupoSanguineo(request.grupoSanguineo());

            Aluno savedAluno = alunoRepository.save(aluno);

        turmaService.adicionarAlunoTurma(turma.getUuid(), savedAluno);

        if (request.alergias() != null && !request.alergias().isEmpty())
            alergiaService.salvarLista(savedAluno.getUuid(), request.alergias());

        if (request.medicamentos() != null && !request.medicamentos().isEmpty())
            medicamentoService.salvarLista(savedAluno.getUuid(), request.medicamentos());

        vincularItinerario(request, savedAluno);

        return savedAluno;
    }


    private void cadastrarResponsavel(AlunoRequest request, Aluno newStudent) {
        if (request.responsavelAlunoRequest() != null) {
            responsavelAlunoService.create(request.responsavelAlunoRequest(), newStudent.getUuid());
        }
    }

    private void enviarEmails(AlunoRequest request, Aluno student, Cartao novoCartao, String senha, String senhaCartao) {
        List<String> emailsCartao = List.of();
        String numeroCartao = null;

        if (novoCartao != null) {
            numeroCartao = novoCartao.getNumero();
            emailsCartao = Stream.concat(
                    Stream.ofNullable(student.getEmail()).filter(e -> !e.isBlank()),
                    student.getResponsaveis().stream()
                            .map(ResponsavelAluno::getResponsavel)
                            .map(Usuario::getEmail)
                            .filter(e -> e != null && !e.isBlank())
            ).toList();
        }

        String infoLogin = usuarioEmailService.montarInfoLogin(student);
        usuarioEmailService.enviarEmailBoasVindas(
                student.getNome(),
                student.getEmail(),
                senha,
                infoLogin,
                emailsCartao,
                numeroCartao,
                senhaCartao
        );
    }

    private void vincularItinerario(AlunoRequest request, Aluno aluno) {
        if (request.itinerarioUuid() == null) {
            return;
        }

        Turma turma = turmaService.findByUuid(request.turmaUuid(), Turma.class);
        Serie serie = turma.getSerie();

        if (serie == Serie.PRIMEIRO_MEDIO || serie == Serie.SEGUNDO_MEDIO) {
            GrupoItinerario grupo = grupoItinerarioRepository
                    .findFirstByItinerarioUuidAndAnoLetivo(request.itinerarioUuid(), turma.getAnoLetivo())
                    .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));

            alunoGrupoItinerarioService.create(
                    request.itinerarioUuid(),
                    grupo.getUuid(),
                    new com.example.demo.dto.academico.AlunoGrupoItinerarioRequest(aluno.getUuid())
            );
        }
    }


}
