package com.example.demo.dto.aluno;

import com.example.demo.domain.enums.Status;
import java.util.UUID;

public record AlunoUsuarioResponse(
        UUID uuid,
        String nome,
        String matricula,
        String email,
        String foto,
        Status status
) {
}
