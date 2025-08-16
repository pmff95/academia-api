package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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
    private String nome;

    @Column(nullable = false)
    private boolean preset;

    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichaTreinoCategoria> categorias = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        if (dataAtualizacao == null) {
            dataAtualizacao = dataCadastro;
        }
    }

    @PreUpdate
    private void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
