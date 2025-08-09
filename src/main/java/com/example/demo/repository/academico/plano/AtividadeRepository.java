package com.example.demo.repository.academico.plano;

import com.example.demo.domain.model.academico.plano.atividade.Atividade;
import com.example.demo.dto.projection.AtividadeAlunoSummary;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AtividadeRepository extends BaseRepository<Atividade, Long> {
    Optional<Atividade> findByUuid(UUID uuid);
    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);
    <T> Optional<T> findByUuidAndPlanoDisciplinaUuid(UUID uuid, UUID disciplinaUuid, Class<T> projection);
    <T> Page<T> findAllByPlanoDisciplinaUuid(UUID disciplinaUuid, Pageable pageable, Class<T> projection);

    @Query("""
            SELECT a.uuid        AS uuid,
                   a.titulo      AS titulo,
                   a.descricao   AS descricao,
                   a.prazo       AS prazo,
                   a.arquivoUrl  AS arquivoUrl,
                   CASE WHEN ea.entregueEm IS NOT NULL THEN true ELSE false END AS entregue
            FROM Atividade a
                     JOIN a.entregaAtividades ea
                     JOIN ea.aluno al
                     JOIN a.planoDisciplina pd
            WHERE (pd.uuid = :planoDisciplinaUuid)
              AND al.uuid = :alunoUuid
            """)
    Page<AtividadeAlunoSummary> findAllByPlanoDisciplinaUuidAndAlunoUuid(@Param("planoDisciplinaUuid") UUID planoDisciplinaUuid,
                                                                         @Param("alunoUuid") UUID alunoUuid,
                                                                         Pageable pageable);
}
