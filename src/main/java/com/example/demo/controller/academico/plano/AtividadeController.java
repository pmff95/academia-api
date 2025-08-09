package com.example.demo.controller.academico.plano;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.plano.AtividadeRequest;
import com.example.demo.dto.academico.plano.EntregaAtividadeRequest;
import com.example.demo.dto.academico.plano.RegistrarNotaItemRequest;
import com.example.demo.dto.projection.AtividadeAlunoSummary;
import com.example.demo.dto.projection.AtividadeSummary;
import com.example.demo.dto.projection.EntregaAtividadeAlunoViewImpl;
import com.example.demo.service.academico.plano.atividade.AtividadeService;
import com.example.demo.service.academico.plano.atividade.EntregaAtividadeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/atividades")
@Tag(name = "Atividades", description = "Endpoints para gerenciamento de atividades")
public class AtividadeController {

    private final AtividadeService service;
    private final EntregaAtividadeService entregaService;

    public AtividadeController(AtividadeService service, EntregaAtividadeService entregaService) {
        this.service = service;
        this.entregaService = entregaService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('MASTER','PROFESSOR')")
    @EurekaApiOperation(summary = "Criar atividade", description = "Cria uma nova atividade")
    public ResponseEntity<ApiReturn<String>> create(
            @RequestPart("dados") @Valid AtividadeRequest request,
            @RequestPart("arquivo") MultipartFile arquivo
    ) {
        request = new AtividadeRequest(
                request.titulo(),
                request.descricao(),
                request.prazo(),
                request.todosAlunos(),
                request.alunosSelecionados(),
                arquivo,
                request.planoDisciplinaUuid()
        );
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Atividade cadastrada com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Atualizar atividade", description = "Atualiza uma atividade pelo UUID")
    public ResponseEntity<ApiReturn<String>> update(
            @PathVariable UUID uuid,
            @RequestBody @Valid AtividadeRequest request
    ) {
        service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @GetMapping
    @EurekaApiOperation(summary = "Listar atividades", description = "Lista as atividades da disciplina")
    public ResponseEntity<ApiReturn<Page<AtividadeSummary>>> list(
            @RequestParam UUID planoDisciplinaUuid,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(planoDisciplinaUuid, pageable)));
    }

    @GetMapping("/aluno")
    @PreAuthorize("hasAnyRole('ALUNO','RESPONSAVEL')")
    @EurekaApiOperation(summary = "Listar atividades do aluno", description = "Lista as atividades da disciplina com status de entrega")
    public ResponseEntity<ApiReturn<Page<AtividadeAlunoSummary>>> listAluno(
            @RequestParam UUID planoDisciplinaUuid,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.findAllAluno(planoDisciplinaUuid, pageable)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Buscar atividade", description = "Busca uma atividade pelo UUID")
    public ResponseEntity<ApiReturn<AtividadeSummary>> find(
            @PathVariable UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid, AtividadeSummary.class)));
    }

    @PutMapping("/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Inativar atividade", description = "Inativa a atividade pelo UUID")
    public ResponseEntity<ApiReturn<String>> deactivate(
            @PathVariable UUID uuid
    ) {
        service.changeStatus(uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
    }

    @PutMapping("/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Ativar atividade", description = "Ativa a atividade pelo UUID")
    public ResponseEntity<ApiReturn<String>> activate(
            @PathVariable UUID uuid
    ) {
        service.changeStatus(uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
    }

    @PostMapping(value = "/{atividadeUuid}/entrega", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ALUNO')")
    @EurekaApiOperation(summary = "Entregar atividade", description = "Realiza a entrega da atividade pelo aluno")
    public ResponseEntity<ApiReturn<String>> entregar(
            @PathVariable UUID atividadeUuid,
            @ModelAttribute @Valid EntregaAtividadeRequest request
    ) {
        entregaService.entregar(atividadeUuid, request.arquivo());
        return ResponseEntity.ok(ApiReturn.of("Atividade entregue com sucesso."));
    }

    @GetMapping("/{turmaUuid}/{atividadeUuid}/alunos")
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(summary = "Listar entregas da atividade", description = "Lista os alunos e o status da entrega")
    public ResponseEntity<ApiReturn<java.util.List<EntregaAtividadeAlunoViewImpl>>> listarEntregas(
            @PathVariable UUID atividadeUuid,
            @PathVariable UUID turmaUuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(entregaService.listarEntregas(atividadeUuid, turmaUuid)));
    }

    @PostMapping("/{atividadeUuid}/notas")
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    @EurekaApiOperation(summary = "Registrar notas", description = "Registra a nota das entregas da atividade")
    public ResponseEntity<ApiReturn<String>> registrarNotas(
            @PathVariable UUID atividadeUuid,
            @RequestBody @Valid List<RegistrarNotaItemRequest> notas
    ) {
        entregaService.registrarNotas(atividadeUuid, notas);
        return ResponseEntity.ok(ApiReturn.of("Notas registradas com sucesso."));
    }
}
