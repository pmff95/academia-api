package com.example.demo.service;

import com.example.demo.dto.MercadoPagoCartaoDTO;
import com.example.demo.dto.MercadoPagoQrCodeDTO;
import com.example.demo.entity.MercadoPagoPagamento;
import com.example.demo.entity.CompraHistorico;
import com.example.demo.repository.MercadoPagoPagamentoRepository;
import com.example.demo.repository.CompraHistoricoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token:}")
    private String accessToken;

    private final MercadoPagoPagamentoRepository pagamentoRepository;
    private final CompraHistoricoRepository historicoRepository;

    public MercadoPagoService(MercadoPagoPagamentoRepository pagamentoRepository,
                              CompraHistoricoRepository historicoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.historicoRepository = historicoRepository;
    }


    public MercadoPagoPagamento pagarCartao(MercadoPagoCartaoDTO dto) {
        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentClient client = new PaymentClient();

        try {
            IdentificationRequest identification = IdentificationRequest.builder()
                    .type(dto.getDocType())
                    .number(dto.getDocNumber())
                    .build();

            PaymentPayerRequest payer = PaymentPayerRequest.builder()
                    .email(dto.getEmail())
                    .identification(identification)
                    .build();

            PaymentCreateRequest request = PaymentCreateRequest.builder()
                    .transactionAmount(dto.getValor())
                    .token(dto.getToken())
                    .description(dto.getDescricao())
                    .installments(dto.getParcelas())
                    .paymentMethodId(dto.getMetodo())
                    .payer(payer)
                    .build();

            Payment payment = client.create(request);

            MercadoPagoPagamento pagamento = new MercadoPagoPagamento();
            pagamento.setMercadoPagoId(payment.getId().toString());
            pagamento.setStatus(payment.getStatus());
            pagamento.setTipo("CARTAO");
            pagamentoRepository.save(pagamento);

            return pagamento;

        } catch (MPException | MPApiException e) {
            e.printStackTrace(); // ou logger

            // Aqui você pode até salvar um registro de erro no banco
            MercadoPagoPagamento erro = new MercadoPagoPagamento();
            erro.setStatus("ERRO");
            erro.setTipo("CARTAO");
            erro.setDetalhe(e.getMessage()); // exemplo
            pagamentoRepository.save(erro);

            throw new RuntimeException("Erro ao criar pagamento no Mercado Pago", e);
        }
    }


    public MercadoPagoPagamento criarQrCode(MercadoPagoQrCodeDTO dto) {
        MercadoPagoConfig.setAccessToken(accessToken);
        PreferenceClient client = new PreferenceClient();

        try {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(dto.getDescricao())
                    .quantity(1)
                    .unitPrice(dto.getValor())
                    .currencyId("BRL")
                    .build();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(List.of(item))
                    .build();

            Preference preference = client.create(request);

            MercadoPagoPagamento pagamento = new MercadoPagoPagamento();
            pagamento.setMercadoPagoId(preference.getId());
            pagamento.setStatus("PENDING"); // Mercado Pago inicia como pending
            pagamento.setTipo("QRCODE");

            // 🔹 Se QR Code disponível, salvar a URL
            if (preference.getInitPoint() != null) {
                pagamento.setDetalhe(preference.getInitPoint());
            }

            pagamentoRepository.save(pagamento);
            return pagamento;

        } catch (Exception e) {
            e.printStackTrace();

            // 🔹 Registra o erro no banco
            MercadoPagoPagamento erro = new MercadoPagoPagamento();
            erro.setStatus("ERRO");
            erro.setTipo("QRCODE");
            erro.setDetalhe(e.getMessage());
            pagamentoRepository.save(erro);

            throw new RuntimeException("Erro ao criar QR Code no Mercado Pago", e);
        }
    }


    public void tratarWebhook(String payload, Map<String, String> headers) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);
            if (node.has("data") && node.get("data").has("id")) {
                String id = node.get("data").get("id").asText();
                MercadoPagoConfig.setAccessToken(accessToken);
                PaymentClient client = new PaymentClient();
                Payment payment = client.get(Long.valueOf(id));
                pagamentoRepository.findByMercadoPagoId(id).ifPresent(pag -> {
                    pag.setStatus(payment.getStatus());
                    pagamentoRepository.save(pag);

                    if ("approved".equalsIgnoreCase(payment.getStatus()) &&
                            !historicoRepository.existsByPagamento(pag)) {
                        CompraHistorico historico = new CompraHistorico();
                        historico.setPagamento(pag);
                        historico.setDescricao(payment.getDescription());
                        historico.setValor(payment.getTransactionAmount());
                        historicoRepository.save(historico);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
