package com.example.demo.repository;

import com.example.demo.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Page<Professor> findByAcademiaUuid(UUID uuid, Pageable pageable);
}
