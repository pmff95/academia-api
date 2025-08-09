package com.example.demo.dto.api.cobpag.normal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EditarCobrancaRequest(
        Calendario calendario,
        Devedor devedor,
        Valor valor,
        String chave,
        String solicitacaoPagador,
        List<InfoAdicional> infoAdicionais,
        String status
) {
}
