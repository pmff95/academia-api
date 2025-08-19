package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class ConsumoAlimento {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Refeicao refeicao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "alimento_uuid", referencedColumnName = "uuid")
    private Alimento alimento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
