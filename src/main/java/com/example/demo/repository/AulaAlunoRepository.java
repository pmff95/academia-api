package com.example.demo.repository;

import com.example.demo.domain.enums.StatusInscricao;
import com.example.demo.entity.Aula;
import com.example.demo.entity.AulaAluno;
import com.example.demo.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AulaAlunoRepository extends JpaRepository<AulaAluno, UUID> {
    Optional<AulaAluno> findByAulaAndAluno(Aula aula, Aluno aluno);
    long countByAulaAndStatus(Aula aula, StatusInscricao status);
}
