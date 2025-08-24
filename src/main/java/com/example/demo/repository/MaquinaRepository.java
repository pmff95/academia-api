package com.example.demo.repository;

import com.example.demo.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaquinaRepository extends JpaRepository<Maquina, UUID> {
}

