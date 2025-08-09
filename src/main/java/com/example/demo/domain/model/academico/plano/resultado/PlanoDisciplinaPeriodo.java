package com.example.demo.domain.model.academico.plano.resultado;

import com.example.demo.domain.model.academico.plano.PlanoDisciplina;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.PeriodoAvaliativo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
@Entity
@Table(name = "plano_disciplina_periodo")
@Audited
@Getter
@Setter
public class PlanoDisciplinaPeriodo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boletim_id", nullable = false)
    private BoletimAluno boletim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_disciplina_id", nullable = false)
    private PlanoDisciplina planoDisciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoAvaliativo periodo;

    @Column(name = "nota")
    private Double nota;

    @Column(name = "faltas")
    private Integer faltas;

    @Column(name = "observacao")
    private String observacao;
}