package com.example.demo.repository;

import com.example.demo.entity.AlunoMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlunoMedidaRepository extends JpaRepository<AlunoMedida, Long> {
    List<AlunoMedida> findByAlunoUuid(UUID alunoUuid);
}
