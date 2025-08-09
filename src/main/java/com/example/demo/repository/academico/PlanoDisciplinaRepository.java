package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PlanoDisciplinaRepository extends BaseRepository<PlanoDisciplina, Long> {
    Optional<PlanoDisciplina> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    Optional<PlanoDisciplina> findByTurmaDisciplina_Uuid(UUID turmaDisciplinaUuid);

    Optional<PlanoDisciplina> findByDisciplinaGrupo_Uuid(UUID disciplinaGrupoUuid);

    @Query("""
                SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
                FROM PlanoDisciplina p
                    LEFT JOIN p.turmaDisciplina td
                    LEFT JOIN p.disciplinaGrupo dg
                WHERE td.uuid = :uuid
                   OR dg.uuid = :uuid
            """)
    boolean existsByTurmaItinerario(@Param("uuid") UUID turmaItinerarioUuid);
}
