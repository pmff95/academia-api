package com.example.demo.dto;

import java.time.LocalDate;
import java.util.UUID;

public class TreinoSessaoDTO {
    private UUID exercicioUuid;
    private LocalDate data;
    private Integer repeticoesRealizadas;
    private Double cargaRealizada;

    public UUID getExercicioUuid() {
        return exercicioUuid;
    }

    public void setExercicioUuid(UUID exercicioUuid) {
        this.exercicioUuid = exercicioUuid;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getRepeticoesRealizadas() {
        return repeticoesRealizadas;
    }

    public void setRepeticoesRealizadas(Integer repeticoesRealizadas) {
        this.repeticoesRealizadas = repeticoesRealizadas;
    }

    public Double getCargaRealizada() {
        return cargaRealizada;
    }

    public void setCargaRealizada(Double cargaRealizada) {
        this.cargaRealizada = cargaRealizada;
    }
}
