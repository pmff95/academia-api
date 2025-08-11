package com.example.demo.repository;

import com.example.demo.entity.AlunoObservacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlunoObservacaoRepository extends JpaRepository<AlunoObservacao, UUID> {
    List<AlunoObservacao> findByAlunoUuid(UUID alunoUuid);
}
