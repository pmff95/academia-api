package com.example.demo.repository;

import com.example.demo.entity.Exercicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExercicioRepository extends JpaRepository<Exercicio, UUID> {
    Page<Exercicio> findByAcademiaIsNull(Pageable pageable);

    Page<Exercicio> findByAcademiaUuid(UUID academiaUuid, Pageable pageable);
}
