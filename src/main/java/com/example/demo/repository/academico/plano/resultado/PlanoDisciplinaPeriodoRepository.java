package com.example.demo.repository.academico.plano.resultado;

import com.example.demo.domain.model.academico.plano.resultado.PlanoDisciplinaPeriodo;
import com.example.demo.repository.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlanoDisciplinaPeriodoRepository extends BaseRepository<PlanoDisciplinaPeriodo, Long> {
    Optional<PlanoDisciplinaPeriodo> findByUuid(UUID uuid);
    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);
}
