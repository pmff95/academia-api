package com.example.demo.repository;

import com.example.demo.entity.Academia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AcademiaRepository extends JpaRepository<Academia, UUID> {
    Optional<Academia> findByAdminUuid(UUID uuid);

    Academia findByUuid(UUID uuid);

    Page<Academia> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Optional<Academia> findByCodigo(String codigo);
}
