package com.example.demo.entity;

import com.example.demo.domain.enums.StatusInscricao;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class AulaAluno {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aula_uuid", referencedColumnName = "uuid")
    private Aula aula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_uuid", referencedColumnName = "uuid")
    private Aluno aluno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInscricao status;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
