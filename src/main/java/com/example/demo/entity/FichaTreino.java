package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class FichaTreino {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private Professor professor;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private boolean preset;

    @ManyToMany
    @JoinTable(name = "ficha_exercicio",
            joinColumns = @JoinColumn(name = "ficha_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "exercicio_uuid", referencedColumnName = "uuid"))
    private List<Exercicio> exercicios = new ArrayList<>();

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
