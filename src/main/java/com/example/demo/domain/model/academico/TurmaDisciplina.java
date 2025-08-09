package com.example.demo.domain.model.academico;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Table(name = "turma_disciplina")
@Audited
@Getter
@Setter
public class TurmaDisciplina extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria = 0;

    @Column(name = "aulas_semana", nullable = false)
    private Integer aulasSemana = 0;


    @OneToMany(mappedBy = "turmaDisciplina", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AulaHorario> aulas;

}
