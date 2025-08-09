package com.example.demo.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Requisição para definição de senha por um administrador.
 */
public record DefinirSenhaRequest(
        @Schema(description = "Nova senha do usuário (apenas números)", example = "123456")
        @NotBlank(message = "Senha deve ser informada")
        @Pattern(regexp = "\\d+", message = "Senha deve conter apenas números")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 dígitos")
        String senha
) {
}
