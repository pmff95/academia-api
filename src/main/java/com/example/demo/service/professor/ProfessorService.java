package com.example.demo.service.professor;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.projection.*;
import com.example.demo.service.academico.TurmaService;
import com.example.demo.service.common.PasswordService;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.domain.model.carteira.Cartao;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.professor.HorarioDisponivel;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.domain.model.usuario.Endereco;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.dto.professor.HorarioDisponivelCadastroRequest;
import com.example.demo.dto.professor.HorarioDisponivelRequest;
import com.example.demo.dto.professor.ProfessorRequest;
import com.example.demo.dto.professor.ProfessorFullResponse;
import com.example.demo.dto.professor.DisciplinaSerieResponse;
import com.example.demo.dto.professor.ProfessorAulasResponse;
import com.example.demo.dto.projection.ProfessorAulaHorarioView;
import com.example.demo.dto.projection.ProfessorAulaHorarioItinerarioView;
import com.example.demo.domain.enums.NivelEnsino;
import com.example.demo.dto.usuario.EnderecoRequest;
import com.example.demo.dto.usuario.EnderecoResponse;
import com.example.demo.repository.academico.DisciplinaRepository;
import com.example.demo.repository.professor.ProfessorRepository;
import com.example.demo.repository.professor.HorarioDisponivelRepository;
import com.example.demo.repository.academico.TurmaDisciplinaRepository;
import com.example.demo.repository.academico.AulaHorarioGradeRepository;
import com.example.demo.repository.carteira.CartaoRepository;
import com.example.demo.repository.specification.ProfessorSpecification;
import com.example.demo.repository.usuario.EnderecoRepository;
import com.example.demo.service.carteira.CarteiraService;
import com.example.demo.service.usuario.UsuarioEmailService;
import com.example.demo.service.common.StorageService;
import com.example.demo.service.usuario.UsuarioCrudService;
import com.example.demo.service.usuario.BaseUsuarioService;
import com.example.demo.event.ProfessorCreatedEvent;
import com.example.demo.service.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final PasswordService passwordService;
    private final CarteiraService carteiraService;
    private final CartaoRepository cartaoRepository;
    private final UsuarioEmailService usuarioEmailService;
    private final StorageService storageService;
    private final DisciplinaRepository disciplinaRepository;
    private final EnderecoRepository enderecoRepository;
    private final UsuarioCrudService usuarioCrudService;
    private final BaseUsuarioService baseUsuarioService;
    private final TurmaService turmaService;
    private final TurmaDisciplinaRepository turmaDisciplinaRepository;
    private final AulaHorarioGradeRepository gradeRepository;
    private final ApplicationEventPublisher eventPublisher;


    public List<TurmaDisciplinaView> listarSugestoesProfessorLogado() {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        Professor professor = professorRepository.findById(currentUser.getId())
                .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado"));

        return turmaDisciplinaRepository.buscarSugestoesPorProfessorId(professor.getId());
    }


    public Professor findByUuid(UUID professorId) {
        return this.professorRepository.findByUuid(professorId).orElseThrow(
                () -> EurekaException.ofNotFound("Professor não encontrado."));
    }

    @Transactional
    public UUID create(ProfessorRequest request, MultipartFile foto) {
        String senha = passwordService.generateTemporaryPassword();
        Escola escola = SecurityUtils.getUsuarioLogado().getEscola();
        String fotoUrl = null;
        String fotoKey = null;
        if (foto != null && !foto.isEmpty()) {
            StorageOutput storage = storageService.uploadFile(foto);
            fotoUrl = storage.getUrl();
            fotoKey = storage.getKey();
        }

        String senhaCartao = request.numeroCartao() != null ? passwordService.generateTemporaryPin() : null;

        try {
            Professor professor = salvarProfessor(request, senha, escola, fotoUrl);

            eventPublisher.publishEvent(
                    new ProfessorCreatedEvent(professor, request.numeroCartao(), senha, senhaCartao)
            );

            return professor.getUuid();
        } catch (RuntimeException e) {
            if (fotoKey != null) {
                storageService.deleteFile(fotoKey);
            }
            throw e;
        }
    }

    @Transactional
    protected Professor salvarProfessor(ProfessorRequest request, String senha, Escola escola, String fotoUrl) {
        Professor professor = construirProfessor(request, senha, escola, fotoUrl);

//        turmaService.montarVinculoTurmaProfessorDisciplina(professor  );

        if (request.horariosDisponiveis() != null) {
            vincularHorariosDisponiveis(professor, request.horariosDisponiveis());
        }
        if (!request.disciplinasIds().isEmpty()) {
            vincularDisciplinas(professor, request.disciplinasIds());
        }

        professorRepository.save(professor);

        if (request.endereco() != null) {
            cadastrarEndereco(professor, request.endereco());
        }

        return professor;
    }

    public void update(UUID uuid, ProfessorRequest request, MultipartFile foto) {
        Professor professor = this.findByUuid(uuid);

        usuarioCrudService.updateBasicInfo(professor, request.nome(), request.email(), request.cpf(), request.dataNascimento(), request.sexo(), request.telefone());
        professor.setSexo(request.sexo());

        String novaFotoUrl = null;
        String novaFotoKey = null;

        try {
            if (foto != null && !foto.isEmpty()) {
                StorageOutput storage = storageService.uploadFile(foto);
                novaFotoUrl = storage.getUrl();
                novaFotoKey = storage.getKey();
                professor.setFoto(novaFotoUrl);
            }

            professor.setMae(request.mae());
            professor.setPai(request.pai());

            if (request.disciplinasIds() != null && !request.disciplinasIds().isEmpty()) {
                vincularDisciplinas(professor, request.disciplinasIds());
            }

            if (request.horariosDisponiveis() != null) {
                vincularHorariosDisponiveis(professor, request.horariosDisponiveis());
            }

            professorRepository.save(professor);

            if (request.endereco() != null) {
                cadastrarEndereco(professor, request.endereco());
            }

        } catch (RuntimeException e) {
            if (novaFotoKey != null) {
                storageService.deleteFile(novaFotoKey);
            }
            throw e;
        }
    }


    private Professor construirProfessor(ProfessorRequest request, String senha, Escola escola, String foto) {
        return baseUsuarioService.construirUsuario(
                new Professor(),
                escola,
                Perfil.PROFESSOR,
                request.nome(),
                request.mae(),
                request.pai(),
                request.email(),
                request.cpf(),
                request.dataNascimento(),
                request.sexo(),
                request.telefone(),
                foto,
                request.matricula(),
                senha
        );
    }

    private void vincularDisciplinas(Professor professor, List<UUID> disciplinasIds) {
        if (disciplinasIds == null || disciplinasIds.isEmpty()) return;

        List<Disciplina> disciplinas = disciplinaRepository.findAllByUuidIn(disciplinasIds);
        if (disciplinas.size() != disciplinasIds.size()) {
            throw EurekaException.ofValidation("Uma ou mais disciplinas não foram encontradas.");
        }
        professor.setDisciplinas(disciplinas);
    }

    public boolean vincularHorariosDisponiveis(HorarioDisponivelCadastroRequest request) {
        Professor professor = findByUuid(request.uuid());

        vincularHorariosDisponiveis(professor, request.horarios());

        return true;
    }

    private void vincularHorariosDisponiveis(Professor professor, List<HorarioDisponivelRequest> horariosDisponiveisRequest) {
        if (horariosDisponiveisRequest == null) return;

        Map<UUID, HorarioDisponivel> existentes = professor.getHorariosDisponiveis()
                .stream()
                .collect(Collectors.toMap(HorarioDisponivel::getUuid, h -> h));

        List<HorarioDisponivel> atualizados = new ArrayList<>();

        for (HorarioDisponivelRequest req : horariosDisponiveisRequest) {
            HorarioDisponivel horario = null;
            if (req.uuid() != null) {
                horario = existentes.remove(req.uuid());
            }

            if (horario == null) {
                horario = new HorarioDisponivel();
                horario.setProfessor(professor);
            }

            horario.setDia(req.dia());
            horario.setInicio(req.inicio());
            horario.setFim(req.fim());
            horario.setTurno(req.turno());

            atualizados.add(horario);
        }

        professor.getHorariosDisponiveis().clear();
        professor.getHorariosDisponiveis().addAll(atualizados);
    }


    private void cadastrarEndereco(Professor professor, EnderecoRequest enderecoRequest) {

        Endereco endereco = new Endereco();
        endereco.setCep(enderecoRequest.cep());
        endereco.setEndereco(enderecoRequest.endereco());
        endereco.setNumero(enderecoRequest.numero());
        endereco.setCidade(enderecoRequest.cidade());
        endereco.setEstado(enderecoRequest.estado());
        endereco.setUsuario(professor);

        enderecoRepository.save(endereco);
    }

    public Page<ProfessorSummary> findAll(ProfessorSpecification specification, Pageable pageable) {
        Sort sort = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by("nome");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.professorRepository.findAllProjected(specification, sortedPageable, ProfessorSummary.class);
    }

    public List<HorarioDisponivelSummary> listarHorariosDisponiveis(UUID professorUuid) {
        return horarioDisponivelRepository
                .findAllByProfessorUuid(professorUuid, HorarioDisponivelSummary.class);
    }

    public ProfessorAulasResponse listarAulas(UUID professorUuid) {
        List<ProfessorAulaHorarioView> aulas = gradeRepository
                .findHorarioByProfessorUuid(professorUuid, ProfessorAulaHorarioView.class);

        List<ProfessorAulaHorarioItinerarioView> itinerarios = gradeRepository
                .findHorarioItinerarioByProfessorUuid(professorUuid, ProfessorAulaHorarioItinerarioView.class);

        return new ProfessorAulasResponse(aulas, itinerarios);
    }

    public List<ProfessorDisciplinaView> listarProfessoresComDisciplinas() {
        return professorRepository.findAllByStatus(Status.ATIVO);
    }

    public ProfessorFullResponse findCurrent() {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        if (usuario == null) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        return findFull(usuario.getUuid());
    }

    public ProfessorFullResponse findFull(UUID uuid) {
        ProfessorFull projection = this.professorRepository
                .findByUuid(uuid, ProfessorFull.class)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

        EnderecoResponse endereco = enderecoRepository
                .findByUsuarioId(projection.getId(), EnderecoResponse.class)
                .orElse(null);

        String numeroCartao = cartaoRepository
                .findByStatusAndCarteira_Professor_Uuid(Status.ATIVO, uuid)
                .map(Cartao::getNumero)
                .orElse(null);

        var disciplinaRows = turmaDisciplinaRepository
                .findDisciplinasByProfessorUuid(uuid, DisciplinaSerieRow.class);

        var seriesMap = disciplinaRows.stream()
                .collect(java.util.stream.Collectors.toMap(
                        DisciplinaSerieRow::getDisciplinaUuid,
                        r -> r.getSerie().name(),
                        (s1, s2) -> s1
                ));

        var disciplinaList = projection.getDisciplinas().stream()
//                .filter(d -> seriesMap.containsKey(d.getUuid()))
                .map(d -> new DisciplinaSerieResponse(
                        d.getUuid(),
                        d.getNome(),
                        NivelEnsino.valueOf(d.getNivelEnsino()).getDescricao(),
                        seriesMap.getOrDefault(d.getUuid(), null)
//                        seriesMap.get(d.getUuid())
                ))
                .toList();


        return new ProfessorFullResponse(
                projection.getUuid(),
                projection.getNome(),
                projection.getEmail(),
                projection.getCpf(),
                projection.getGrupoSanguineo(),
                projection.getMatricula(),
                projection.getFoto(),
                projection.getTelefone(),
                projection.getDataNascimento(),
                projection.getSexo(),
                projection.getStatus(),
                projection.getPai(),
                projection.getMae(),
                endereco,
                numeroCartao,
                projection.getHorariosDisponiveis(),
                disciplinaList
        );
    }


    public <T> T findByUuid(UUID uuid, Class<T> clazz) {
        return this.professorRepository
                .findByUuid(uuid, clazz)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
    }

    public void changeStudentStatus(UUID uuid, Status status) {

        Professor student = this.findByUuid(uuid);

        if (student.getStatus() != status) {
            student.setStatus(status);
            this.professorRepository.save(student);
        }
    }

    private void enviarEmails(Professor professor, Cartao novoCartao, String senha, String senhaCartao) {
        List<String> emailsCartao = List.of();
        String numeroCartao = null;

        if (novoCartao != null) {
            numeroCartao = novoCartao.getNumero();
            emailsCartao = professor.getEmail() == null || professor.getEmail().isBlank()
                    ? List.of()
                    : List.of(professor.getEmail());
        }

        String infoLogin = usuarioEmailService.montarInfoLogin(professor);
        usuarioEmailService.enviarEmailBoasVindas(
                professor.getNome(),
                professor.getEmail(),
                senha,
                infoLogin,
                emailsCartao,
                numeroCartao,
                senhaCartao
        );
    }


}
