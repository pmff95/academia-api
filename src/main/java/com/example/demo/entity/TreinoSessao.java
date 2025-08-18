package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

import com.example.demo.domain.enums.StatusTreino;

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

    @Column
    private Integer repeticoesRealizadas;

    @Column
    private Double cargaRealizada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTreino status;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (status == null) {
            status = StatusTreino.PENDENTE;
        }
    }
}
