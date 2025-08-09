package com.example.demo.dto.instituicao;

import io.swagger.v3.oas.annotations.media.Schema;

public record EscolaFinanceiroRequest(
        @Schema(description = "Dia do pagamento da mensalidade")
        Integer diaPagamento,

        @Schema(description = "Dia previsto para recebimento")
        Integer diaRecebimento
) {
}
