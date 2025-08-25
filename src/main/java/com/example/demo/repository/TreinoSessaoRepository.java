package com.example.demo.repository;

import com.example.demo.entity.TreinoSessao;
import com.example.demo.domain.enums.StatusTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreinoSessaoRepository extends JpaRepository<TreinoSessao, UUID> {
    List<TreinoSessao> findByAlunoUuid(UUID alunoUuid);

    Optional<TreinoSessao> findByAluno_UuidAndExercicio_UuidAndData(UUID alunoUuid, UUID exercicioUuid, LocalDate data);

    @Query("SELECT s FROM TreinoSessao s " +
            "WHERE s.aluno.uuid = :alunoUuid " +
            "AND s.exercicio.uuid = :exercicioUuid " +
            "AND (s.status = :status OR s.status IS NULL) " +
            "ORDER BY s.data ASC")
    Optional<TreinoSessao> findFirstByAlunoAndExercicioWithStatusOrNull(
            @Param("alunoUuid") UUID alunoUuid,
            @Param("exercicioUuid") UUID exercicioUuid,
            @Param("status") StatusTreino status);


    long countByAlunoUuidAndExercicio_Categoria_UuidAndDataAndStatus(UUID alunoUuid, UUID categoriaUuid, LocalDate data, StatusTreino status);

    List<TreinoSessao> findByAlunoUuidAndData(UUID alunoUuid, LocalDate data);

    List<TreinoSessao> findByAlunoUuidAndDataAfter(UUID alunoUuid, LocalDate data);

    List<TreinoSessao> findByAlunoUuidAndDataBeforeOrderByDataDesc(UUID alunoUuid, LocalDate data);

    void deleteByAlunoUuidAndDataAfter(UUID alunoUuid, LocalDate data);

    void deleteByAlunoUuid(UUID alunoUuid);

    boolean existsByExercicio_Uuid(UUID exercicioUuid);
}
