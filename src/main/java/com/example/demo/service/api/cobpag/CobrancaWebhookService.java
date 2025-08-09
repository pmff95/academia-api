package com.example.demo.service.api.cobpag;

import com.example.demo.dto.api.cobpag.normal.BuscarCobrancaResponse;
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
public class CobrancaWebhookService {

    private final WebClient webClient;
    private final String credId;
    private final String credSecret;
    private final String pixKey;
    private volatile AuthTokenResponse currentToken;
    private static final DateTimeFormatter C6_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .withZone(ZoneOffset.UTC);

    public CobrancaWebhookService(
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

    public void configurarWebhook(String chavePix, com.example.dto.cobv.ConfigurarWebhookRequest request) {
        webClient.put()
                .uri("/v2/pix/webhook/{chave}", chavePix)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
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
