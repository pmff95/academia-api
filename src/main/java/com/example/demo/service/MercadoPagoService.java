package com.example.demo.service;

import com.example.demo.dto.MercadoPagoCartaoDTO;
import com.example.demo.dto.MercadoPagoQrCodeDTO;
import com.example.demo.dto.EnderecoDTO;
import com.example.demo.entity.MercadoPagoPagamento;
import com.example.demo.entity.Endereco;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.MercadoPagoPagamentoRepository;
import com.example.demo.repository.EnderecoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
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
import java.util.UUID;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token:}")
    private String accessToken;

    private final MercadoPagoPagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;

    public MercadoPagoService(MercadoPagoPagamentoRepository pagamentoRepository,
                              UsuarioRepository usuarioRepository,
                              EnderecoRepository enderecoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
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
            preencherUsuarioEndereco(pagamento, dto.getEnderecoUuid(), dto.getEndereco());
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

            preencherUsuarioEndereco(pagamento, dto.getEnderecoUuid(), dto.getEndereco());
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
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherUsuarioEndereco(MercadoPagoPagamento pagamento,
                                          UUID enderecoUuid,
                                          EnderecoDTO enderecoDto) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuarioLogado == null) {
            throw new ApiException("Usuário não autenticado");
        }

        Usuario usuario = usuarioRepository.findByUuid(usuarioLogado.getUuid())
                .orElseThrow(() -> new ApiException("Usuário não encontrado"));

        Endereco endereco;
        if (enderecoUuid != null) {
            endereco = enderecoRepository.findById(enderecoUuid)
                    .orElseThrow(() -> new ApiException("Endereço não encontrado"));
        } else if (enderecoDto != null) {
            endereco = new Endereco();
            endereco.setUsuario(usuario);
            endereco.setLogradouro(enderecoDto.getLogradouro());
            endereco.setBairro(enderecoDto.getBairro());
            endereco.setCidade(enderecoDto.getCidade());
            endereco.setUf(enderecoDto.getUf());
            endereco.setCep(enderecoDto.getCep());
            endereco = enderecoRepository.save(endereco);
        } else {
            throw new ApiException("Endereço obrigatório");
        }

        pagamento.setUsuario(usuario);
        pagamento.setEndereco(endereco);
    }
}
