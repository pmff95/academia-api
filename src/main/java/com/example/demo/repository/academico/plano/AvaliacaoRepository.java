package com.example.demo.repository.academico.plano;

import com.example.demo.domain.model.academico.plano.avaliacao.Avaliacao;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvaliacaoRepository extends BaseRepository<Avaliacao, Long> {
    Optional<Avaliacao> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    @Query("""
            SELECT a
            FROM Avaliacao a
                JOIN a.disciplina pd
                JOIN pd.turmaDisciplina td
                JOIN td.turma t
            WHERE t.uuid = :turmaUuid
        """)
    List<Avaliacao> findByTurmaUuid(@Param("turmaUuid") UUID turmaUuid);
}

