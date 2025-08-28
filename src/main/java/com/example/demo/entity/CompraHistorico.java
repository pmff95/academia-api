package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class CompraHistorico {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pagamento_uuid", nullable = false)
    private MercadoPagoPagamento pagamento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataCompra;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (dataCompra == null) {
            dataCompra = LocalDateTime.now();
        }
    }
}

