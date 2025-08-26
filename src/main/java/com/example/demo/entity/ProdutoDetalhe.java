package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class ProdutoDetalhe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private String tamanho;
    private String cor;
    private String sabor;
    private String volume;
    private String referencia;
    private String imagemUrl;
    private boolean ativo = true;
    private boolean promocao = false;
    private Integer estoque;
    private BigDecimal preco;
    private BigDecimal precoDesconto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_uuid", nullable = false)
    private Produto produto;
}
