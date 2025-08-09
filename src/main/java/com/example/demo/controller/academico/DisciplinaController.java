package com.example.demo.controller.academico;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.academico.DisciplinaRequest;
import com.example.demo.dto.academico.DisciplinaCreateRequest;
import com.example.demo.dto.academico.DisciplinaView;
import com.example.demo.service.academico.DisciplinaService;
import com.example.demo.repository.specification.DisciplinaSpecification;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.enums.NivelEnsino;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/disciplinas")
@Tag(name = "Disciplinas", description = "Endpoints para gerenciamento de disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Criar uma disciplina",
            description = "Cria e persiste uma nova disciplina."
    )
    public ResponseEntity<ApiReturn<String>> create(@RequestBody @Valid DisciplinaCreateRequest request) {
        this.service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Disciplina cadastrada com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Atualizar disciplina",
            description = "Atualiza, a partir do seu UUID, uma disciplina persistida com as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> update(@PathVariable UUID uuid, @RequestBody @Valid DisciplinaRequest request) {
        this.service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Disciplina atualizada com sucesso."));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Listar disciplinas",
            description = "Retorna todas as disciplinas ordenadas por nome."
    )
    public ResponseEntity<ApiReturn<List<DisciplinaView>>> findAll(
            @ParameterObject DisciplinaSpecification specification) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findAll(specification)));
    }

    @PostMapping("/por-niveis")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Listar disciplinas por nível de ensino",
            description = "Retorna as disciplinas correspondentes aos níveis enviados."
    )
    public ResponseEntity<ApiReturn<List<DisciplinaView>>> findByNiveis(@RequestBody List<NivelEnsino> niveis) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findByNiveis(niveis)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Buscar disciplina por UUID",
            description = "Retorna os dados de uma disciplina pelo seu UUID."
    )
    public ResponseEntity<ApiReturn<DisciplinaView>> findByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findByUuid(uuid)));
    }

    @PutMapping("/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Ativar disciplina",
            description = "Ativa, a partir do seu UUID, uma disciplina persistida."
    )
    public ResponseEntity<ApiReturn<String>> activate(@PathVariable UUID uuid) {
        this.service.changeStatus(uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Disciplina ativada com sucesso."));
    }

    @PutMapping("/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Inativar disciplina",
            description = "Inativa, a partir do seu UUID, uma disciplina persistida."
    )
    public ResponseEntity<ApiReturn<String>> deactivate(@PathVariable UUID uuid) {
        this.service.changeStatus(uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Disciplina inativada com sucesso."));
    }
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Remover disciplina",
            description = "Exclui uma disciplina caso não esteja em uso"
    )
    public ResponseEntity<ApiReturn<String>> delete(@PathVariable UUID uuid) {
        this.service.delete(uuid);
        return ResponseEntity.ok(ApiReturn.of("Disciplina removida com sucesso."));
    }
}
