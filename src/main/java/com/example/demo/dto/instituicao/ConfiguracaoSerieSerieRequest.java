package com.example.demo.dto.instituicao;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ConfiguracaoSerieSerieRequest(
        @Schema(description = "Série")
        Serie serie,
        @Schema(description = "Quantidade de avaliações para a série")
        Integer quantidadeAvaliacoes,
        @Schema(description = "Média necessária para aprovação")
        BigDecimal media,
        @Schema(description = "Status da série")
        Status status
) {
}
