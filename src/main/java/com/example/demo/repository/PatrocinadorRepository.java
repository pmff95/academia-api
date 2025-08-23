package com.example.demo.repository;

import com.example.demo.entity.Patrocinador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatrocinadorRepository extends JpaRepository<Patrocinador, UUID> {
    Page<Patrocinador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}

