package com.example.demo.domain.model.aluno;

import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.domain.model.academico.Turma;
import com.example.demo.domain.enums.Serie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno")
@Audited
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class Aluno extends Usuario {

    @OneToMany(mappedBy = "aluno", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResponsavelAluno> responsaveis = new ArrayList<>();

    @ManyToMany(mappedBy = "alunos", fetch = FetchType.LAZY)
    private List<Turma> turmas = new ArrayList<>();

    public Aluno() {
    }
}
