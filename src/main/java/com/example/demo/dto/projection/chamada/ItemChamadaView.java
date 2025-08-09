package com.example.demo.dto.projection.chamada;

import com.example.demo.dto.projection.aluno.AlunoSummary;

import java.util.UUID;

public interface ItemChamadaView {
    UUID getUuid();

    AlunoSummary getAluno();

    Boolean getPresente();
}
