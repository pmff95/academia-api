package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@DiscriminatorValue("ALUNO")
public class Aluno extends Usuario {
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    private LocalDate dataMatricula;

    @Enumerated(EnumType.STRING)
    private StatusAluno status = StatusAluno.ATIVO;
}
