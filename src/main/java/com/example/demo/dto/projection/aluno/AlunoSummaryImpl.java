package com.example.demo.dto.projection.aluno;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record AlunoSummaryImpl(
        UUID uuid,
        String nome,
        String foto,
        String matricula,
        LocalDate dataNascimento,
        Serie serie,
        String cpf,
        Status status
) implements AlunoSummary {

    public AlunoSummaryImpl(AlunoSummary summary) {
        this(
                summary != null ? summary.getUuid() : null,
                summary != null ? summary.getNome() : null,
                summary != null ? summary.getFoto() : null,
                summary != null ? summary.getMatricula() : null,
                summary != null ? summary.getDataNascimento() : null,
                summary != null ? summary.getSerie() : null,
                summary != null ? summary.getCpf() : null,
                summary != null ? summary.getStatus() : null
        );
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getFoto() {
        return foto;
    }

    @Override
    public String getMatricula() {
        return matricula;
    }

    @Override
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    @Override
    public Serie getSerie() {
        return serie;
    }

    @Override
    public String getCpf() {
        return cpf;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
