package com.example.demo.repository;

import com.example.demo.entity.CompraHistorico;
import com.example.demo.entity.MercadoPagoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompraHistoricoRepository extends JpaRepository<CompraHistorico, UUID> {
    boolean existsByPagamento(MercadoPagoPagamento pagamento);
}

