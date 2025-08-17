package com.example.demo.repository;

import com.example.demo.entity.TreinoSessao;
import com.example.demo.domain.enums.StatusTreino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreinoSessaoRepository extends JpaRepository<TreinoSessao, UUID> {
    List<TreinoSessao> findByAlunoUuid(UUID alunoUuid);

    Optional<TreinoSessao> findByAlunoUuidAndExercicio_UuidAndData(UUID alunoUuid, UUID exercicioUuid, LocalDate data);

    long countByAlunoUuidAndExercicio_Categoria_UuidAndDataAndStatus(UUID alunoUuid, UUID categoriaUuid, LocalDate data, StatusTreino status);

    List<TreinoSessao> findByAlunoUuidAndData(UUID alunoUuid, LocalDate data);

    List<TreinoSessao> findByAlunoUuidAndDataBeforeOrderByDataDesc(UUID alunoUuid, LocalDate data);
}
