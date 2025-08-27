package com.example.demo.service;

import com.example.demo.dto.MercadoPagoCartaoDTO;
import com.example.demo.dto.MercadoPagoQrCodeDTO;
import com.example.demo.entity.MercadoPagoPagamento;
import com.example.demo.repository.MercadoPagoPagamentoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerIdentificationRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
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

    public MercadoPagoService(MercadoPagoPagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public MercadoPagoPagamento pagarCartao(MercadoPagoCartaoDTO dto) {
        MercadoPagoConfig.setAccessToken(accessToken);

        PaymentClient client = new PaymentClient();

        PaymentPayerIdentificationRequest identification = PaymentPayerIdentificationRequest.builder()
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
    }

    public MercadoPagoPagamento criarQrCode(MercadoPagoQrCodeDTO dto) {
        MercadoPagoConfig.setAccessToken(accessToken);

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title(dto.getDescricao())
                .quantity(1)
                .unitPrice(dto.getValor())
                .currencyId("BRL")
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(List.of(item))
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        MercadoPagoPagamento pagamento = new MercadoPagoPagamento();
        pagamento.setMercadoPagoId(preference.getId());
        pagamento.setStatus("pending");
        pagamento.setTipo("QRCODE");
        if (preference.getInitPoint() != null) {
            pagamento.setDetalhe(preference.getInitPoint());
        }
        pagamentoRepository.save(pagamento);

        return pagamento;
    }

    public void tratarWebhook(String payload, Map<String, String> headers) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);
            if (node.has("data") && node.get("data").has("id")) {
                String id = node.get("data").get("id").asText();
                MercadoPagoConfig.setAccessToken(accessToken);
                PaymentClient client = new PaymentClient();
                Payment payment = client.get(id);
                pagamentoRepository.findByMercadoPagoId(id).ifPresent(pag -> {
                    pag.setStatus(payment.getStatus());
                    pagamentoRepository.save(pag);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
