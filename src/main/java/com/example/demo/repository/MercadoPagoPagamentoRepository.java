package com.example.demo.repository;

import com.example.demo.entity.MercadoPagoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MercadoPagoPagamentoRepository extends JpaRepository<MercadoPagoPagamento, UUID> {
    Optional<MercadoPagoPagamento> findByMercadoPagoId(String mercadoPagoId);
}
