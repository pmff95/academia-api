package com.example.demo.dto.aviso;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AvisoRequest(
        @Schema(description = "Título do aviso", example = "Recesso escolar")
        @NotBlank(message = "O título é obrigatório")
        @Size(min = 2, max = 255, message = "O título deve ter entre 2 e 255 caracteres")
        String titulo,

        @Schema(description = "Mensagem do aviso", example = "As aulas retornarão na próxima semana")
        String mensagem
) {
}
