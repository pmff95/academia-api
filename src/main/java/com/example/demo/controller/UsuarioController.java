package com.example.demo.controller;

import com.example.demo.service.UsuarioService;
import com.example.demo.common.response.ApiReturn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuários")
@RestController
@RequestMapping("/api/public")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/master")
    public ResponseEntity<ApiReturn<String>> criarMaster() {
        return ResponseEntity.ok(ApiReturn.of(service.criarUsuarioMaster()));
    }

    @PutMapping("/{uuid}/ativo")
    public ResponseEntity<ApiReturn<String>> alterarAtivo(@PathVariable UUID uuid, @RequestParam boolean ativo) {
        return ResponseEntity.ok(ApiReturn.of(service.alterarAtivo(uuid, ativo)));
    }
}
