package com.example.demo.dto.api.cobpag.normal;

import java.util.List;

public record BuscarCobrancaResponse(
        String chave,
        String solicitacaoPagador,
        List<InfoAdicional> infoAdicionais,
        String pixCopiaECola,
        Calendario calendario,
        String txid,
        Integer revisao,
        Devedor devedor,
        String location,
        String status,
        Valor valor
) {
}
