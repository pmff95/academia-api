package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.Disciplina;
import com.example.demo.repository.BaseRepository;
import com.example.demo.domain.enums.NivelEnsino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplinaRepository extends BaseRepository<Disciplina, Long> {
    Optional<Disciplina> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> type);

    <T> Page<T> findAllProjectedBy(Pageable pageable, Class<T> type);

    <T> Page<T> findAllByNivelEnsinoIn(List<NivelEnsino> niveis,
                                       Pageable pageable,
                                       Class<T> projection);

    List<Disciplina> findAllByUuidIn(List<UUID> uuids);

    boolean existsByNomeIgnoreCaseAndNivelEnsinoAndEscola(String nome,
                                                          NivelEnsino nivelEnsino,
                                                          com.example.demo.domain.model.instituicao.Escola escola);
}
