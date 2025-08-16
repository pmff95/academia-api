package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_exercicio_nome_musculo_descricao",
                columnNames = {"nome", "musculo", "descricao"}
        )
)
public class Exercicio {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Musculo musculo;

    @ManyToOne
    @JoinColumn(name = "academia_uuid", referencedColumnName = "uuid")
    private Academia academia;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
