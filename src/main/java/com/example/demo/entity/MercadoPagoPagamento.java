package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pagamento registrado no Mercado Pago com associação ao usuário e endereço de entrega.
 */

@Data
@Entity
public class MercadoPagoPagamento {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    private String mercadoPagoId;

    private String status;

    private String tipo;

    private String detalhe;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Endereco endereco;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MercadoPagoPagamentoProduto> produtos = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
