package com.example.demo.dto;

import java.util.UUID;

public class FichaTreinoExercicioDTO {
    private UUID exercicioUuid;
    private Integer repeticoes;
    private Double carga;
    private Integer series;
    private Integer tempoDescanso;

    public UUID getExercicioUuid() {
        return exercicioUuid;
    }

    public void setExercicioUuid(UUID exercicioUuid) {
        this.exercicioUuid = exercicioUuid;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public Double getCarga() {
        return carga;
    }

    public void setCarga(Double carga) {
        this.carga = carga;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getTempoDescanso() {
        return tempoDescanso;
    }

    public void setTempoDescanso(Integer tempoDescanso) {
        this.tempoDescanso = tempoDescanso;
    }
}
