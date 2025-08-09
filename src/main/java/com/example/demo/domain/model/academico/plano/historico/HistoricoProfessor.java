package com.example.demo.domain.model.academico.plano.historico;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.professor.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "historico_professor")
@Audited
@Getter
@Setter
public class HistoricoProfessor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Column(name = "disciplina")
    private String disciplina;

    @Column(name = "ano_letivo")
    private Integer anoLetivo;

    @Column(name = "total_horas")
    private Integer totalHoras;

    @Column(name = "turma_nome")
    private String turmaNome;
}
