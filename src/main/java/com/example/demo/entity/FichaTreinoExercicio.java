package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "ficha_exercicio")
public class FichaTreinoExercicio {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_uuid", referencedColumnName = "uuid")
    private FichaTreinoCategoria categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercicio_uuid", referencedColumnName = "uuid")
    private Exercicio exercicio;

    @Column
    private String tipo;

    @ElementCollection
    @CollectionTable(name = "ficha_exercicio_repeticoes", joinColumns = @JoinColumn(name = "ficha_exercicio_uuid"))
    @Column(name = "repeticao", nullable = false)
    private List<Integer> repeticoes;

    @ElementCollection
    @CollectionTable(name = "ficha_exercicio_cargas", joinColumns = @JoinColumn(name = "ficha_exercicio_uuid"))
    @Column(name = "carga", nullable = false)
    private List<Double> carga;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false)
    private Integer tempoDescanso;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
