package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.ProdutoDTO;
import com.example.demo.service.ProdutoFavoritoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Favoritos")
@RestController
@RequestMapping("/favoritos")
public class FavoritoController {
    private final ProdutoFavoritoService service;

    public FavoritoController(ProdutoFavoritoService service) {
        this.service = service;
    }

    @PostMapping("/{produtoUuid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiReturn<String>> favoritar(@PathVariable UUID produtoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.favoritar(produtoUuid)));
    }

    @DeleteMapping("/{produtoUuid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiReturn<String>> desfavoritar(@PathVariable UUID produtoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.desfavoritar(produtoUuid)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiReturn<Page<ProdutoDTO>>> listar(Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.listar(pageable)));
    }
}
