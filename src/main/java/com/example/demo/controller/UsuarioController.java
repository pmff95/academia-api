package com.example.demo.controller;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuários")
@RestController
@RequestMapping("/api/public/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/master")
    public ResponseEntity<String> criarMaster(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.criarUsuarioMaster(dto));
    }

    @PutMapping("/{uuid}/ativo")
    public ResponseEntity<String> alterarAtivo(@PathVariable UUID uuid, @RequestParam boolean ativo) {
        return ResponseEntity.ok(service.alterarAtivo(uuid, ativo));
    }
}
