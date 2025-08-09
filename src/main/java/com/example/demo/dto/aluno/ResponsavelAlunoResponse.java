package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.GrauParentesco;

import java.util.UUID;

public record ResponsavelAlunoResponse(
        UUID uuid,
        String nome,
        String email,
        String cpf,
        String telefone,
        GrauParentesco grauParentesco
) {
}
