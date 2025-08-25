package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Maquina {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}

