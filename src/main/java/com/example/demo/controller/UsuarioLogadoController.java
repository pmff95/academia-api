package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuário Logado")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioLogadoController {

    private final UsuarioService service;

    public UsuarioLogadoController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obterUsuarioLogado() {
        return ResponseEntity.ok(service.buscarUsuarioLogado());
    }
}
