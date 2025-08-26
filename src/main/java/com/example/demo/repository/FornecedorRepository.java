package com.example.demo.repository;

import com.example.demo.entity.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {
    Page<Fornecedor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Optional<Fornecedor> findByUuid(UUID fornecedorUuid);
}

