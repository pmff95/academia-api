package com.example.demo.repository;

import com.example.demo.entity.Aula;
import com.example.demo.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AulaRepository extends JpaRepository<Aula, UUID> {
    boolean existsByProfessorAndInicioLessThanAndFimGreaterThan(Professor professor, LocalDateTime fim, LocalDateTime inicio);
    boolean existsByProfessorAndInicioLessThanAndFimGreaterThanAndUuidNot(Professor professor, LocalDateTime fim, LocalDateTime inicio, UUID uuid);
}
