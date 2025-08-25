package com.example.demo.repository;

import com.example.demo.entity.AlunoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlunoPagamentoRepository extends JpaRepository<AlunoPagamento, UUID> {
    Optional<AlunoPagamento> findTopByAlunoUuidOrderByDataPagamentoDesc(UUID alunoUuid);
}
