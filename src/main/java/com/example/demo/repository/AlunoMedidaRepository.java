package com.example.demo.repository;

import com.example.demo.entity.AlunoMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlunoMedidaRepository extends JpaRepository<AlunoMedida, UUID> {
    List<AlunoMedida> findByAlunoUuid(UUID alunoUuid);

    Optional<AlunoMedida> findTopByAlunoUuidOrderByDataRegistroDesc(UUID alunoUuid);

    List<AlunoMedida> findByAlunoUuidAndDataRegistroAfterOrderByDataRegistroDesc(UUID alunoUuid, LocalDateTime data);

    List<AlunoMedida> findTop10ByAlunoUuidOrderByDataRegistroDesc(UUID alunoUuid);

    void deleteByAlunoUuidAndDataRegistroBefore(UUID alunoUuid, LocalDateTime data);
}
