package com.example.demo.dto;

import com.example.demo.entity.Musculo;
import com.example.demo.domain.enums.StatusTreino;
import java.util.List;
import java.util.UUID;
import com.example.demo.dto.CargaDTO;

public class FichaTreinoExercicioDTO {
    private UUID uuid;
    private UUID exercicioUuid;
    private String exercicioNome;
    private Musculo musculo;
    private String tipo;
    private List<CargaDTO> cargas;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<CargaDTO> getCargas() {
        return cargas;
    }

    public void setCargas(List<CargaDTO> cargas) {
        this.cargas = cargas;
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
