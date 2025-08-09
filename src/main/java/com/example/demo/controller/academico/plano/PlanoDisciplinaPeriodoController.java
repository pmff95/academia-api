package com.example.demo.controller.academico.plano;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.academico.plano.resultado.PlanoDisciplinaPeriodo;
import com.example.demo.service.academico.plano.resultado.PlanoDisciplinaPeriodoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/academico/plano/resultado/plano-disciplina-periodos")
@Tag(name = "Plano Disciplina Período", description = "Endpoints para gerenciamento de PlanoDisciplinaPeriodo")
@RequiredArgsConstructor
public class PlanoDisciplinaPeriodoController {

    private final PlanoDisciplinaPeriodoService service;

    @PostMapping
    @EurekaApiOperation(summary = "Criar registro", description = "Cria um novo PlanoDisciplinaPeriodo")
    public ResponseEntity<ApiReturn<UUID>> create(@RequestBody @Valid PlanoDisciplinaPeriodo request) {
        UUID uuid = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiReturn.of(uuid));
    }

    @GetMapping
    @EurekaApiOperation(summary = "Listar registros", description = "Lista todos os PlanoDisciplinaPeriodo")
    public ResponseEntity<ApiReturn<List<PlanoDisciplinaPeriodo>>> list() {
        return ResponseEntity.ok(ApiReturn.of(service.findAll()));
    }

    @GetMapping("/{uuid}")
    @EurekaApiOperation(summary = "Buscar registro", description = "Busca PlanoDisciplinaPeriodo pelo UUID")
    public ResponseEntity<ApiReturn<PlanoDisciplinaPeriodo>> find(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiReturn.of(service.findByUuid(uuid, PlanoDisciplinaPeriodo.class)));
    }

    @PutMapping("/{uuid}")
    @EurekaApiOperation(summary = "Atualizar registro", description = "Atualiza PlanoDisciplinaPeriodo pelo UUID")
    public ResponseEntity<ApiReturn<String>> update(@PathVariable UUID uuid,
                                                    @RequestBody @Valid PlanoDisciplinaPeriodo request) {
        service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @PutMapping("/{uuid}/inativar")
    @EurekaApiOperation(summary = "Inativar registro", description = "Inativa PlanoDisciplinaPeriodo pelo UUID")
    public ResponseEntity<ApiReturn<String>> deactivate(@PathVariable UUID uuid) {
        service.changeStatus(uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Inativado com sucesso."));
    }

    @PutMapping("/{uuid}/ativar")
    @EurekaApiOperation(summary = "Ativar registro", description = "Ativa PlanoDisciplinaPeriodo pelo UUID")
    public ResponseEntity<ApiReturn<String>> activate(@PathVariable UUID uuid) {
        service.changeStatus(uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Ativado com sucesso."));
    }
}
