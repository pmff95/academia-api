package com.example.demo.repository;

import com.example.demo.entity.MercadoPagoPagamentoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MercadoPagoPagamentoProdutoRepository extends JpaRepository<MercadoPagoPagamentoProduto, UUID> {
}
