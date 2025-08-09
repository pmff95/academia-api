package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class FichaTreino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Aluno aluno;

    @ManyToOne
    private Professor professor;

    @ManyToMany
    @JoinTable(name = "ficha_exercicio",
            joinColumns = @JoinColumn(name = "ficha_id"),
            inverseJoinColumns = @JoinColumn(name = "exercicio_id"))
    private List<Exercicio> exercicios = new ArrayList<>();
}
