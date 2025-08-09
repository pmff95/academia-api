package com.example.demo.dto.academico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItinerarioFormativoRequest(
        @Schema(description = "Nome do itinerário", example = "Itinerário 1")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Schema(description = "Descrição do itinerário", example = "Descrição")
        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 2, max = 255, message = "A descrição deve ter entre 2 e 255 caracteres")
        String descricao
) {
}
