package com.example.demo.controller.academico.plano.chamada;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.academico.plano.chamada.RegistroChamadaRequest;
import com.example.demo.dto.projection.RegistroChamadaSummary;
import com.example.demo.dto.projection.chamada.RegistroChamadaView;
import com.example.demo.service.academico.plano.chamada.RegistroChamadaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import java.util.UUID;

@RestController
@RequestMapping("/api/chamadas")
@Tag(name = "Chamadas", description = "Gerenciamento de chamadas")
public class RegistroChamadaController {

    private final RegistroChamadaService service;

    public RegistroChamadaController(RegistroChamadaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','PROFESSOR')")
    @EurekaApiOperation(summary = "Registrar chamada", description = "Cria um registro de chamada")
    public ResponseEntity<ApiReturn<String>> create(@RequestBody @Valid RegistroChamadaRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Chamada registrada com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','PROFESSOR')")
    @EurekaApiOperation(summary = "Editar chamada", description = "Edita um registro de chamada")
    public ResponseEntity<ApiReturn<String>> update(@PathVariable UUID uuid,
                                                   @RequestBody @Valid RegistroChamadaRequest request) {
        service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Atualizado com sucesso."));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','PROFESSOR')")
    @EurekaApiOperation(summary = "Listar chamadas", description = "Lista registros de chamada da turma")
    public ResponseEntity<ApiReturn<Page<RegistroChamadaSummary>>> list(
            @RequestParam UUID planoDisciplinaUuid,
            @ParameterObject Pageable pageable) {
        Page<RegistroChamadaSummary> page = service.findAll(planoDisciplinaUuid, pageable);
        return ResponseEntity.ok(ApiReturn.of(page));
    }

    @GetMapping("/data")
    @PreAuthorize("hasAnyRole('MASTER','PROFESSOR')")
    @EurekaApiOperation(summary = "Verificar chamada por data", description = "Retorna os dados do registro de chamada na data informada ou informa se já existe")
    public ResponseEntity<ApiReturn<RegistroChamadaView>> findByDate(
            @RequestParam UUID planoDisciplinaUuid,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAula) {
        RegistroChamadaView view = service.findByDate(planoDisciplinaUuid, dataAula);
        return ResponseEntity.ok(ApiReturn.of(view));
    }
}
