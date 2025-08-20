package com.example.demo.controller;

import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.*;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.service.AlunoMedidaService;
import com.example.demo.service.AlunoObservacaoService;
import com.example.demo.service.AlunoService;
import com.example.demo.service.TreinoSessaoService;
import com.example.demo.common.security.SecurityUtils;
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
    private final TreinoSessaoService treinoSessaoService;

    public AlunoController(AlunoService service,
                           AlunoMedidaService medidaService,
                           AlunoObservacaoService observacaoService,
                           TreinoSessaoService treinoSessaoService) {
        this.service = service;
        this.medidaService = medidaService;
        this.observacaoService = observacaoService;
        this.treinoSessaoService = treinoSessaoService;
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

    @GetMapping("/disponiveis")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<ApiReturn<Page<AlunoDTO>>> listarDisponiveis(@RequestParam(required = false) String nome,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAllNotFromLoggedProfessor(nome, pageable)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<AlunoDTO>> buscar(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> atualizar(@PathVariable UUID uuid, @Validated @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(service.update(uuid, dto)));
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('ALUNO')")
    public ResponseEntity<ApiReturn<String>> atualizarUsuarioLogado( @Validated @RequestBody AlunoDTO dto) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
        return ResponseEntity.ok(ApiReturn.of(service.update(usuarioLogado.getUuid(), dto)));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> remover(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Aluno removido"));
    }

    @PostMapping("/{uuid}/medidas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<AlunoMedidaResultadoDTO>> adicionarMedida(@PathVariable UUID uuid,
                                                                              @Validated @RequestBody AlunoMedidaDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(medidaService.adicionarMedida(uuid, dto)));
    }

    @GetMapping("/{uuid}/medidas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<List<AlunoMedidaDTO>>> listarMedidas(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(medidaService.listarMedidas(uuid)));
    }

    @GetMapping("/me/medidas")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<List<AlunoMedidaDTO>>> listarMedidasAlunoLogado() {
        return ResponseEntity.ok(ApiReturn.of(medidaService.listarMedidas(SecurityUtils.getUsuarioLogadoDetalhes().getUuid())));
    }

    @PostMapping("/{uuid}/observacoes")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<String>> adicionarObservacao(@PathVariable UUID uuid,
                                                                 @Validated @RequestBody AlunoObservacaoDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(observacaoService.adicionarObservacao(uuid, dto)));
    }

    @GetMapping("/{uuid}/observacoes")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<List<AlunoObservacaoDTO>>> listarObservacoes(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(observacaoService.listarObservacoes(uuid)));
    }

    @PostMapping("/registrar-treino")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<Double>> registrarTreino(@Validated @RequestBody TreinoSessaoDTO dto) {
        UUID uuid = SecurityUtils.getUsuarioLogadoDetalhes().getUuid();
        return ResponseEntity.ok(ApiReturn.of(treinoSessaoService.registrarSessao(uuid, dto)));
    }

    @GetMapping("/me/treinos/percentual/{categoriaUuid}")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<ApiReturn<Double>> percentualTreino(@PathVariable UUID categoriaUuid) {
        UUID uuid = SecurityUtils.getUsuarioLogadoDetalhes().getUuid();
        return ResponseEntity.ok(ApiReturn.of(treinoSessaoService.buscarPercentualDoDia(uuid, categoriaUuid)));
    }

    @GetMapping("/{uuid}/treinos")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','PROFESSOR')")
    public ResponseEntity<ApiReturn<List<TreinoSessaoDTO>>> listarTreinos(@PathVariable UUID alunoUuid) {
        return ResponseEntity.ok(ApiReturn.of(treinoSessaoService.listarSessoes(alunoUuid)));
    }
}
