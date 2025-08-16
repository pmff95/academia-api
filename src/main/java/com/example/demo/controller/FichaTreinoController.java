package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoHistoricoDTO;
import com.example.demo.service.FichaTreinoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Fichas de Treino")
@RestController
@RequestMapping("/fichas")
@PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
public class FichaTreinoController {
    private final FichaTreinoService service;

    public FichaTreinoController(FichaTreinoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody FichaTreinoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

//    @GetMapping
//    public ResponseEntity<ApiReturn<Page<FichaTreinoDTO>>> listar(Pageable pageable) {
//        return ResponseEntity.ok(ApiReturn.of(service.findAll(pageable)));
//    }

    @GetMapping("/{fichaUuid}")
    public ResponseEntity<ApiReturn<FichaTreinoDTO>> detalhar(@PathVariable UUID fichaUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(fichaUuid)));
    }
//
//    @GetMapping("/aluno/{alunoUuid}")
//    public ResponseEntity<ApiReturn<List<FichaTreinoDTO>>> listarPorAluno(@PathVariable UUID alunoUuid) {
//        return ResponseEntity.ok(ApiReturn.of(service.findByAluno(alunoUuid)));
//    }

    @GetMapping("/historico/{alunoUuid}")
    public ResponseEntity<ApiReturn<List<FichaTreinoHistoricoDTO>>> historicoPorAluno(@PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findHistoricoByAluno(alunoUuid)));
    }

    @GetMapping("/presets")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<List<FichaTreinoDTO>>> listarPresetsProfessor() {
        return ResponseEntity.ok(ApiReturn.of(service.findPresetsByProfessor()));
    }

    @PostMapping("/preset/{presetUuid}/aluno/{alunoUuid}")
    public ResponseEntity<ApiReturn<String>> atribuirPreset(@PathVariable UUID presetUuid, @PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.assignPreset(presetUuid, alunoUuid)));
    }

    @GetMapping("/ficha-atual")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<FichaTreinoDTO>> fichaAlunoLogado() {
        UUID alunoUuid = SecurityUtils.getUsuarioLogadoDetalhes().getUuid();
        return ResponseEntity.ok(ApiReturn.of(service.findCurrentByAluno(alunoUuid)));
    }

    @PutMapping("/ficha-atual/{fichaUuid}")
    public ResponseEntity<ApiReturn<String>> atualizarFichaAtual(@PathVariable UUID fichaUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.atualizarFichaAtual(fichaUuid)));
    }
}
