package com.example.demo.repository;

import com.example.demo.entity.AlunoObservacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoObservacaoRepository extends JpaRepository<AlunoObservacao, Long> {
    List<AlunoObservacao> findByAlunoId(Long alunoId);
}
