package com.example.demo.service.api.cobpag;

import com.example.demo.dto.api.cobpag.normal.Calendario;
import com.example.demo.dto.api.cobpag.normal.CriarCobrancaRequest;
import com.example.demo.dto.api.cobpag.normal.Devedor;
import com.example.demo.dto.api.cobpag.normal.Valor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TesteService {
    private final CobrancaPagamentoService cobrancaPagamentoService;
//    @Scheduled(fixedDelay = 10000)
    public void testarBuscar() {
        ZoneOffset utc = ZoneOffset.UTC;

        OffsetDateTime inicio = OffsetDateTime.of(
                2025, 7, 1,          // 1º de julho
                0, 0, 0, 0,          // 00:00:00
                utc);                // UTC → sufixo Z

        OffsetDateTime fim = OffsetDateTime.now(utc);

        var retornoCreate = cobrancaPagamentoService.criarCobranca(
                "Abcdef123456Ghijk7890LMNOPQR",
                new CriarCobrancaRequest(
                        new Calendario(3600, null),
                        new Devedor("Clebinho da ZL", "59115994066", null),
                        new Valor("54.00", 1),
                        null,
                        "Teste Javinha dos Cria",
                        null
                )
        );

        var retorno = cobrancaPagamentoService.listarCobrancas(inicio, fim);

        var retorno2 = cobrancaPagamentoService.buscarCobranca("Abcdef123456Ghijk7890LMNOPQR");

        System.out.println("ok");
    }


}
