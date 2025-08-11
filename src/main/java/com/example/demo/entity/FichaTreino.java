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

    @ManyToOne(optional = false)
    private Aluno aluno;

    @ManyToOne
    private Professor professor;

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
