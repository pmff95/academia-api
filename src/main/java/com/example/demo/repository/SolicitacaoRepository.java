package com.example.demo.repository;

import com.example.demo.entity.Solicitacao;
import com.example.demo.domain.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {
    List<Solicitacao> findByAlunoUuidAndStatus(UUID alunoUuid, StatusSolicitacao status);
}
