package com.example.demo.dto.instituicao;

import com.example.demo.domain.enums.TipoPeriodo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ConfiguracaoSerieItemRequest(
        @Schema(description = "Tipo de período avaliativo")
        TipoPeriodo tipoPeriodo,
        @Schema(description = "Séries associadas ao tipo de período")
        List<ConfiguracaoSerieSerieRequest> series
) {
}
