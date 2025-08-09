package com.example.demo.dto.api.cobpag.vencimento;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RevisarCobrancaVencimentoRequest(
    CalendarioVencimento calendario,
    DevedorCompleto devedor,
    Valor valor,
    Abatimento abatimento,
    String chave,
    String solicitacaoPagador,
    List<InfoAdicional> infoAdicionais,
    String status
) {}