package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import com.example.demo.domain.enums.Tema;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuário Logado")
@RestController
@RequestMapping("/api/usuario")
@PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR','ALUNO', 'FORNECEDOR')")
public class UsuarioLogadoController {

    private final UsuarioService service;

    public UsuarioLogadoController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public ResponseEntity<ApiReturn<UsuarioDTO>> obterUsuarioLogado() {
        return ResponseEntity.ok(service.buscarUsuarioLogado());
    }

    @PutMapping("/tema")
    public ResponseEntity<ApiReturn<String>> alterarTema(@RequestParam Tema tema) {
        return ResponseEntity.ok(ApiReturn.of(service.alterarTema(tema)));
    }
}
