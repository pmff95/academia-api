package com.example.demo.dto.academico;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Turno;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record TurmaRequest(
        @Schema(description = "Nome da turma", example = "Turma A")
        @NotBlank(message = "O nome da turma é obrigatório")
        @Size(min = 1, max = 60, message = "O nome deve ter entre 1 e 60 caracteres")
        String nome,

        @Schema(description = "Série da turma", example = "PRIMEIRO_ANO")
        @NotNull(message = "A série é obrigatória")
        Serie serie,

        @Schema(description = "Turno da turma", example = "MANHA")
        @NotNull(message = "O turno é obrigatório")
        Turno turno,

        @Schema(description = "Ano letivo da turma", example = "2025")
        Integer anoLetivo,

        @Schema(description = "Limite de vagas da turma", example = "30")
        @Min(value = 1, message = "O limite deve ser maior que zero")
        Integer limiteVagas,

        @Schema(description = "Lista de UUID dos alunos")
        List<UUID> alunosUuids
) {
}
