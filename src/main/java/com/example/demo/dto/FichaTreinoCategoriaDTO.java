package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public class FichaTreinoCategoriaDTO {
    private UUID uuid;
    private String nome;
    private List<FichaTreinoExercicioDTO> exercicios;
    private String observacao;
    private double percentualConcluido;
    private boolean ativo;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<FichaTreinoExercicioDTO> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<FichaTreinoExercicioDTO> exercicios) {
        this.exercicios = exercicios;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public double getPercentualConcluido() {
        return percentualConcluido;
    }

    public void setPercentualConcluido(double percentualConcluido) {
        this.percentualConcluido = percentualConcluido;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
