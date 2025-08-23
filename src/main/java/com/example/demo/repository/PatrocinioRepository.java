package com.example.demo.repository;

import com.example.demo.entity.Patrocinio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatrocinioRepository extends JpaRepository<Patrocinio, UUID> {
    Page<Patrocinio> findByPatrocinadorUuid(UUID patrocinadorUuid, Pageable pageable);
}

