package com.example.demo.domain.model.instituicao;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.TipoPeriodo;
import com.example.demo.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Entity
@Table(name = "configuracao_serie")
@Audited
@Getter
@Setter
public class ConfiguracaoSerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Enumerated(EnumType.STRING)
    @Column(name = "serie", nullable = false)
    private Serie serie;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_periodo", nullable = false)
    private TipoPeriodo tipoPeriodo;

    @Column(name = "quantidade_avaliacoes", nullable = false)
    private Integer quantidadeAvaliacoes = 0;

    @Column(name = "media", nullable = false)
    private BigDecimal media = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ATIVO;

    @Version
    private int version = 0;
}