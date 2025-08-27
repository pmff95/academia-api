package com.example.demo.repository;

import com.example.demo.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Page<Professor> findByAcademiaUuid(UUID uuid, Pageable pageable);

    Page<Professor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Professor> findByAcademiaUuidAndNomeContainingIgnoreCase(UUID uuid, String nome, Pageable pageable);

    Page<Professor> findByCrefIsNotNull(Pageable pageable);

    Page<Professor> findByCrefIsNull(Pageable pageable);

    Page<Professor> findByNomeContainingIgnoreCaseAndCrefIsNotNull(String nome, Pageable pageable);

    Page<Professor> findByNomeContainingIgnoreCaseAndCrefIsNull(String nome, Pageable pageable);

    Page<Professor> findByAcademiaUuidAndCrefIsNotNull(UUID uuid, Pageable pageable);

    Page<Professor> findByAcademiaUuidAndCrefIsNull(UUID uuid, Pageable pageable);

    Page<Professor> findByAcademiaUuidAndNomeContainingIgnoreCaseAndCrefIsNotNull(UUID uuid, String nome, Pageable pageable);

    Page<Professor> findByAcademiaUuidAndNomeContainingIgnoreCaseAndCrefIsNull(UUID uuid, String nome, Pageable pageable);
}
