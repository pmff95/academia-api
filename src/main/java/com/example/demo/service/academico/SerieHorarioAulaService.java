package com.example.demo.service.academico;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.academico.SerieHorarioAula;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.dto.academico.HorarioAulaRequest;
import com.example.demo.dto.academico.SerieHorarioAulaCadastroRequest;
import com.example.demo.dto.academico.SerieHorarioSerieView;
import com.example.demo.repository.academico.SerieHorarioAulaRepository;
import com.example.demo.repository.academico.SerieHorarioItinerarioRepository;
import com.example.demo.repository.academico.TurmaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SerieHorarioAulaService {

    private final SerieHorarioAulaRepository repository;
    private final TurmaRepository turmaRepository;
    private final SerieHorarioItinerarioRepository itinerarioRepository;

    public SerieHorarioAulaService(SerieHorarioAulaRepository repository,
                                   TurmaRepository turmaRepository,
                                   SerieHorarioItinerarioRepository itinerarioRepository) {
        this.repository = repository;
        this.turmaRepository = turmaRepository;
        this.itinerarioRepository = itinerarioRepository;
    }

    @Transactional
    public void salvarHorarios(SerieHorarioAulaCadastroRequest request) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        List<SerieHorarioAula> entities = new ArrayList<>();
        for (Serie serie : request.series()) {
            List<SerieHorarioAula> antigos = repository.findBySerie(serie);
            repository.deleteAll(antigos);
            for (HorarioAulaRequest h : request.horarios()) {
                SerieHorarioAula sha = new SerieHorarioAula();
                sha.setSerie(serie);
                sha.setEscola(usuarioLogado.getEscola());
                sha.setOrdem(h.ordem());
                sha.setInicio(h.inicio());
                sha.setFim(h.fim());
                sha.setIntervalo(Boolean.TRUE.equals(h.intervalo()));
                entities.add(sha);
            }
        }
        repository.saveAll(entities);
    }

    public <T> List<T> listarPorSerie(Serie serie, Class<T> projection) {
        return repository.findBySerie(serie, projection);
    }

    public <T> List<T> listarPorTurma(UUID turmaUuid, Class<T> projection) {
        Serie serie = turmaRepository.findByUuid(turmaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."))
                .getSerie();
        return repository.findBySerie(serie, projection);
    }

    public List<SerieHorarioSerieView> listarSeriesComStatus() {
        List<SerieHorarioSerieView> list = new ArrayList<>();
        for (Serie s : Serie.values()) {
            list.add(new SerieHorarioSerieView(s, repository.existsBySerie(s)));
        }
        return list;
    }

    @Transactional
    public void removerHorarios(Serie serie) {
        List<SerieHorarioAula> existentes = repository.findBySerie(serie);
        repository.deleteAll(existentes);
    }

    @Transactional
    public void copiarHorarios(Serie destino, Serie origem) {
        if (repository.existsBySerie(destino)) {
            throw EurekaException.ofValidation("Série destino já possui horários cadastrados");
        }

        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();

        List<SerieHorarioAula> listaOrigem = repository.findBySerie(origem);
        if (listaOrigem.isEmpty()) {
            throw EurekaException.ofValidation("Série de origem não possui horários cadastrados");
        }

        List<SerieHorarioAula> novos = new ArrayList<>();
        for (SerieHorarioAula h : listaOrigem) {
            SerieHorarioAula novo = new SerieHorarioAula();
            novo.setSerie(destino);
            novo.setEscola(usuarioLogado.getEscola());
            novo.setOrdem(h.getOrdem());
            novo.setInicio(h.getInicio());
            novo.setFim(h.getFim());
            novo.setIntervalo(h.isIntervalo());
            novos.add(novo);
        }
        repository.saveAll(novos);
    }

    public com.example.demo.dto.academico.SerieHorarioAulaTurmaView listarPorTurmaComItinerarios(java.util.UUID turmaUuid) {
        Turma turma = turmaRepository.findByUuid(turmaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Turma não encontrada."));
        Serie serie = turma.getSerie();

        java.util.List<com.example.demo.dto.projection.SerieHorarioAulaSummary> horarios =
                repository.findBySerie(serie, com.example.demo.dto.projection.SerieHorarioAulaSummary.class);

        java.util.List<com.example.demo.dto.projection.SerieHorarioItinerarioSummary> itinerarios =
                itinerarioRepository.findResumoByTurma(turma, com.example.demo.dto.projection.SerieHorarioItinerarioSummary.class);

        return new com.example.demo.dto.academico.SerieHorarioAulaTurmaView(horarios, itinerarios);
    }

}
