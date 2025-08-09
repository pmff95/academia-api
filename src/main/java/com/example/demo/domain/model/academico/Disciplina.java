package com.example.demo.domain.model.academico;

import com.example.demo.domain.enums.NivelEnsino;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplina")
@Audited
@Getter
@Setter
public class Disciplina extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_ensino", nullable = false)
    private NivelEnsino nivelEnsino;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria = 0;

    @ManyToMany(mappedBy = "disciplinas")
    private List<Professor> professores = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "itinerario", nullable = false)
    private boolean itinerario = false;

    
}
