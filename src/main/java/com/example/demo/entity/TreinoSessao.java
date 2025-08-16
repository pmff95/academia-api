package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
public class TreinoSessao {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    private Aluno aluno;

    @ManyToOne(optional = false)
    private FichaTreinoExercicio exercicio;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Integer repeticoesRealizadas;

    @Column(nullable = false)
    private Double cargaRealizada;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
