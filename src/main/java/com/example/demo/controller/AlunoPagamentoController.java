package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.AlunoPagamentoDTO;
import com.example.demo.service.AlunoPagamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Pagamentos")
@RestController
@RequestMapping("/alunos")
public class AlunoPagamentoController {
    private final AlunoPagamentoService service;

    public AlunoPagamentoController(AlunoPagamentoService service) {
        this.service = service;
    }

    @PostMapping("/{uuid}/pagamentos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> registrarPagamento(@PathVariable UUID uuid,
                                                                 @Validated @RequestBody AlunoPagamentoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.registrar(uuid, dto)));
    }
}
