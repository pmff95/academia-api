package com.example.demo.repository;

import com.example.demo.entity.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlimentoRepository extends JpaRepository<Alimento, UUID> {
}
