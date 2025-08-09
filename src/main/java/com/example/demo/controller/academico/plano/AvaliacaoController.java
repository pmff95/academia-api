package com.example.demo.controller.academico.plano;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.academico.plano.avaliacao.CalendarioProvaRequest;
import com.example.demo.dto.academico.plano.avaliacao.CalendarioProvaResponse;
import com.example.demo.service.academico.plano.avaliacao.AvaliacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/avaliacoes")
@Tag(name = "Avaliações", description = "Endpoints para gerenciamento de avaliações")
public class AvaliacaoController {

    private final AvaliacaoService service;

    public AvaliacaoController(AvaliacaoService service) {
        this.service = service;
    }

    @PostMapping("/calendario")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Criar calendário de provas", description = "Define datas de provas para disciplinas")
    public ResponseEntity<ApiReturn<String>> criarCalendario(@RequestBody @Valid CalendarioProvaRequest request) {
        String mensagem = service.criarCalendario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiReturn.of(mensagem));
    }

    @GetMapping("/calendario/{turmaUuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Listar calendário de provas", description = "Retorna o calendário de provas da turma")
    public ResponseEntity<ApiReturn<CalendarioProvaResponse>> buscarCalendario(@PathVariable UUID turmaUuid) {
        CalendarioProvaResponse response = service.buscarCalendario(turmaUuid);
        return ResponseEntity.ok(ApiReturn.of(response));
    }
}

