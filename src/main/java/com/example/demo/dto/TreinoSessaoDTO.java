package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.demo.domain.enums.StatusTreino;

public class TreinoSessaoDTO {
    private UUID exercicioUuid;
    private LocalDate data;
    private List<Integer> repeticoesRealizadas;
    private List<Double> cargaRealizada;
    private StatusTreino status;

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

    public List<Integer> getRepeticoesRealizadas() {
        return repeticoesRealizadas;
    }

    public void setRepeticoesRealizadas(List<Integer> repeticoesRealizadas) {
        this.repeticoesRealizadas = repeticoesRealizadas;
    }

    public List<Double> getCargaRealizada() {
        return cargaRealizada;
    }

    public void setCargaRealizada(List<Double> cargaRealizada) {
        this.cargaRealizada = cargaRealizada;
    }

    public StatusTreino getStatus() {
        return status;
    }

    public void setStatus(StatusTreino status) {
        this.status = status;
    }
}
