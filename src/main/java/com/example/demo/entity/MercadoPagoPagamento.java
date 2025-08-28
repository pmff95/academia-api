package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pagamento registrado no Mercado Pago com associação ao usuário e dados de entrega/contato.
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

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MercadoPagoPagamentoProduto> produtos = new ArrayList<>();

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String nomeContato;
    private String telefone;
    private String telefoneSecundario;

    @PrePersist
    private void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
