package com.example.demo.dto.instituicao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EscolaVisualRequest(
        @Schema(description = "Cor do corpo do menu", example = "#FFFFFF")
        @NotBlank(message = "Cor do corpo deve ser informada")
        @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Cor do corpo deve estar em hexadecimal")
        String corCorpo,

        @Schema(description = "Cor do texto do menu", example = "#000000")
        @NotBlank(message = "Cor do texto deve ser informada")
        @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Cor do texto deve estar em hexadecimal")
        String corTexto,

        @Schema(description = "Cor do texto com mouse em cima", example = "#FF0000")
        @NotBlank(message = "Cor do texto hover deve ser informada")
        @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Cor do texto hover deve estar em hexadecimal")
        String corTextoHover,

        @Schema(description = "Cor do fundo com mouse em cima", example = "#EEEEEE")
        @NotBlank(message = "Cor do fundo hover deve ser informada")
        @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Cor do fundo hover deve estar em hexadecimal")
        String corFundoHover,

        @Schema(description = "URL da logo da escola")
        @NotBlank(message = "URL da logo deve ser informada")
        String logoUrl
) {
}
