package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Data
@Entity
@Audited
public class AlunoObservacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Aluno aluno;

    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private ObservacaoTipo tipo;

    @CreationTimestamp
    private LocalDateTime dataRegistro;
}
