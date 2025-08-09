package com.example.demo.domain.model.academico.plano.historico;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "historico_escolar")
@Audited
@Getter
@Setter
public class HistoricoEscolar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "serie")
    private String serie;

    @Column(name = "turma")
    private String turma;

    @Column(name = "disciplina")
    private String disciplina;

    @Column(name = "nota_final")
    private Double notaFinal;

    @Column(name = "frequencia")
    private Double frequencia;

    @Column(name = "resultado_final")
    private String resultadoFinal; // Aprovado, Reprovado, etc.
}
