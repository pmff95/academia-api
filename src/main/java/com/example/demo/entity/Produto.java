package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
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
    @ElementCollection
    private List<String> tamanhos;
    @ElementCollection
    private List<String> cores;
    @ElementCollection
    private List<String> sabores;
    private String marca;
    private Integer quantidade;
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

