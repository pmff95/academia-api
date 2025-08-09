package com.example.demo.service.academico;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.*;
import com.example.demo.dto.academico.AulaHorarioRequest;
import com.example.demo.dto.academico.AulaHorarioVinculoRequest;
import com.example.demo.dto.projection.AulaHorarioSummary;
import com.example.demo.repository.academico.AulaHorarioRepository;
import com.example.demo.repository.academico.AulaHorarioGradeRepository;
import com.example.demo.repository.academico.SerieHorarioAulaRepository;
import com.example.demo.repository.academico.TurmaDisciplinaRepository;
import com.example.demo.repository.academico.TurmaRepository;
import com.example.demo.repository.academico.DisciplinaRepository;
import com.example.demo.repository.academico.GrupoItinerarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AulaHorarioService {

    private final AulaHorarioRepository repository;
    private final TurmaDisciplinaRepository turmaDisciplinaRepository;
    private final SerieHorarioAulaRepository serieHorarioAulaRepository;
    private final TurmaRepository turmaRepository;
    private final AulaHorarioGradeRepository gradeRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final GrupoItinerarioRepository grupoRepository;

    public AulaHorarioService(AulaHorarioRepository repository,
                              TurmaDisciplinaRepository turmaDisciplinaRepository,
                              SerieHorarioAulaRepository serieHorarioAulaRepository,
                              TurmaRepository turmaRepository,
                              AulaHorarioGradeRepository gradeRepository,
                              DisciplinaRepository disciplinaRepository,
                              GrupoItinerarioRepository grupoRepository) {
        this.repository = repository;
        this.turmaDisciplinaRepository = turmaDisciplinaRepository;
        this.serieHorarioAulaRepository = serieHorarioAulaRepository;
        this.turmaRepository = turmaRepository;
        this.gradeRepository = gradeRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public UUID create(UUID turmaDisciplinaUuid, AulaHorarioRequest request) {
        TurmaDisciplina td = turmaDisciplinaRepository.findByUuid(turmaDisciplinaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma/Disciplina não encontrada."));
        AulaHorario ah = new AulaHorario();
        ah.setDia(request.dia());
        ah.setInicio(request.inicio());
        ah.setFim(request.fim());
        ah.setTurmaDisciplina(td);
        ah.setStatus(Status.ATIVO);
        repository.save(ah);
        return ah.getUuid();
    }

    @Transactional
    public void createAll(UUID turmaUuid, List<AulaHorarioVinculoRequest> requests) {
        Turma turma = turmaRepository.findByUuid(turmaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));

        List<AulaHorarioGrade> existentes = gradeRepository.findByTurma_Uuid(turmaUuid);
        gradeRepository.deleteAll(existentes);

        Serie serie = turma.getSerie();

        for (AulaHorarioVinculoRequest request : requests) {
            SerieHorarioAula serieHorarioAula = serieHorarioAulaRepository
                    .findBySerieAndOrdem(serie, request.ordem())
                    .orElseThrow(() -> EurekaException.ofNotFound("Horário padrão não encontrado."));

            if (serieHorarioAula.isIntervalo()) {
                continue;
            }

            Disciplina disciplina = disciplinaRepository.findByUuid(request.disciplinaUuid())
                    .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));

            com.example.demo.domain.model.academico.intinerario.GrupoItinerario grupo = null;
            if (request.grupoItinerarioUuid() != null) {
                grupo = grupoRepository.findByUuid(request.grupoItinerarioUuid())
                        .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));
            }

            AulaHorarioGrade aulaHorario = new AulaHorarioGrade();
            aulaHorario.setDia(request.dia());
            aulaHorario.setInicio(serieHorarioAula.getInicio());
            aulaHorario.setFim(serieHorarioAula.getFim());
            aulaHorario.setTurma(turma);
            aulaHorario.setDisciplina(disciplina);
            aulaHorario.setGrupo(grupo);
            aulaHorario.setStatus(Status.ATIVO);

            gradeRepository.save(aulaHorario);
        }
    }


    @Transactional
    public void update(UUID turmaDisciplinaUuid, UUID uuid, AulaHorarioRequest request) {
        AulaHorario ah = repository.findByUuidAndTurmaDisciplinaUuid(uuid, turmaDisciplinaUuid, AulaHorario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Aula não encontrada."));
        ah.setDia(request.dia());
        ah.setInicio(request.inicio());
        ah.setFim(request.fim());
        repository.save(ah);
    }

    @Transactional
    public void changeStatus(UUID turmaDisciplinaUuid, UUID uuid, Status status) {
        AulaHorario ah = repository.findByUuidAndTurmaDisciplinaUuid(uuid, turmaDisciplinaUuid, AulaHorario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Aula não encontrada."));
        ah.setStatus(status);
        repository.save(ah);
    }

    public Page<AulaHorarioSummary> findAll(UUID turmaDisciplinaUuid, Pageable pageable) {
        return repository.findAllByTurmaDisciplinaUuid(turmaDisciplinaUuid, pageable, AulaHorarioSummary.class);
    }

    public <T> T findByUuid(UUID turmaDisciplinaUuid, UUID uuid, Class<T> projection) {
        return repository.findByUuidAndTurmaDisciplinaUuid(uuid, turmaDisciplinaUuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Aula não encontrada."));
    }

    public <T> java.util.List<T> listarPorTurma(UUID turmaUuid, Class<T> projection) {
        return gradeRepository.findHorarioByTurmaUuid(turmaUuid, projection);
    }

    public <T> java.util.List<T> listarPorTurmaEGrupo(UUID turmaUuid, UUID grupoUuid, Class<T> projection) {
        return gradeRepository.findHorarioByTurmaUuidAndGrupoUuid(turmaUuid, grupoUuid, projection);
    }
}
