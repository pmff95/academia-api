package com.example.demo.dto;

import com.example.demo.entity.Musculo;
import com.example.demo.domain.enums.StatusTreino;
import java.util.UUID;

public class FichaTreinoExercicioDTO {
    private UUID uuid;
    private UUID exercicioUuid;
    private String exercicioNome;
    private Musculo musculo;
    private Integer repeticoes;
    private Double carga;
    private Integer series;
    private Integer tempoDescanso;
    private StatusTreino status;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getExercicioUuid() {
        return exercicioUuid;
    }

    public void setExercicioUuid(UUID exercicioUuid) {
        this.exercicioUuid = exercicioUuid;
    }

    public String getExercicioNome() {
        return exercicioNome;
    }

    public void setExercicioNome(String exercicioNome) {
        this.exercicioNome = exercicioNome;
    }

    public Musculo getMusculo() {
        return musculo;
    }

    public void setMusculo(Musculo musculo) {
        this.musculo = musculo;
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

    public StatusTreino getStatus() {
        return status;
    }

    public void setStatus(StatusTreino status) {
        this.status = status;
    }
}
