package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.service.AlunoMedidaService;
import com.example.demo.service.AlunoObservacaoService;
import com.example.demo.service.AlunoService;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Alunos")
@Slf4j
@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService service;
    private final AlunoMedidaService medidaService;
    private final AlunoObservacaoService observacaoService;

    public AlunoController(AlunoService service,
                           AlunoMedidaService medidaService,
                           AlunoObservacaoService observacaoService) {
        this.service = service;
        this.medidaService = medidaService;
        this.observacaoService = observacaoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> criar(@Validated @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.create(dto)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<Page<AlunoDTO>>> listar(@RequestParam(required = false) String nome,
                                                           Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(nome, pageable)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<AlunoDTO>> buscar(@PathVariable UUID uuid) {
        AlunoDTO dto = service.findByUuid(uuid);
        return ResponseEntity.ok(ApiReturn.of(dto));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Aluno removido"));
    }

    @PostMapping("/{uuid}/medidas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> adicionarMedida(@PathVariable UUID uuid,
                                                             @Validated @RequestBody AlunoMedidaDTO dto) {
        String msg = medidaService.adicionarMedida(uuid, dto);
        return ResponseEntity.ok(ApiReturn.of(msg));
    }

    @GetMapping("/{uuid}/medidas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<List<AlunoMedidaDTO>>> listarMedidas(@PathVariable UUID uuid) {
        List<AlunoMedidaDTO> lista = medidaService.listarMedidas(uuid);
        return ResponseEntity.ok(ApiReturn.of(lista));
    }

    @GetMapping("/me/medidas")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<List<AlunoMedidaDTO>>> listarMedidasAlunoLogado() {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        List<AlunoMedidaDTO> lista = medidaService.listarMedidas(usuario.getUuid());
        return ResponseEntity.ok(ApiReturn.of(lista));
    }

    @PostMapping("/{uuid}/observacoes")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> adicionarObservacao(@PathVariable UUID uuid,
                                                                 @Validated @RequestBody AlunoObservacaoDTO dto) {
        String msg = observacaoService.adicionarObservacao(uuid, dto);
        return ResponseEntity.ok(ApiReturn.of(msg));
    }

    @GetMapping("/{uuid}/observacoes")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<List<AlunoObservacaoDTO>>> listarObservacoes(@PathVariable UUID uuid) {
        List<AlunoObservacaoDTO> lista = observacaoService.listarObservacoes(uuid);
        return ResponseEntity.ok(ApiReturn.of(lista));
    }
}
