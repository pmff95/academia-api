package com.example.demo.controller.vendas;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.vendas.CriarPagamentoRequest;
import com.example.demo.service.vendas.PagamentoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/aluno/pagamento", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pagamento", description = "Endpoints para pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ALUNO', 'RESPONSAVEL')")
    @EurekaApiOperation(
            summary = "Registrar pré-compra",
            description = "Registra um pagamento pendente para posterior confirmação."
    )
    public ResponseEntity<ApiReturn<String>> registrarPreCompra(
            @RequestBody List<CriarPagamentoRequest> produto
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.registrarPreCompra(produto)));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ALUNO', 'RESPONSAVEL')")
    @EurekaApiOperation(
            summary = "Confirmar compra",
            description = "Confirma o pagamento previamente registrado."
    )
    public ResponseEntity<ApiReturn<String>> confirmarCompra(
            @Parameter(description = "UUID do pagamento a ser buscado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.confirmarCompra(uuid)));
    }

}
