package com.example.demo.repository;

import com.example.demo.entity.ConsumoAlimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumoAlimentoRepository extends JpaRepository<ConsumoAlimento, UUID> {
}
