package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BuscarCobrancaVencimentoResponse(
    CalendarioVencimento calendario,
    String txid,
    Integer revisao,
    LocCompleto loc,
    String status,
    DevedorCompleto devedor,
    Recebedor recebedor,
    Valor valor,
    String chave,
    String solicitacaoPagador
) {}