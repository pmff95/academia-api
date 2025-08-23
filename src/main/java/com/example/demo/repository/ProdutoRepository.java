package com.example.demo.repository;

import com.example.demo.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    Page<Produto> findByFornecedorUuid(UUID fornecedorUuid, Pageable pageable);
}

