package com.example.demo.domain.model.vendas;

import com.example.demo.domain.enums.TipoPagamento;
import com.example.demo.domain.model.aluno.Aluno;
import com.example.demo.domain.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamento_item")
@Audited
@Getter
@Setter
public class PagamentoItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoPagamento tipo;

    @Column(name = "titulo")
    private String titulo;

    @Transient
    private BigDecimal valorIndividual;

    public PagamentoItem() {
    }
}
