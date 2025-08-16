package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.SolicitacaoDTO;
import com.example.demo.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Solicitações")
@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @PostMapping("/alunos/{alunoUuid}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> solicitar(@PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.solicitar(alunoUuid)));
    }

    @PostMapping("/{uuid}/responder")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<String>> responder(@PathVariable UUID uuid,
                                                       @RequestParam boolean aceita) {
        return ResponseEntity.ok(ApiReturn.of(service.responder(uuid, aceita)));
    }

    @GetMapping("/alunos/{alunoUuid}")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<List<SolicitacaoDTO>>> listarPendentes(@PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.listarPendentes(alunoUuid)));
    }

    @GetMapping("/professores")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<List<SolicitacaoDTO>>> listarPendentesProfessor() {
        return ResponseEntity.ok(ApiReturn.of(service.listarPendentesProfessor()));
    }
}
