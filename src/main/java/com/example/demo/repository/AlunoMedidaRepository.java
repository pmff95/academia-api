package com.example.demo.repository;

import com.example.demo.entity.AlunoMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoMedidaRepository extends JpaRepository<AlunoMedida, Long> {
    List<AlunoMedida> findByAlunoId(Long alunoId);
}
