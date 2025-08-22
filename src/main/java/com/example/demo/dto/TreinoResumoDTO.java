package com.example.demo.dto;

import java.util.List;

public class TreinoResumoDTO {
    private int diasConsecutivos;
    private List<TreinoDesempenhoDTO> diasTreinados;

    public int getDiasConsecutivos() {
        return diasConsecutivos;
    }

    public void setDiasConsecutivos(int diasConsecutivos) {
        this.diasConsecutivos = diasConsecutivos;
    }

    public List<TreinoDesempenhoDTO> getDiasTreinados() {
        return diasTreinados;
    }

    public void setDiasTreinados(List<TreinoDesempenhoDTO> diasTreinados) {
        this.diasTreinados = diasTreinados;
    }
}
