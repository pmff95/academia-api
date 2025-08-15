package com.example.demo.repository;

import com.example.demo.entity.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Page<Aluno> findByAcademiaUuid(UUID uuid, Pageable pageable);

    Page<Aluno> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Aluno> findByAcademiaUuidAndNomeContainingIgnoreCase(UUID uuid, String nome, Pageable pageable);
    Page<Aluno> findByProfessorUuidAndAcademiaUuid(UUID professorUuid, String escolaUuid, Pageable pageable);
}
