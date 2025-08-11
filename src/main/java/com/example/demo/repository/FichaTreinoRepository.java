package com.example.demo.repository;

import com.example.demo.entity.FichaTreino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FichaTreinoRepository extends JpaRepository<FichaTreino, UUID> {
    List<FichaTreino> findByAluno_Uuid(UUID alunoUuid);
}
