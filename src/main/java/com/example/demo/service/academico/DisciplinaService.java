package com.example.demo.service.academico;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.dto.academico.DisciplinaRequest;
import com.example.demo.dto.academico.DisciplinaCreateRequest;
import com.example.demo.dto.academico.DisciplinaView;
import com.example.demo.dto.projection.DisciplinaSummary;
import com.example.demo.repository.academico.DisciplinaRepository;
import com.example.demo.repository.academico.TurmaDisciplinaRepository;
import com.example.demo.repository.academico.DisciplinaGrupoItinerarioRepository;
import com.example.demo.repository.academico.PlanoDisciplinaRepository;
import com.example.demo.repository.specification.DisciplinaSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.demo.domain.enums.NivelEnsino;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DisciplinaService {

    private final DisciplinaRepository repository;
    private final TurmaDisciplinaRepository turmaDisciplinaRepository;
    private final DisciplinaGrupoItinerarioRepository disciplinaGrupoItinerarioRepository;
    private final PlanoDisciplinaRepository planoDisciplinaRepository;

    public DisciplinaService(DisciplinaRepository repository, TurmaDisciplinaRepository turmaDisciplinaRepository, DisciplinaGrupoItinerarioRepository disciplinaGrupoItinerarioRepository, PlanoDisciplinaRepository planoDisciplinaRepository) {
        this.repository = repository;
        this.turmaDisciplinaRepository = turmaDisciplinaRepository;
        this.disciplinaGrupoItinerarioRepository = disciplinaGrupoItinerarioRepository;
        this.planoDisciplinaRepository = planoDisciplinaRepository;
    }

    @Transactional
    public List<UUID> create(DisciplinaCreateRequest request) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        List<UUID> ids = new java.util.ArrayList<>();
        for (NivelEnsino nivel : request.niveisEnsino()) {
            boolean exists = repository.existsByNomeIgnoreCaseAndNivelEnsinoAndEscola(
                    request.nome(), nivel, usuarioLogado.getEscola());
            if (exists) {
                throw EurekaException.ofValidation("Disciplina já cadastrada para o nível: " + nivel.name() + ". Remova este nível antes de salvar novamente");
            }
            Disciplina disciplina = new Disciplina();
            disciplina.setNome(request.nome());
            disciplina.setNivelEnsino(nivel);
            disciplina.setItinerario(request.itinerario());
            disciplina.setCargaHoraria(request.cargaHoraria());
            disciplina.setStatus(Status.ATIVO);
            disciplina.setEscola(usuarioLogado.getEscola());
            repository.save(disciplina);
            ids.add(disciplina.getUuid());
        }
        return ids;
    }

    @Transactional
    public void update(UUID uuid, DisciplinaRequest request) {
        Disciplina disciplina = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));
        disciplina.setNome(request.nome());
        disciplina.setItinerario(request.itinerario());
        disciplina.setCargaHoraria(request.cargaHoraria());
        repository.save(disciplina);
    }

    @Transactional
    public void changeStatus(UUID uuid, Status status) {
        Disciplina disciplina = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));
        disciplina.setStatus(status);
        repository.save(disciplina);
    }

    public List<DisciplinaView> findAll(DisciplinaSpecification specification) {
        List<Disciplina> list = repository.findAll(specification, Sort.by("nome"));
        return list.stream()
                .map(d -> new DisciplinaView(
                        d.getUuid(),
                        d.getNome(),
                        d.getNivelEnsino().getDescricao(),
                        d.isItinerario(),
                        d.getCargaHoraria()))
                .toList();
    }

    public List<DisciplinaView> findByNiveis(List<NivelEnsino> niveis) {
        Pageable sorted = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("nome"));
        if (niveis == null || niveis.isEmpty()) {
            return repository
                    .findAllProjectedBy(sorted, DisciplinaSummary.class)
                    .map(s -> new DisciplinaView(
                            s.getUuid(),
                            s.getNome(),
                            NivelEnsino.valueOf(s.getNivelEnsino()).getDescricao(),
                            s.getItinerario(),
                            s.getCargaHoraria()))
                    .getContent();
        }
        return repository
                .findAllByNivelEnsinoIn(niveis, sorted, DisciplinaSummary.class)
                .map(s -> new DisciplinaView(
                        s.getUuid(),
                        s.getNome(),
                        NivelEnsino.valueOf(s.getNivelEnsino()).getDescricao(),
                        s.getItinerario(),
                        s.getCargaHoraria()))
                .getContent();
    }

    public <T> T findByUuid(UUID uuid, Class<T> projection) {
        return repository.findByUuid(uuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));
    }

    public DisciplinaView findByUuid(UUID uuid) {
        DisciplinaSummary s = findByUuid(uuid, DisciplinaSummary.class);
        return new DisciplinaView(
                s.getUuid(),
                s.getNome(),
                NivelEnsino.valueOf(s.getNivelEnsino()).getDescricao(),
                s.getItinerario(),
                s.getCargaHoraria());
    }

    @Transactional
    public void delete(UUID uuid) {
        Disciplina disciplina = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Disciplina não encontrada."));
        boolean emUso = turmaDisciplinaRepository.findFirstByDisciplinaUuid(uuid).isPresent()
                || disciplinaGrupoItinerarioRepository.findFirstByDisciplinaUuid(uuid).isPresent();
        if (emUso) {
            throw EurekaException.ofValidation("Disciplina está sendo utilizada e não pode ser removida.");
        }
        repository.delete(disciplina);
    }

}
