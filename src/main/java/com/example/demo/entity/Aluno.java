package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@DiscriminatorValue("ALUNO")
public class Aluno extends Usuario {
    @ManyToOne
    @JoinColumn(name = "professor_uuid")
    private Professor professor;

    private LocalDate dataMatricula;
}
