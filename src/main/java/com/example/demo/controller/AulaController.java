package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.entity.Aula;
import com.example.demo.service.AulaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/aulas")
public class AulaController {
    private final AulaService service;

    public AulaController(AulaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiReturn<Aula>> criar(@Validated @RequestBody Aula aula) {
        return ResponseEntity.ok(ApiReturn.of(service.salvar(aula)));
    }

    @GetMapping
    public ResponseEntity<ApiReturn<List<Aula>>> listar() {
        return ResponseEntity.ok(ApiReturn.of(service.listar()));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiReturn<Aula>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody Aula aula) {
        aula.setUuid(uuid);
        return ResponseEntity.ok(ApiReturn.of(service.salvar(aula)));
    }

    @PostMapping("/{uuid}/inscricoes")
    public ResponseEntity<ApiReturn<String>> inscrever(@PathVariable UUID uuid, @RequestParam UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.inscrever(uuid, alunoUuid)));
    }

    @DeleteMapping("/{uuid}/inscricoes/{alunoUuid}")
    public ResponseEntity<ApiReturn<String>> cancelar(@PathVariable UUID uuid, @PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.cancelar(uuid, alunoUuid)));
    }
}
