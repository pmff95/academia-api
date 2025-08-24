package com.example.demo.service;

import com.example.demo.dto.MercadoPagoPreferenceDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceReceiverAddressRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceShipmentsRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

//    @Value("${mercadopago.access-token:}")
//    private String accessToken;
//
//    public String criarPreferencia(MercadoPagoPreferenceDTO dto) {
//        MercadoPagoConfig.setAccessToken(accessToken);
//        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
//                .title(dto.getTitulo())
//                .quantity(dto.getQuantidade())
//                .unitPrice(dto.getValor())
//                .currencyId("BRL")
//                .build();
//
//        PreferenceRequest.Builder builder = PreferenceRequest.builder()
//                .items(List.of(itemRequest))
//                .notificationUrl(dto.getNotificationUrl());
//
//        if (dto.getEntrega() != null && !dto.getEntrega().isRetirada()) {
//            PreferenceReceiverAddressRequest address = PreferenceReceiverAddressRequest.builder()
//                    .zipCode(dto.getEntrega().getCep())
//                    .streetName(dto.getEntrega().getEndereco())
//                    .streetNumber(dto.getEntrega().getNumero())
//                    .cityName(dto.getEntrega().getCidade())
//                    .stateName(dto.getEntrega().getEstado())
//                    .build();
//            PreferenceShipmentsRequest shipments = PreferenceShipmentsRequest.builder()
//                    .receiverAddress(address)
//                    .build();
//            builder.shipments(shipments);
//        }
//
//        PreferenceRequest request = builder.build();
//
//        PreferenceClient client = new PreferenceClient();
//        Preference preference = client.create(request);
//        return preference.getId();
//    }
//
//    public void tratarWebhook(String payload, Map<String, String> headers) {
//        // Aqui poderíamos processar a atualização do pagamento
//        System.out.println("Webhook MercadoPago: " + payload);
//    }
}

