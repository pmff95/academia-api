package com.example.demo.domain.model.academico.plano.atividade;

import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrega_atividade")
@Audited
@Getter
@Setter
public class EntregaAtividade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    @Column(name = "entregue_em")
    private LocalDateTime entregueEm;

    @Column(name = "arquivo_url")
    private String arquivoUrl;

    @Column(name = "nota")
    private Double nota;
}
