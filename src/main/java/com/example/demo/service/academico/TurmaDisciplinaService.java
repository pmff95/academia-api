package com.example.demo.service.academico;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.domain.model.academico.TurmaDisciplina;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.dto.academico.TurmaDisciplinaRequest;
import com.example.demo.repository.academico.DisciplinaRepository;
import com.example.demo.repository.academico.TurmaDisciplinaRepository;
import com.example.demo.repository.academico.TurmaRepository;
import com.example.demo.repository.aluno.AlunoRepository;
import com.example.demo.repository.professor.ProfessorRepository;
import com.example.demo.service.academico.plano.PlanoDisciplinaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TurmaDisciplinaService {

    private final TurmaDisciplinaRepository turmaDisciplinaRepository;
    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoDisciplinaService planoDisciplinaService;


    @Transactional
    public UUID create(UUID turmaUuid, TurmaDisciplinaRequest requisicaoDisciplina) {
        Turma turma = turmaRepository.findByUuid(turmaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));

        Professor professor = professorRepository.findByUuid(requisicaoDisciplina.professorUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado."));

        Disciplina disciplina = disciplinaRepository.findByUuid(requisicaoDisciplina.disciplinaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));

        TurmaDisciplina turmaDisciplina = turmaDisciplinaRepository
                .findByTurma_UuidAndDisciplina_Uuid(turmaUuid, requisicaoDisciplina.disciplinaUuid())
                .orElseGet(() -> {
                    TurmaDisciplina nova = new TurmaDisciplina();
                    nova.setTurma(turma);
                    nova.setStatus(Status.ATIVO);
                    return nova;
                });

        turmaDisciplina.setProfessor(professor);
        turmaDisciplina.setDisciplina(disciplina);
        turmaDisciplina.setCargaHoraria(disciplina.getCargaHoraria());
        turmaDisciplina.setAulasSemana(requisicaoDisciplina.aulasSemana());

        turmaDisciplinaRepository.save(turmaDisciplina);
        planoDisciplinaService.criarPlanoDeDisciplinaSeNaoExistir(turmaDisciplina);
        return turmaDisciplina.getUuid();
    }

    @Transactional
    public void create(UUID turmaUuid, List<TurmaDisciplinaRequest> listaTurmaDisciplina) {
        for (TurmaDisciplinaRequest requisicaoDisciplina : listaTurmaDisciplina) {
            create(turmaUuid, requisicaoDisciplina);
        }
    }

    @Transactional
    public void saveAll(UUID turmaUuid, List<TurmaDisciplinaRequest> listaDeDisciplinasDaTurma) {
        for (TurmaDisciplinaRequest requisicaoDisciplina : listaDeDisciplinasDaTurma) {
            TurmaDisciplina turmaDisciplina = buscarOuCriarTurmaDisciplina(turmaUuid, requisicaoDisciplina);
            atualizarTurmaDisciplinaSeNecessario(turmaDisciplina, requisicaoDisciplina);
            planoDisciplinaService.criarPlanoDeDisciplinaSeNaoExistir(turmaDisciplina);
        }
    }

    private TurmaDisciplina buscarOuCriarTurmaDisciplina(UUID turmaUuid, TurmaDisciplinaRequest requisicaoDisciplina) {
        if (requisicaoDisciplina.uuid() != null) {
            return turmaDisciplinaRepository
                    .findByUuidAndTurmaUuid(requisicaoDisciplina.uuid(), turmaUuid, TurmaDisciplina.class)
                    .orElseGet(() -> tentarCriarTurmaDisciplina(turmaUuid, requisicaoDisciplina));
        }

        return turmaDisciplinaRepository
                .findByTurma_UuidAndDisciplina_Uuid(turmaUuid, requisicaoDisciplina.disciplinaUuid())
                .orElseGet(() -> tentarCriarTurmaDisciplina(turmaUuid, requisicaoDisciplina));
    }

    private TurmaDisciplina tentarCriarTurmaDisciplina(UUID turmaUuid, TurmaDisciplinaRequest requisicaoDisciplina) {
        UUID novoUuid = create(turmaUuid, requisicaoDisciplina);
        return turmaDisciplinaRepository.findByUuidAndTurmaUuid(novoUuid, turmaUuid, TurmaDisciplina.class)
                .orElseThrow(() -> EurekaException.ofException("Erro ao criar disciplina da turma."));
    }

    private void atualizarTurmaDisciplinaSeNecessario(TurmaDisciplina disciplinaDaTurma, TurmaDisciplinaRequest requisicaoDisciplina) {
        Disciplina disciplina = disciplinaRepository.findByUuid(requisicaoDisciplina.disciplinaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));

        boolean precisaAtualizar =
                !disciplinaDaTurma.getProfessor().getUuid().equals(requisicaoDisciplina.professorUuid()) ||
                        !disciplinaDaTurma.getDisciplina().getUuid().equals(requisicaoDisciplina.disciplinaUuid()) ||
                        !disciplinaDaTurma.getAulasSemana().equals(requisicaoDisciplina.aulasSemana()) ||
                        !java.util.Objects.equals(disciplinaDaTurma.getCargaHoraria(), disciplina.getCargaHoraria());

        if (precisaAtualizar) {
            Professor professor = professorRepository.findByUuid(requisicaoDisciplina.professorUuid())
                    .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado."));

            disciplinaDaTurma.setProfessor(professor);
            disciplinaDaTurma.setDisciplina(disciplina);
            disciplinaDaTurma.setCargaHoraria(disciplina.getCargaHoraria());
            disciplinaDaTurma.setAulasSemana(requisicaoDisciplina.aulasSemana());
            turmaDisciplinaRepository.save(disciplinaDaTurma);
        }
    }

    @Transactional
    public void update(UUID turmaUuid, UUID uuid, TurmaDisciplinaRequest requisicaoDisciplina) {
        TurmaDisciplina disciplinaDaTurma = turmaDisciplinaRepository.findByUuidAndTurmaUuid(uuid, turmaUuid, TurmaDisciplina.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma/Disciplina não encontrada."));
        Professor professor = professorRepository.findByUuid(requisicaoDisciplina.professorUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado."));
        Disciplina disciplina = disciplinaRepository.findByUuid(requisicaoDisciplina.disciplinaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));

        disciplinaDaTurma.setProfessor(professor);
        disciplinaDaTurma.setDisciplina(disciplina);
        disciplinaDaTurma.setCargaHoraria(disciplina.getCargaHoraria());
        disciplinaDaTurma.setAulasSemana(requisicaoDisciplina.aulasSemana());
        turmaDisciplinaRepository.save(disciplinaDaTurma);
    }

    @Transactional
    public void changeStatus(UUID turmaUuid, UUID uuid, Status status) {
        TurmaDisciplina disciplinaDaTurma = turmaDisciplinaRepository.findByUuidAndTurmaUuid(uuid, turmaUuid, TurmaDisciplina.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma/Disciplina não encontrada."));

        disciplinaDaTurma.setStatus(status);
        turmaDisciplinaRepository.save(disciplinaDaTurma);
    }

    public <T> Page<T> findAll(UUID turmaUuid, Pageable paginacao, Class<T> projection) {
        return turmaDisciplinaRepository.findAllByTurmaUuid(turmaUuid, paginacao, projection);
    }

    public <T> List<T> findAll(UUID turmaUuid, Class<T> projection) {
        return turmaDisciplinaRepository.findAllByTurmaUuid(turmaUuid, projection);
    }

    public <T> T findByUuid(UUID turmaUuid, UUID uuid, Class<T> projection) {
        return turmaDisciplinaRepository.findByUuidAndTurmaUuid(uuid, turmaUuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma/Disciplina não encontrada."));
    }

    public <T> List<T> findByAluno(UUID alunoUuid, Class<T> projection) {
        return turmaDisciplinaRepository.findDisciplinasByAlunoUuid(alunoUuid, projection);
    }

    public <T> List<T> findByAluno(UUID turmaUuid, UUID alunoUuid, Class<T> projection) {
        return turmaDisciplinaRepository.findDisciplinasByAlunoUuidAndTurmaUuid(alunoUuid, turmaUuid, projection);
    }

    public <T> List<T> findByLoggedAluno(Class<T> projection) {
        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        if (currentUser == null)
            throw EurekaException.ofNotFound("Usuário não encontrado.");

        Aluno aluno = alunoRepository.findByUuid(currentUser.getUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Aluno não encontrado."));

        return turmaDisciplinaRepository.findDisciplinasByAlunoUuid(aluno.getUuid(), projection);
    }
}
