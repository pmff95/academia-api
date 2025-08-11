package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class AlunoObservacao {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    private Aluno aluno;

    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private ObservacaoTipo tipo;

    @CreationTimestamp
    private LocalDateTime dataRegistro;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
