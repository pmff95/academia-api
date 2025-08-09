package com.example.demo.domain.model.carteira;

import com.example.demo.domain.enums.TipoTransacao;
import com.example.demo.domain.model.base.BaseEntity;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.domain.model.vendas.Pedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacao")
@Audited
@Getter
@Setter
public class Transacao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carteira_id", nullable = false)
    private Carteira carteira;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(name = "tipo_transacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Version
    private int version;

    public Transacao() {

    }

}
