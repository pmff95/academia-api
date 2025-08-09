package com.example.demo.service.academico;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.dto.academico.TurmaRequest;
import com.example.demo.dto.projection.TurmaSummary;
import com.example.demo.dto.projection.aluno.AlunoSummary;
import com.example.demo.repository.academico.TurmaRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.specification.TurmaSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TurmaService {

    private final TurmaRepository repository;
    private final AlunoRepository alunoRepository;

    public TurmaService(TurmaRepository repository, AlunoRepository alunoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void create(TurmaRequest request) {
        if (repository.existsByNomeIgnoreCaseAndTurno(request.nome(), request.turno())) {
            throw EurekaException.ofValidation("Turma já cadastrada para este turno.");
        }
        Turma turma = new Turma();
        turma.setNome(request.nome());
        turma.setSerie(request.serie());
        turma.setTurno(request.turno());
        turma.setAnoLetivo(request.anoLetivo());
        turma.setLimiteVagas(request.limiteVagas());

        repository.save(turma);
    }

    @Transactional
    public void update(UUID uuid, TurmaRequest request) {
        Turma turma = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));
        turma.setNome(request.nome());
        turma.setSerie(request.serie());
        turma.setTurno(request.turno());
        turma.setAnoLetivo(request.anoLetivo());
        turma.setLimiteVagas(request.limiteVagas());

        repository.save(turma);
    }

    public Page<TurmaSummary> findAll(TurmaSpecification specification, Pageable pageable) {
        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by("nome");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllProjected(specification, sortedPageable, TurmaSummary.class);
    }

    public <T> T findByUuid(UUID uuid, Class<T> projection) {
        return repository.findByUuid(uuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));
    }

    public <T> List<T> findBySerie(Serie serie, Class<T> projection) {
        return repository.findAllBySerie(serie, projection);
    }

    public void adicionarAlunoTurma(UUID uuid, Aluno aluno) {
        Turma turma = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));

        if (turma.getLimiteVagas() != null && turma.getAlunos().size() >= turma.getLimiteVagas()) {
            throw EurekaException.ofValidation("Turma atingiu o limite de vagas.");
        }

        turma.getAlunos().add(aluno);

        repository.save(turma);
    }

    public List<AlunoSummary> listarAlunos(UUID turmaUuid) {
        return repository.findAlunosByTurmaUuid(turmaUuid, AlunoSummary.class);
    }

    public void montarVinculoTurmaProfessorDisciplina(Professor professor) {
        // tem que acresentar mais alguns parametros do objeto
    }
}
