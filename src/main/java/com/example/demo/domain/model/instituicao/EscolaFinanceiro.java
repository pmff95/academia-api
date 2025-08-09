package com.example.demo.domain.model.instituicao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "escola_financeiro")
@Audited
@Getter
@Setter
public class EscolaFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false, unique = true)
    private Escola escola;

    @Column(name = "dia_pagamento", nullable = false)
    private Integer diaPagamento;

    @Column(name = "dia_recebimento", nullable = false)
    private Integer diaRecebimento;

    @Version
    private int version = 0;
}
