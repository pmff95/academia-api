package com.example.demo.domain.model.academico;

import com.example.demo.domain.enums.Turno;
import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turma")
@Audited
@Getter
@Setter
public class Turma extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "serie", nullable = false)
    private Serie serie;

    @ManyToMany
    @JoinTable(
            name = "aluno_turma",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Turno turno;

    @Column(name = "ano_letivo")
    private Integer anoLetivo;

    @Column(name = "limite_vagas")
    private Integer limiteVagas;

    @Version
    private int version;
}
