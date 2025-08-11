package com.example.demo.repository;

import com.example.demo.entity.Academia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcademiaRepository extends JpaRepository<Academia, UUID> {
    Optional<Academia> findByAdminUuid(UUID uuid);

    Academia findByUuid(UUID uuid);
}
