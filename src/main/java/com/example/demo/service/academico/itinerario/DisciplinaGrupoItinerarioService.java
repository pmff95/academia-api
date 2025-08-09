package com.example.demo.service.academico.itinerario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.domain.model.academico.intinerario.DisciplinaGrupoItinerario;
import com.example.demo.domain.model.academico.intinerario.GrupoItinerario;
import com.example.demo.domain.model.professor.Professor;
import com.example.demo.dto.academico.DisciplinaGrupoItinerarioRequest;
import com.example.demo.dto.projection.DisciplinaGrupoItinerarioSummary;
import com.example.demo.repository.academico.DisciplinaGrupoItinerarioRepository;
import com.example.demo.repository.academico.DisciplinaRepository;
import com.example.demo.repository.academico.GrupoItinerarioRepository;
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
public class DisciplinaGrupoItinerarioService {

    private final DisciplinaGrupoItinerarioRepository repository;
    private final GrupoItinerarioRepository grupoRepository;
    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final PlanoDisciplinaService planoDisciplinaService;

    @Transactional
    public UUID create(UUID itinerarioUuid, UUID grupoUuid, DisciplinaGrupoItinerarioRequest request) {
        GrupoItinerario grupo = grupoRepository.findByUuidAndItinerarioUuid(grupoUuid, itinerarioUuid, GrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Grupo não encontrado."));
        Professor professor = professorRepository.findByUuid(request.professorUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado."));
        Disciplina disciplina = disciplinaRepository.findByUuid(request.disciplinaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));

        DisciplinaGrupoItinerario dgi = new DisciplinaGrupoItinerario();
        dgi.setGrupo(grupo);
        dgi.setProfessor(professor);
        dgi.setDisciplina(disciplina);
        dgi.setCargaHoraria(disciplina.getCargaHoraria());
        dgi.setAulasSemana(request.aulasSemana());
        dgi.setStatus(Status.ATIVO);
        repository.save(dgi);

        planoDisciplinaService.criarPlanoDeDisciplinaSeNaoExistir(dgi);
        return dgi.getUuid();
    }

    @Transactional
    public void update(UUID itinerarioUuid, UUID grupoUuid, UUID uuid, DisciplinaGrupoItinerarioRequest request) {
        DisciplinaGrupoItinerario dgi = repository.findByUuidAndGrupoUuid(uuid, grupoUuid, DisciplinaGrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
        Professor professor = professorRepository.findByUuid(request.professorUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Professor não encontrado."));
        Disciplina disciplina = disciplinaRepository.findByUuid(request.disciplinaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));

        dgi.setProfessor(professor);
        dgi.setDisciplina(disciplina);
        dgi.setAulasSemana(request.aulasSemana());
        dgi.setCargaHoraria(disciplina.getCargaHoraria());
        repository.save(dgi);

        planoDisciplinaService.criarPlanoDeDisciplinaSeNaoExistir(dgi);
    }

    @Transactional
    public void changeStatus(UUID itinerarioUuid, UUID grupoUuid, UUID uuid, Status status) {
        DisciplinaGrupoItinerario dgi = repository.findByUuidAndGrupoUuid(uuid, grupoUuid, DisciplinaGrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
        dgi.setStatus(status);
        repository.save(dgi);
    }

    public Page<DisciplinaGrupoItinerarioSummary> findAll(UUID grupoUuid, Pageable pageable) {
        return repository.findAllByGrupoUuid(grupoUuid, pageable, DisciplinaGrupoItinerarioSummary.class);
    }

    public <T> Page<T> findAll(UUID grupoUuid, Pageable pageable, Class<T> projection) {
        return repository.findAllByGrupoUuid(grupoUuid, pageable, projection);
    }

    public <T> List<T> findAll(UUID grupoUuid, Class<T> projection) {
        return repository.findAllByGrupoUuid(grupoUuid, projection);
    }

    public <T> T findByUuid(UUID grupoUuid, UUID uuid, Class<T> projection) {
        return repository.findByUuidAndGrupoUuid(uuid, grupoUuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
    }

    @Transactional
    public void delete(UUID itinerarioUuid, UUID grupoUuid, UUID uuid) {
        DisciplinaGrupoItinerario dgi = repository.findByUuidAndGrupoUuid(uuid, grupoUuid, DisciplinaGrupoItinerario.class)
                .orElseThrow(() -> EurekaException.ofNotFound("Registro não encontrado."));
        repository.delete(dgi);
    }
}
