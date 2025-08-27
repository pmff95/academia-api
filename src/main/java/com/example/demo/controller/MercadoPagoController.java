package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.MercadoPagoCartaoDTO;
import com.example.demo.dto.MercadoPagoQrCodeDTO;
import com.example.demo.entity.MercadoPagoPagamento;
import com.example.demo.service.MercadoPagoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Pagamentos")
@RestController
@RequestMapping("/pagamentos")
public class MercadoPagoController {
    private final MercadoPagoService service;

    public MercadoPagoController(
            MercadoPagoService service
    ) {
        this.service = service;
    }

    @PostMapping("/cartao")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<MercadoPagoPagamento>> pagarCartao(@RequestBody MercadoPagoCartaoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.pagarCartao(dto)));
    }

    @PostMapping("/qrcode")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<MercadoPagoPagamento>> criarQrCode(@RequestBody MercadoPagoQrCodeDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.criarQrCode(dto)));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader Map<String, String> headers) {
        service.tratarWebhook(payload, headers);
        return ResponseEntity.ok("ok");
    }
}
