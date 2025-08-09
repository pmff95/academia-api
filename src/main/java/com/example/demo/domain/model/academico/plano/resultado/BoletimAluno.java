package com.example.demo.domain.model.academico.plano.resultado;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.instituicao.Escola;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Table(name = "boletim_aluno")
@Audited
@Getter
@Setter
public class BoletimAluno extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "observacao_geral", columnDefinition = "TEXT")
    private String observacaoGeral;

    @OneToMany(mappedBy = "boletim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanoDisciplinaPeriodo> notas;
}