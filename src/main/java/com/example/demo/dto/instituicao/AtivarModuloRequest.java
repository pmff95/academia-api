package com.example.demo.dto.instituicao;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record AtivarModuloRequest(
        @Schema(description = "Identificador da escola")
        UUID escolaUuid,

        @Schema(description = "Módulos e suas datas de expiração")
        List<ModuloExpiracaoRequest> modulos
) {
}
