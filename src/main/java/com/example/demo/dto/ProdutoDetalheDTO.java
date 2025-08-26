package com.example.demo.dto;

import java.math.BigDecimal;

public class ProdutoDetalheDTO {
    private String tamanho;
    private String cor;
    private String sabor;
    private String volume;
    private String referencia;
    private String imagemUrl;
    private Boolean ativo;
    private Boolean promocao;
    private Integer estoque;
    private BigDecimal preco;
    private BigDecimal precoDesconto;

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getSabor() { return sabor; }
    public void setSabor(String sabor) { this.sabor = sabor; }
    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public Boolean getPromocao() { return promocao; }
    public void setPromocao(Boolean promocao) { this.promocao = promocao; }
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public BigDecimal getPrecoDesconto() { return precoDesconto; }
    public void setPrecoDesconto(BigDecimal precoDesconto) { this.precoDesconto = precoDesconto; }
}
