package com.example.demo.dto.api.cobpag.vencimento;

import com.example.demo.dto.api.cobpag.vencimento.CalendarioVencimento;
import com.example.demo.dto.api.cobpag.vencimento.DevedorCompleto;
import com.example.demo.dto.api.cobpag.vencimento.Valor;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CriarCobrancaVencimentoRequest(
    CalendarioVencimento calendario,
    DevedorCompleto devedor,
    Valor valor,
    String chave,
    String solicitacaoPagador
) {}