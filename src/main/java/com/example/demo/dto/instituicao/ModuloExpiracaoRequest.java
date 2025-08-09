package com.example.demo.dto.instituicao;

import com.example.demo.domain.enums.NomeModulo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ModuloExpiracaoRequest(
        @Schema(description = "Nome do módulo")
        NomeModulo nomeModulo,

        @Schema(description = "Data de expiração do módulo", example = "2024-12-31")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataExpiracao
) {
}
