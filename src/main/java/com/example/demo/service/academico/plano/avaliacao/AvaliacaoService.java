package com.example.demo.service.academico.plano.avaliacao;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.TurmaDisciplina;
import com.example.demo.domain.model.academico.intinerario.DisciplinaGrupoItinerario;
import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.academico.plano.avaliacao.Avaliacao;
import com.example.demo.dto.academico.plano.avaliacao.CalendarioProvaRequest;
import com.example.demo.dto.academico.plano.avaliacao.CalendarioProvaResponse;
import com.example.demo.repository.academico.DisciplinaGrupoItinerarioRepository;
import com.example.demo.repository.academico.TurmaDisciplinaRepository;
import com.example.demo.repository.academico.PlanoDisciplinaRepository;
import com.example.demo.repository.academico.plano.AvaliacaoRepository;
import com.example.demo.service.academico.plano.PlanoDisciplinaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository repository;
    private final TurmaDisciplinaRepository turmaDisciplinaRepository;
    private final DisciplinaGrupoItinerarioRepository disciplinaGrupoItinerarioRepository;
    private final PlanoDisciplinaRepository planoDisciplinaRepository;
    private final PlanoDisciplinaService planoDisciplinaService;

    @Transactional
    public String criarCalendario(CalendarioProvaRequest request) {
        if (request.dados() == null || request.dados().isEmpty()) {
            throw EurekaException.ofValidation("Informe ao menos um conjunto de dados.");
        }

        for (CalendarioProvaRequest.ItemCalendarioRequest item : request.dados()) {
            List<UUID> disciplinas = item.disciplinas();
            List<LocalDate> datas = item.datas();
            if (disciplinas == null || disciplinas.isEmpty()) {
                throw EurekaException.ofValidation("Informe ao menos uma disciplina.");
            }
            if (datas == null || datas.isEmpty()) {
                throw EurekaException.ofValidation("Informe ao menos uma data.");
            }

            for (UUID disciplinaUuid : disciplinas) {
                TurmaDisciplina td = turmaDisciplinaRepository.findByUuid(disciplinaUuid).orElse(null);
                if (td != null) {
                    if (!td.getTurma().getUuid().equals(request.turmaUuid())) {
                        throw EurekaException.ofValidation("Disciplina não pertence à turma informada.");
                    }
                    planoDisciplinaService.criarPlanoDeDisciplinaSeNaoExistir(td);
                    PlanoDisciplina plano = planoDisciplinaRepository
                            .findByTurmaDisciplina_Uuid(disciplinaUuid)
                            .orElseThrow(() -> EurekaException.ofNotFound("Plano disciplina não encontrado."));
                    for (LocalDate data : datas) {
                        criarAvaliacao(plano, data, "Prova");
                    }
                    continue;
                }

                DisciplinaGrupoItinerario dgi = disciplinaGrupoItinerarioRepository.findByUuid(disciplinaUuid)
                        .orElseThrow(() -> EurekaException.ofNotFound("Disciplina do itinerário não encontrada."));
                planoDisciplinaService.criarPlanoDeDisciplinaSeNaoExistir(dgi);
                PlanoDisciplina plano = planoDisciplinaRepository
                        .findByDisciplinaGrupo_Uuid(disciplinaUuid)
                        .orElseThrow(() -> EurekaException.ofNotFound("Plano disciplina não encontrado."));
                for (LocalDate data : datas) {
                    criarAvaliacao(plano, data, "Prova");
                }
            }
        }
        return "Calendário de provas criado com sucesso.";
    }

    public CalendarioProvaResponse buscarCalendario(UUID turmaUuid) {
        List<Avaliacao> avaliacoes = repository.findByTurmaUuid(turmaUuid);
        List<CalendarioProvaResponse.ItemCalendarioResponse> itens = avaliacoes.stream()
                .map(a -> {
                    UUID disciplinaUuid;
                    PlanoDisciplina plano = a.getDisciplina();
                    if (plano.getTurmaDisciplina() != null) {
                        disciplinaUuid = plano.getTurmaDisciplina().getUuid();
                    } else if (plano.getDisciplinaGrupo() != null) {
                        disciplinaUuid = plano.getDisciplinaGrupo().getUuid();
                    } else {
                        disciplinaUuid = plano.getUuid();
                    }
                    return new CalendarioProvaResponse.ItemCalendarioResponse(disciplinaUuid, a.getData(), a.getTitulo());
                })
                .toList();
        return new CalendarioProvaResponse(turmaUuid, itens);
    }

    private void criarAvaliacao(PlanoDisciplina plano, LocalDate data, String titulo) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setTitulo(titulo);
        avaliacao.setTipo("PROVA");
        avaliacao.setData(data);
        avaliacao.setDisciplina(plano);
        avaliacao.setStatus(Status.ATIVO);
        repository.save(avaliacao);
    }
}

