package com.example.demo.repository.academico.plano;

import com.example.demo.domain.model.academico.plano.atividade.EntregaAtividade;
import com.example.demo.repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntregaAtividadeRepository extends BaseRepository<EntregaAtividade, Long> {
    Optional<EntregaAtividade> findByUuid(UUID uuid);
    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);
    Optional<EntregaAtividade> findByAtividade_UuidAndAluno_Uuid(UUID atividadeUuid, UUID alunoUuid);

    <T> List<T> findAllByAtividade_Uuid(UUID atividadeUuid, Class<T> projection);
}
