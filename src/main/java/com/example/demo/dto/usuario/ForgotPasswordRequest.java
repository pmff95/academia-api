package com.example.demo.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Dados para recuperação de senha.
 */
public record ForgotPasswordRequest(
        @Schema(description = "Matrícula do usuário", example = "123456")
        String matricula,

        @Schema(description = "Email para envio da nova senha", example = "user@example.com")
        String email,

        @Schema(description = "Data de nascimento do usuário", example = "1990-01-01")
        LocalDate dataNascimento,

        @Schema(description = "Último nome da mãe")
        String ultimoNomeMae,

        @Schema(description = "Três primeiros dígitos do CPF")
        String cpfTresDigitos,

        @Schema(description = "Nova senha quando não houver envio por email", example = "senhaSegura123")
        String novaSenha
) {
}
