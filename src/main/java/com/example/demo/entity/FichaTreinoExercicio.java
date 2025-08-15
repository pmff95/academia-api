package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "ficha_exercicio")
public class FichaTreinoExercicio {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ficha_uuid", referencedColumnName = "uuid")
    private FichaTreino ficha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercicio_uuid", referencedColumnName = "uuid")
    private Exercicio exercicio;

    @Column(nullable = false)
    private Integer repeticoes;

    @Column(nullable = false)
    private Double carga;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
