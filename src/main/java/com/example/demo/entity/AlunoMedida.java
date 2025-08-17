package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class AlunoMedida {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    private Aluno aluno;

    private BigDecimal peso;
    private BigDecimal altura;
    private BigDecimal bracoEsquerdo;
    private BigDecimal bracoDireito;
    private BigDecimal peito;
    private BigDecimal abdomen;
    private BigDecimal cintura;
    private BigDecimal quadril;
    private BigDecimal coxaEsquerda;
    private BigDecimal coxaDireita;
    private BigDecimal panturrilhaEsquerda;
    private BigDecimal panturrilhaDireita;

    @CreationTimestamp
    private LocalDateTime dataRegistro;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
