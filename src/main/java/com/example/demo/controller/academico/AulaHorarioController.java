package com.example.demo.controller.academico;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.AulaHorarioRequest;
import com.example.demo.dto.academico.AulaHorarioVinculoRequest;
import com.example.demo.dto.projection.AulaHorarioSummary;
import com.example.demo.dto.projection.TurmaAulaHorarioView;
import com.example.demo.service.academico.AulaHorarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aula-horario")
@Tag(name = "AulaHorarios", description = "Endpoints para gerenciamento de horários de aula")
public class AulaHorarioController {

    private final AulaHorarioService service;

    public AulaHorarioController(AulaHorarioService service) {
        this.service = service;
    }

    @PostMapping("/{turmaUuid}/disciplinas/aulas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Adicionar aulas",
            description = "Adiciona horários de aula para as disciplinas da turma")
    public ResponseEntity<ApiReturn<String>> createAll(
            @PathVariable UUID turmaUuid,
            @RequestBody @Valid List<AulaHorarioVinculoRequest> requests) {
        this.service.createAll(turmaUuid, requests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Aulas cadastradas com sucesso."));
    }

//    @PostMapping("/{turmaUuid}/disciplinas/{turmaDisciplinaUuid}/aulas")
//    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
//    @EurekaApiOperation(summary = "Adicionar aula",
//            description = "Adiciona horário de aula na disciplina da turma")
//    public ResponseEntity<ApiReturn<String>> create(
//            @PathVariable UUID turmaUuid,
//            @PathVariable UUID turmaDisciplinaUuid,
//            @RequestBody @Valid AulaHorarioRequest request) {
//        this.service.create(turmaDisciplinaUuid, request);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiReturn.of("Aula cadastrada com sucesso."));
//    }

//    @PutMapping("/{turmaUuid}/disciplinas/{turmaDisciplinaUuid}/aulas/{uuid}")
//    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
//    @EurekaApiOperation(summary = "Atualizar aula",
//            description = "Atualiza horário de aula da disciplina da turma")
//    public ResponseEntity<ApiReturn<String>> update(
//            @PathVariable UUID turmaUuid,
//            @PathVariable UUID turmaDisciplinaUuid,
//            @PathVariable UUID uuid,
//            @RequestBody @Valid AulaHorarioRequest request) {
//        this.service.update(turmaDisciplinaUuid, uuid, request);
//        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
//    }
//
//    @GetMapping("/{turmaUuid}/disciplinas/{turmaDisciplinaUuid}/aulas")
//    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
//    @EurekaApiOperation(summary = "Listar aulas",
//            description = "Lista horários de aula da disciplina da turma")
//    public ResponseEntity<ApiReturn<Page<AulaHorarioSummary>>> list(
//            @PathVariable UUID turmaUuid,
//            @PathVariable UUID turmaDisciplinaUuid,
//            @ParameterObject Pageable pageable) {
//        Page<AulaHorarioSummary> page = this.service.findAll(turmaDisciplinaUuid, pageable);
//        return ResponseEntity.ok(ApiReturn.of(page));
//    }
//
//    @GetMapping("/{turmaUuid}/disciplinas/{turmaDisciplinaUuid}/aulas/{uuid}")
//    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
//    @EurekaApiOperation(summary = "Buscar aula",
//            description = "Busca horário de aula pelo UUID")
//    public ResponseEntity<ApiReturn<AulaHorarioSummary>> find(
//            @PathVariable UUID turmaUuid,
//            @PathVariable UUID turmaDisciplinaUuid,
//            @PathVariable UUID uuid) {
//        AulaHorarioSummary summary = this.service
//                .findByUuid(turmaDisciplinaUuid, uuid, AulaHorarioSummary.class);
//        return ResponseEntity.ok(ApiReturn.of(summary));
//    }
//
//    @PutMapping("/{turmaUuid}/disciplinas/{turmaDisciplinaUuid}/aulas/{uuid}/inativar")
//    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
//    @EurekaApiOperation(summary = "Inativar aula",
//            description = "Inativa horário de aula da disciplina da turma")
//    public ResponseEntity<ApiReturn<String>> inativar(
//            @PathVariable UUID turmaUuid,
//            @PathVariable UUID turmaDisciplinaUuid,
//            @PathVariable UUID uuid) {
//        this.service.changeStatus(turmaDisciplinaUuid, uuid, Status.INATIVO);
//        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
//    }
//
//    @PutMapping("/{turmaUuid}/disciplinas/{turmaDisciplinaUuid}/aulas/{uuid}/ativar")
//    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
//    @EurekaApiOperation(summary = "Ativar aula",
//            description = "Ativa horário de aula da disciplina da turma")
//    public ResponseEntity<ApiReturn<String>> ativar(
//            @PathVariable UUID turmaUuid,
//            @PathVariable UUID turmaDisciplinaUuid,
//            @PathVariable UUID uuid) {
//        this.service.changeStatus(turmaDisciplinaUuid, uuid, Status.ATIVO);
//        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
//    }

    @GetMapping("/turmas/{turmaUuid}/grade")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Grade da turma",
            description = "Lista ordem, disciplina, professor e dia da semana da turma")
    public ResponseEntity<ApiReturn<java.util.List<TurmaAulaHorarioView>>> listarGrade(
            @PathVariable UUID turmaUuid) {
        var list = this.service.listarPorTurma(turmaUuid, TurmaAulaHorarioView.class);
        return ResponseEntity.ok(ApiReturn.of(list));
    }

    @GetMapping("/turmas/{turmaUuid}/grade/itinerarios/{grupoUuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Grade do grupo", description = "Lista aulas do grupo de itinerário")
    public ResponseEntity<ApiReturn<java.util.List<TurmaAulaHorarioView>>> listarGradeGrupo(
            @PathVariable UUID turmaUuid,
            @PathVariable UUID grupoUuid) {
        var list = this.service.listarPorTurmaEGrupo(turmaUuid, grupoUuid, TurmaAulaHorarioView.class);
        return ResponseEntity.ok(ApiReturn.of(list));
    }
}
