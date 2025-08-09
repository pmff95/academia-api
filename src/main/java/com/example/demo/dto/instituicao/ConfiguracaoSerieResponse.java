package com.example.demo.dto.instituicao;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.enums.TipoPeriodo;
import com.example.demo.domain.enums.Status;
import java.math.BigDecimal;

public record ConfiguracaoSerieResponse(
        Serie serie,
        TipoPeriodo tipoPeriodo,
        Integer quantidadeAvaliacoes,
        BigDecimal media,
        Status status
) {
}