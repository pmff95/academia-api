package com.example.demo.repository;

import com.example.demo.entity.FichaTreinoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FichaTreinoHistoricoRepository extends JpaRepository<FichaTreinoHistorico, UUID> {
    List<FichaTreinoHistorico> findByAluno_UuidOrderByDataCadastroDesc(UUID alunoUuid);
    Optional<FichaTreinoHistorico> findFirstByAluno_UuidOrderByDataCadastroDesc(UUID alunoUuid);
}
