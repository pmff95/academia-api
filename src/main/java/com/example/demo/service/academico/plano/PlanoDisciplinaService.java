package com.example.demo.service.academico.plano;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.TurmaDisciplina;
import com.example.demo.domain.model.academico.intinerario.DisciplinaGrupoItinerario;
import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.repository.academico.PlanoDisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanoDisciplinaService {
    private final PlanoDisciplinaRepository repository;

    public void criarPlanoDeDisciplinaSeNaoExistir(BaseEntity turmaItinerarioDisciplina) {
        boolean planoJaExiste = repository.existsByTurmaItinerario(turmaItinerarioDisciplina.getUuid());

        if (!planoJaExiste) {
            PlanoDisciplina novoPlanoDisciplina = new PlanoDisciplina();

            switch (turmaItinerarioDisciplina) {
                case TurmaDisciplina td -> {
                    novoPlanoDisciplina.setTurmaDisciplina(td);
                }
                case DisciplinaGrupoItinerario dgi -> {
                    novoPlanoDisciplina.setDisciplinaGrupo(dgi);
                }
                default -> {
                    throw EurekaException.ofValidation("Tipo não suportado!");
                }
            }

            novoPlanoDisciplina.setStatus(Status.ATIVO);
            novoPlanoDisciplina.setAnoLetivo(java.time.LocalDate.now().getYear());

            repository.save(novoPlanoDisciplina);
        }
    }

    public PlanoDisciplina findByUuid(UUID uuid) {
        return repository
                .findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Plano pisciplina não encontrado."));
    }
}
