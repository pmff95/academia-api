package com.example.demo.controller.aviso;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.aviso.AvisoRequest;
import com.example.demo.dto.projection.aviso.AvisoView;
import com.example.demo.service.aviso.AvisoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/avisos")
@Tag(name = "Avisos", description = "Endpoints para gerenciamento de avisos")
@RequiredArgsConstructor
public class AvisoController {

    private final AvisoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Criar um aviso",
            description = "Cria e persiste um novo aviso"
    )
    public ResponseEntity<ApiReturn<String>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do aviso",
                    required = true
            )
            @RequestBody @Valid AvisoRequest request
    ) {
        service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Aviso criado com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Atualizar um aviso",
            description = "Atualiza, a partir do seu UUID, um aviso"
    )
    public ResponseEntity<ApiReturn<String>> editar(
            @Parameter(description = "UUID do aviso a ser atualizado", required = true)
            @PathVariable("uuid") UUID uuid,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do aviso",
                    required = true
            )
            @RequestBody @Valid AvisoRequest request
    ) {
        service.atualizar(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Aviso atualizado com sucesso."));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','ALUNO','PROFESSOR','PDV','RESPONSAVEL_CONTRATUAL')")
    @EurekaApiOperation(
            summary = "Listar avisos",
            description = "Lista os avisos do mais recente para o mais antigo"
    )
    public ResponseEntity<ApiReturn<List<AvisoView>>> listar() {
        return ResponseEntity.ok(ApiReturn.of(service.listar()));
    }
}
