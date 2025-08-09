package com.example.demo.dto.academico.plano;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class BoletimPdfDTO {
    public String nome;
    public Map<String, NotaPeriodoPdfDTO> notasPorPeriodo = new LinkedHashMap<>();
    public String mediaFinal = "-";
    public String freqFinal = "-";
    public String situacaoFinal = "-";
    public String observacao = "";

    public BoletimPdfDTO(String nome) {
        this.nome = nome;
    }

    public void addNota(String periodo, NotaPeriodoPdfDTO np) {
        notasPorPeriodo.put(periodo, np);
    }
}

