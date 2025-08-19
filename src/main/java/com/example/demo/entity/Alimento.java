package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Alimento {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal calorias;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal gordura;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal proteinas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal carboidratos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal acucares;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal idr;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
