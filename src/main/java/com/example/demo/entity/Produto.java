package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Produto {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "fornecedor_uuid", nullable = false)
    private Fornecedor fornecedor;

    private String nome;
    private String descricao;
    private Integer estoque;
    private BigDecimal preco;
    private BigDecimal precoDesconto;
    private String imagemUrl;
    private boolean ativo = true;

    @PrePersist
    private void gerarUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}

