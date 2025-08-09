package com.example.demo.common.config.api;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * - cobPagWebClientBuilder  → usa mutual-TLS (para a API de cobrança)
 * - plainWebClientBuilder   → sem certificado (para qualquer outra chamada HTTP)
 */
@Configuration
public class WebClientConfig {

    /* ---------- CobPag (mutual-TLS) ---------- */
    @Bean
    public WebClient.Builder cobPagWebClientBuilder(
            @Value("${escola.cobpag.api.path.cert.crt}") String certPath,
            @Value("${escola.cobpag.api.path.cert.key}") String keyPath
    ) throws SSLException {

        SslContext sslContext = SslContextBuilder.forClient()
                .keyManager(new File(certPath), new File(keyPath))
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(ssl -> ssl.sslContext(sslContext));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }

    /* ---------- “Plain” (sem cert) ---------- */
    @Bean
    public WebClient.Builder plainWebClientBuilder() {
        return WebClient.builder();  // sem customização
    }
}
