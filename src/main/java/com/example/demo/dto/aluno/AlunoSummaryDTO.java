package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record AlunoSummaryDTO(
        UUID uuid,
        String nome,
        String foto,
        String matricula,
        String cpf,
        LocalDate dataNascimento,
        Serie serie,
        Status status
) {}
