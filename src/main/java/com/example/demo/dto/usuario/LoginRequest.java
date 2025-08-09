package com.example.demo.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "Login", example = "user")
        @NotBlank(message = "Login deve ser preenchido")
        String login,
        @Schema(description = "Senha", example = "abc123")
        @NotBlank(message = "Senha deve ser preenchida")
        String password
) {

}
