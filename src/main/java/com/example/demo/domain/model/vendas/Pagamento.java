package com.example.demo.domain.model.vendas;

import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
@Audited
@Getter
@Setter
public class Pagamento extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioPagante;

    @Column(name = "mp_id", nullable = false)
    private String mpId;

    @Column(name = "status_pagamento" , nullable = false)
    private String statusPagamento;

    @Column(name = "data")
    private LocalDateTime data;
    @Transient
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "pagamento", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PagamentoItem> itens;

    public Pagamento() {
    }
}
