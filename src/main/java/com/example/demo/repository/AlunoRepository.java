package com.example.demo.repository;

import com.example.demo.entity.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Page<Aluno> findByAcademiaUuid(UUID uuid, Pageable pageable);

    Page<Aluno> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Aluno> findByAcademiaUuidAndNomeContainingIgnoreCase(UUID uuid, String nome, Pageable pageable);

    Page<Aluno> findByAcademiaUuidAndProfessorUuid(UUID academiaUuid, UUID professorUuid, Pageable pageable);

    Page<Aluno> findByAcademiaUuidAndProfessorUuidAndNomeContainingIgnoreCase(UUID academiaUuid, UUID professorUuid, String nome, Pageable pageable);

    @Query("SELECT a FROM Aluno a WHERE a.academia.uuid = :academiaUuid AND (a.professor IS NULL OR a.professor.uuid <> :professorUuid)")
    Page<Aluno> findByAcademiaUuidAndProfessorUuidNotOrProfessorIsNull(UUID academiaUuid, UUID professorUuid, Pageable pageable);

    @Query("SELECT a FROM Aluno a WHERE a.academia.uuid = :academiaUuid AND (a.professor IS NULL OR a.professor.uuid <> :professorUuid) AND LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Aluno> findByAcademiaUuidAndProfessorUuidNotOrProfessorIsNullAndNomeContainingIgnoreCase(UUID academiaUuid, UUID professorUuid, String nome, Pageable pageable);
}
