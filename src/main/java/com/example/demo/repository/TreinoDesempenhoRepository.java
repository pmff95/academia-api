package com.example.demo.repository;

import com.example.demo.entity.TreinoDesempenho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreinoDesempenhoRepository extends JpaRepository<TreinoDesempenho, UUID> {
    Optional<TreinoDesempenho> findByAluno_UuidAndCategoria_UuidAndData(UUID alunoUuid, UUID categoriaUuid, LocalDate data);

    List<TreinoDesempenho> findByAluno_UuidOrderByDataAsc(UUID alunoUuid);

    List<TreinoDesempenho> findByAluno_UuidAndDataBetweenOrderByData(UUID alunoUuid, LocalDate inicio, LocalDate fim);
}
