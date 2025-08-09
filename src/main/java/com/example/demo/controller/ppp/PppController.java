package com.example.demo.controller.ppp;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.ppp.PppRequest;
import com.example.demo.dto.ppp.PppView;
import com.example.demo.service.ppp.PppService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/ppp")
@Tag(name = "PPP", description = "Endpoints para gerenciamento do PPP")
@RequiredArgsConstructor
public class PppController {

    private final PppService service;

    @PostMapping
    @EurekaApiOperation(summary = "Cadastrar PPP", description = "Cria um novo PPP")
    public ResponseEntity<ApiReturn<UUID>> create(@Valid @RequestBody PppRequest request) {
        UUID uuid = service.create(request);
        return ResponseEntity.ok(ApiReturn.of(uuid));
    }

    @GetMapping("/{uuid}")
    @EurekaApiOperation(summary = "Buscar PPP", description = "Consulta um PPP existente")
    public ResponseEntity<ApiReturn<PppView>> get(
            @Parameter(description = "UUID do PPP", required = true)
            @PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findViewByUuid(uuid)));
    }

    @GetMapping("/{uuid}/pdf")
    @EurekaApiOperation(summary = "Gerar PDF", description = "Gera o PDF do PPP")
    public ResponseEntity<byte[]> pdf(
            @Parameter(description = "UUID do PPP", required = true)
            @PathVariable UUID uuid) throws IOException {
        byte[] pdf = service.gerarPdf(uuid);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ppp.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
