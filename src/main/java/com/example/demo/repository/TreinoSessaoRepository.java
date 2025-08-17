package com.example.demo.repository;

import com.example.demo.entity.TreinoSessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TreinoSessaoRepository extends JpaRepository<TreinoSessao, UUID> {
    List<TreinoSessao> findByAlunoUuid(UUID alunoUuid);

    long countByAlunoUuidAndExercicio_Categoria_UuidAndData(UUID alunoUuid, UUID categoriaUuid, LocalDate data);

    List<TreinoSessao> findByAlunoUuidAndDataBeforeOrderByDataDesc(UUID alunoUuid, LocalDate data);
}
