package com.example.demo.service.academico.plano.resultado;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.plano.resultado.PlanoDisciplinaPeriodo;
import com.example.demo.repository.academico.plano.resultado.PlanoDisciplinaPeriodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanoDisciplinaPeriodoService {

    private final PlanoDisciplinaPeriodoRepository repository;

    @Transactional
    public UUID create(PlanoDisciplinaPeriodo entity) {
        entity.setStatus(Status.ATIVO);
        repository.save(entity);
        return entity.getUuid();
    }

    @Transactional
    public void update(UUID uuid, PlanoDisciplinaPeriodo request) {
        PlanoDisciplinaPeriodo entity = repository
                .findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Plano disciplina período não encontrado."));

        entity.setBoletim(request.getBoletim());
        entity.setPlanoDisciplina(request.getPlanoDisciplina());
        entity.setPeriodo(request.getPeriodo());
        entity.setNota(request.getNota());
        entity.setFaltas(request.getFaltas());
        entity.setObservacao(request.getObservacao());
        repository.save(entity);
    }

    public <T> T findByUuid(UUID uuid, Class<T> projection) {
        return repository
                .findByUuid(uuid, projection)
                .orElseThrow(() -> EurekaException.ofNotFound("Plano disciplina período não encontrado."));
    }

    public List<PlanoDisciplinaPeriodo> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void changeStatus(UUID uuid, Status status) {
        PlanoDisciplinaPeriodo entity = repository
                .findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Plano disciplina período não encontrado."));
        entity.setStatus(status);
        repository.save(entity);
    }
}
