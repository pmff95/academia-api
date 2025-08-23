package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.ProdutoDTO;
import com.example.demo.service.ProdutoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Produtos")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FORNECEDOR','MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> criar(@RequestPart("dto") ProdutoDTO dto,
                                                   @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto, imagem)));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<Page<ProdutoDTO>>> listar(@RequestParam UUID fornecedorUuid,
                                                              Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findByFornecedor(fornecedorUuid, pageable)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiReturn<ProdutoDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('FORNECEDOR','MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid,
                                                       @RequestPart("dto") ProdutoDTO dto,
                                                       @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto, imagem)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('FORNECEDOR','MASTER','ADMIN')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Produto removido"));
    }
}

