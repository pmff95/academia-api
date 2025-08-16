package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "ficha_categoria")
public class FichaTreinoCategoria {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ficha_uuid", referencedColumnName = "uuid")
    private FichaTreino ficha;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichaTreinoExercicio> exercicios = new ArrayList<>();

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
