package com.example.demo.service;

import com.example.demo.dto.MercadoPagoCartaoDTO;
import com.example.demo.dto.MercadoPagoQrCodeDTO;
import com.example.demo.dto.EnderecoDTO;
import com.example.demo.dto.MercadoPagoItemDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.CompraHistoricoRepository;
import com.example.demo.repository.MercadoPagoPagamentoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ProdutoRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token:}")
    private String accessToken;

    private final MercadoPagoPagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final CompraHistoricoRepository historicoRepository;

    public MercadoPagoService(MercadoPagoPagamentoRepository pagamentoRepository, UsuarioRepository usuarioRepository, CompraHistoricoRepository historicoRepository, ProdutoRepository produtoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.historicoRepository = historicoRepository;
    }

    public MercadoPagoPagamento pagarCartao(MercadoPagoCartaoDTO dto) {
        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentClient client = new PaymentClient();

        try {
            IdentificationRequest identification = IdentificationRequest.builder().type(dto.getDocType()).number(dto.getDocNumber()).build();

            PaymentPayerRequest payer = PaymentPayerRequest.builder().email(dto.getEmail()).identification(identification).build();

            List<MercadoPagoItemDTO> itens = dto.getItens();
            BigDecimal total;
            String descricao;
            if (itens != null && !itens.isEmpty()) {
                total = itens.stream().map(i -> i.getValor().multiply(BigDecimal.valueOf(i.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
                descricao = itens.stream().map(MercadoPagoItemDTO::getTitulo).collect(Collectors.joining(", "));
            } else {
                total = dto.getValor();
                descricao = dto.getDescricao();
            }

            PaymentCreateRequest request = PaymentCreateRequest.builder().transactionAmount(total).token(dto.getToken()).description(descricao).installments(dto.getParcelas()).paymentMethodId(dto.getMetodo()).payer(payer).build();

            Payment payment = client.create(request);

            MercadoPagoPagamento pagamento = new MercadoPagoPagamento();
            pagamento.setMercadoPagoId(payment.getId().toString());
            pagamento.setStatus(payment.getStatus());
            pagamento.setTipo("CARTAO");
            preencherUsuarioEndereco(pagamento, dto.getEndereco(), dto.getNomeContato(), dto.getTelefone(), dto.getTelefoneSecundario());

            if (itens != null) {
                List<MercadoPagoPagamentoProduto> produtos = itens.stream().map(itemDto -> {
                    Produto produto = produtoRepository.findById(itemDto.getProdutoUuid()).orElseThrow(() -> new ApiException("Produto não encontrado"));
                    MercadoPagoPagamentoProduto pp = new MercadoPagoPagamentoProduto();
                    pp.setPagamento(pagamento);
                    pp.setProduto(produto);
                    pp.setQuantidade(itemDto.getQuantidade());
                    pp.setValor(itemDto.getValor());
                    return pp;
                }).collect(Collectors.toList());
                pagamento.setProdutos(produtos);
            }

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
            List<MercadoPagoItemDTO> itens = dto.getItens();
            List<PreferenceItemRequest> items = itens == null ? List.of() : itens.stream().map(i -> PreferenceItemRequest.builder().id(i.getProdutoUuid().toString()).title(i.getTitulo()).quantity(i.getQuantidade()).unitPrice(i.getValor()).currencyId("BRL").build()).collect(Collectors.toList());

            PreferenceRequest request = PreferenceRequest.builder().items(items).build();

            Preference preference = client.create(request);

            MercadoPagoPagamento pagamento = new MercadoPagoPagamento();
            pagamento.setMercadoPagoId(preference.getId());
            pagamento.setStatus("PENDING"); // Mercado Pago inicia como pending
            pagamento.setTipo("QRCODE");

            // 🔹 Se QR Code disponível, salvar a URL
            if (preference.getInitPoint() != null) {
                pagamento.setDetalhe(preference.getInitPoint());
            }

            preencherUsuarioEndereco(pagamento, dto.getEndereco(), dto.getNomeContato(), dto.getTelefone(), dto.getTelefoneSecundario());

            if (itens != null) {
                List<MercadoPagoPagamentoProduto> produtos = itens.stream().map(itemDto -> {
                    Produto produto = produtoRepository.findById(itemDto.getProdutoUuid()).orElseThrow(() -> new ApiException("Produto não encontrado"));
                    MercadoPagoPagamentoProduto pp = new MercadoPagoPagamentoProduto();
                    pp.setPagamento(pagamento);
                    pp.setProduto(produto);
                    pp.setQuantidade(itemDto.getQuantidade());
                    pp.setValor(itemDto.getValor());
                    return pp;
                }).collect(Collectors.toList());
                pagamento.setProdutos(produtos);
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

                    if ("approved".equalsIgnoreCase(payment.getStatus()) && !historicoRepository.existsByPagamento(pag)) {
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

    private void preencherUsuarioEndereco(MercadoPagoPagamento pagamento, EnderecoDTO enderecoDto, String nomeContato, String telefone, String telefoneSecundario) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuarioLogado == null) {
            throw new ApiException("Usuário não autenticado");
        }

        Usuario usuario = usuarioRepository.findByUuid(usuarioLogado.getUuid()).orElseThrow(() -> new ApiException("Usuário não encontrado"));

        if (enderecoDto != null) {
            usuario.setLogradouro(enderecoDto.getLogradouro());
            usuario.setNumero(enderecoDto.getNumero());
            usuario.setBairro(enderecoDto.getBairro());
            usuario.setCidade(enderecoDto.getCidade());
            usuario.setUf(enderecoDto.getUf());
            usuario.setCep(enderecoDto.getCep());
        }

        if (telefone != null) {
            usuario.setTelefone(telefone);
        }

        if (telefoneSecundario != null) {
            usuario.setTelefoneSecundario(telefoneSecundario);
        }

        usuarioRepository.save(usuario);

        pagamento.setUsuario(usuario);
        pagamento.setLogradouro(usuario.getLogradouro());
        pagamento.setNumero(usuario.getNumero());
        pagamento.setBairro(usuario.getBairro());
        pagamento.setCidade(usuario.getCidade());
        pagamento.setUf(usuario.getUf());
        pagamento.setCep(usuario.getCep());
        pagamento.setTelefone(usuario.getTelefone());
        pagamento.setTelefoneSecundario(usuario.getTelefoneSecundario());
        pagamento.setNomeContato(nomeContato != null ? nomeContato : usuario.getNome());
    }
}
