package com.example.demo.controller;

import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.dto.CategoriaListagemDTO;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoHistoricoDTO;
import com.example.demo.service.FichaTreinoService;
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
public class FichaTreinoController {
    private final FichaTreinoService service;

    public FichaTreinoController(FichaTreinoService service) {
        this.service = service;
    }

    @PutMapping
    public ResponseEntity<ApiReturn<String>> salvar(@Validated @RequestBody FichaTreinoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.save(dto)));
    }

    @GetMapping("/{fichaUuid}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR','ALUNO')")
    public ResponseEntity<ApiReturn<FichaTreinoDTO>> detalhar(@PathVariable UUID fichaUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(fichaUuid)));
    }

    @GetMapping("/historico/{alunoUuid}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR','ALUNO')")
    public ResponseEntity<ApiReturn<List<FichaTreinoHistoricoDTO>>> historicoPorAluno(@PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findHistoricoByAluno(alunoUuid)));
    }

    @GetMapping("/presets")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<List<FichaTreinoDTO>>> listarPresetsProfessor() {
        return ResponseEntity.ok(ApiReturn.of(service.findPresetsByProfessor()));
    }

    @PostMapping("/preset/{presetUuid}/aluno/{alunoUuid}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> atribuirPreset(@PathVariable UUID presetUuid, @PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.assignPreset(presetUuid, alunoUuid)));
    }

    @GetMapping("/ficha-atual")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<FichaTreinoDTO>> fichaAlunoLogado() {
        UUID alunoUuid = SecurityUtils.getUsuarioLogadoDetalhes().getUuid();
        return ResponseEntity.ok(ApiReturn.of(service.findCurrentByAluno(alunoUuid)));
    }

    @GetMapping("/ficha-atual/{alunoUuid}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ADMIN')")
    public ResponseEntity<ApiReturn<FichaTreinoDTO>> fichaAluno(@PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findCurrentByAluno(alunoUuid)));
    }

    @PutMapping("/ficha-atual/{fichaUuid}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ADMIN')")
    public ResponseEntity<ApiReturn<String>> atualizarFichaAtual(@PathVariable UUID fichaUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.atualizarFichaAtual(fichaUuid)));
    }
    
    @GetMapping("/listagem")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR','ALUNO')")
    public ResponseEntity<ApiReturn<List<CategoriaListagemDTO>>> listagem(@RequestParam(required = false) UUID usuarioUuid) {
        return ResponseEntity.ok(ApiReturn.of(service.listarCategorias(usuarioUuid)));
    }
}