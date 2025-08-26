package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class ProdutoDetalhe {
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
}
