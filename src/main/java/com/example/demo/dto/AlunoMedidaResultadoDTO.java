package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Map;

public class AlunoMedidaResultadoDTO {
    private AlunoMedidaDTO atual;
    private Map<String, BigDecimal> variacoes;

    public AlunoMedidaDTO getAtual() {
        return atual;
    }

    public void setAtual(AlunoMedidaDTO atual) {
        this.atual = atual;
    }

    public Map<String, BigDecimal> getVariacoes() {
        return variacoes;
    }

    public void setVariacoes(Map<String, BigDecimal> variacoes) {
        this.variacoes = variacoes;
    }
}
