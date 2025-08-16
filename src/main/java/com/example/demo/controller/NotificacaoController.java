package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.NotificacaoDTO;
import com.example.demo.service.NotificacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notificações")
@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {
    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiReturn<List<NotificacaoDTO>>> listar() {
        return ResponseEntity.ok(ApiReturn.of(service.listarDoUsuarioLogado()));
    }

    @PostMapping("/{uuid}/lida")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiReturn<String>> marcarComoLida(@PathVariable UUID uuid) {
        service.marcarComoLida(uuid);
        return ResponseEntity.ok(ApiReturn.of("Notificação marcada como lida"));
    }
}
