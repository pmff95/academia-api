package com.example.demo.dto.academico.plano;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AtividadeRequest(

        @Schema(description = "Título da atividade")
        @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres")
        String titulo,

        @Schema(description = "Descrição da atividade")
        String descricao,

        @Schema(description = "Prazo para entrega da atividade")
        LocalDate prazo,

        @Schema(description = "Indica se a atividade deve ser atribuída a todos os alunos da turma")
        boolean todosAlunos,

        @Schema(description = "Lista de UUIDs dos alunos selecionados (se todosAlunos for false)")
        List<UUID> alunosSelecionados,

        @Schema(description = "Arquivo anexo da atividade (.doc, .docx, .pdf)")
        MultipartFile arquivo,

        @Schema(description = "UUID da do plano disciplina associado à atividade")
        @NotNull(message = "UUID Plano Disciplina é obrigatório")
        UUID planoDisciplinaUuid

) {
}
