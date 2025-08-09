package com.example.demo.service.api.cobpag;

import com.example.demo.dto.api.cobpag.normal.*;
import com.example.demo.dto.api.cobpag.shared.AuthTokenResponse;
import com.example.demo.dto.api.cobpag.vencimento.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

import static com.example.demo.common.response.exception.EurekaException.mapToEureka;

@Service
public class CobrancaComVencimentoService {

    private final WebClient webClient;
    private final String credId;
    private final String credSecret;
    private final String pixKey;
    private volatile AuthTokenResponse currentToken;
    private static final DateTimeFormatter C6_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .withZone(ZoneOffset.UTC);

    public CobrancaComVencimentoService(
            @Qualifier("cobPagWebClientBuilder") WebClient.Builder builder,
            @Value("${escola.cobpag.api.url}") String baseUrl,
            @Value("${escola.cobpag.api.credentials.id}") String credId,
            @Value("${escola.cobpag.api.credentials.secret}") String credSecret,
            @Value("${escola.cobpag.api.credentials.pix}") String pixKey
    ) {
        this.credId = credId;
        this.credSecret = credSecret;
        this.pixKey = pixKey;
        this.webClient = builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(errorHandlingFilter())
                .build();
    }

    /**
     * Autentica e armazena o token em memória.
     */
    @PostConstruct
    public void init() {
//        this.currentToken = autenticar();
    }

    private String basicAuthHeader() {
        String raw = credId + ":" + credSecret;
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public AuthTokenResponse autenticar() {
        return webClient.post()
                .uri("/v1/auth/")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader())
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(AuthTokenResponse.class)
                .block();
    }

    private String bearer() {
        if (currentToken == null || currentToken.expiresIn() <= 0) {
            currentToken = autenticar();
        }
        return "Bearer " + currentToken.accessToken();
    }

    public BuscarCobrancaResponse criarCobrancaComVencimento(String txid, CriarCobrancaVencimentoRequest request) {
        return webClient.put()
                .uri("/v2/pix/cobv/{txid}", txid)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BuscarCobrancaResponse.class)
                .block();
    }

    public void criarLoteCobrancaComVencimento(String loteId, CriarLoteCobrancaVencimentoRequest request) {
        webClient.put()
                .uri("/v2/pix/lotecobv/{id}", loteId)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    public BuscarCobrancaResponse revisarCobrancaComVencimento(String txid, RevisarCobrancaVencimentoRequest request) {
        return webClient.patch()
                .uri("/v2/pix/cobv/{txid}", txid)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BuscarCobrancaResponse.class)
                .block();
    }

    public BuscarCobrancaResponse cancelarCobrancaComVencimento(String txid) {
        RevisarCobrancaVencimentoRequest request = new RevisarCobrancaVencimentoRequest(
                null, // calendario
                null, // devedor
                null, // valor
                null, // abatimento
                null, // chave
                null, // solicitacaoPagador
                null, // infoAdicionais
                "REMOVIDA_PELO_USUARIO_RECEBEDOR"
        );
        return revisarCobrancaComVencimento(txid, request);
    }

    public BuscarCobrancaVencimentoResponse buscarCobrancaComVencimento(String txid) {
        return webClient.get()
                .uri("/v2/pix/cobv/{txid}", txid)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .bodyToMono(BuscarCobrancaVencimentoResponse.class)
                .block();
    }

    public ListarCobrancasVencimentoResponse listarCobrancasComVencimento(
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Integer paginaAtual,
            Integer itensPorPagina,
            String cpf,
            String cnpj,
            String status
    ) {
        String inicioStr = inicio.toString();
        String fimStr = fim.toString();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/pix/cobv")
                        .queryParam("inicio", inicioStr)
                        .queryParam("fim", fimStr)
                        .queryParamIfPresent("cpf", Optional.ofNullable(cpf))
                        .queryParamIfPresent("cnpj", Optional.ofNullable(cnpj))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParam("paginacao.paginaAtual", paginaAtual != null ? paginaAtual : 0)
                        .queryParam("paginacao.itensPorPagina", itensPorPagina != null ? itensPorPagina : 100)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .bodyToMono(ListarCobrancasVencimentoResponse.class)
                .block();
    }





    /* Filter reutilizável */
    private static ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(resp -> {
            if (resp.statusCode().isError()) {
                return resp.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> Mono.error(mapToEureka(resp.statusCode().value(), body)));
            }
            return Mono.just(resp);
        });
    }
}
