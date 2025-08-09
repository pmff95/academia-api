package com.example.demo.dto.projection;

import com.example.demo.dto.projection.aluno.AlunoSummary;

import java.util.UUID;

public interface AlunoGrupoItinerarioSummary {
    UUID getUuid();

    AlunoSummary getAluno();
}
