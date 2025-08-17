package com.example.demo.repository;

import com.example.demo.entity.TreinoDesempenho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TreinoDesempenhoRepository extends JpaRepository<TreinoDesempenho, UUID> {
    Optional<TreinoDesempenho> findByAluno_UuidAndCategoria_UuidAndData(UUID alunoUuid, UUID categoriaUuid, LocalDate data);
}
