package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<String>> solicitar(@PathVariable UUID alunoUuid) {
        String msg = service.solicitar(alunoUuid);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @PostMapping("/{uuid}/responder")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiResponse<String>> responder(@PathVariable UUID uuid,
                                                         @RequestParam boolean aceita) {
        String msg = service.responder(uuid, aceita);
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @GetMapping("/alunos/{alunoUuid}")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiResponse<List<SolicitacaoDTO>>> listarPendentes(@PathVariable UUID alunoUuid) {
        List<SolicitacaoDTO> lista = service.listarPendentes(alunoUuid);
        return ResponseEntity.ok(new ApiResponse<>(true, "Solicitações pendentes", lista, null));
    }
}
