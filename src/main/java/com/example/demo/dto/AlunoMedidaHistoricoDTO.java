package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AlunoMedidaHistoricoDTO {
    private List<AlunoMedidaDTO> medidas;
    private Map<String, BigDecimal> mediaVariacoes;

    public List<AlunoMedidaDTO> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<AlunoMedidaDTO> medidas) {
        this.medidas = medidas;
    }

    public Map<String, BigDecimal> getMediaVariacoes() {
        return mediaVariacoes;
    }

    public void setMediaVariacoes(Map<String, BigDecimal> mediaVariacoes) {
        this.mediaVariacoes = mediaVariacoes;
    }
}
