package com.example.demo.controller.academico;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.domain.enums.Serie;
import com.example.demo.dto.academico.SerieHorarioAulaCadastroRequest;
import com.example.demo.dto.academico.SerieHorarioAulaCopiarRequest;
import com.example.demo.dto.academico.SerieHorarioSerieView;
import com.example.demo.dto.academico.SerieHorarioAulaTurmaView;
import com.example.demo.dto.projection.SerieHorarioAulaSummary;
import com.example.demo.service.academico.SerieHorarioAulaService;
import com.example.demo.service.academico.SerieHorarioItinerarioService;
import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/serie-horario-aula")
@Tag(name = "SerieHorarioAula", description = "Horários padrão das aulas por série")
public class SerieHorarioAulaController {

    private final SerieHorarioAulaService service;
    private final SerieHorarioItinerarioService itinerarioService;

    public SerieHorarioAulaController(SerieHorarioAulaService service,
                                      SerieHorarioItinerarioService itinerarioService) {
        this.service = service;
        this.itinerarioService = itinerarioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Definir horários",
            description = "Define os horários padrões das aulas para as séries")
    public ResponseEntity<ApiReturn<String>> salvar(@RequestBody @Valid SerieHorarioAulaCadastroRequest request) {
        service.salvarHorarios(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Horários salvos com sucesso."));
    }

    @PostMapping("/itinerarios")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Definir horários de itinerário",
            description = "Define os horários reservados para itinerários")
    public ResponseEntity<ApiReturn<String>> salvarItinerarios(
            @RequestBody @Valid com.example.demo.dto.academico.SerieHorarioItinerarioCadastroRequest request) {
        itinerarioService.salvarHorarios(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Horários salvos com sucesso."));
    }

    @GetMapping("/turmas/{turmaUuid}/itinerarios")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar horários de itinerário",
            description = "Lista os horários reservados da turma")
    public ResponseEntity<ApiReturn<java.util.List<com.example.demo.dto.projection.SerieHorarioItinerarioSummary>>> listarItinerarios(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid) {
        var list = itinerarioService.listar(turmaUuid, com.example.demo.dto.projection.SerieHorarioItinerarioSummary.class);
        return ResponseEntity.ok(ApiReturn.of(list));
    }

    @GetMapping("/turmas/{turmaUuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar horários da turma",
            description = "Retorna os horários cadastrados para a série da turma")
    public ResponseEntity<ApiReturn<SerieHorarioAulaTurmaView>> listarPorTurma(
            @Parameter(description = "UUID da turma", required = true)
            @PathVariable UUID turmaUuid) {
        var view = service.listarPorTurmaComItinerarios(turmaUuid);
        return ResponseEntity.ok(ApiReturn.of(view));
    }

    @GetMapping("/series/{serie}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar horários da série",
            description = "Retorna os horários cadastrados para a série")
    public ResponseEntity<ApiReturn<List<SerieHorarioAulaSummary>>> listarPorSerie(
            @Parameter(description = "Série", required = true)
            @PathVariable Serie serie) {
        var list = service.listarPorSerie(serie, SerieHorarioAulaSummary.class);
        return ResponseEntity.ok(ApiReturn.of(list));
    }

    @GetMapping("/series")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Listar séries",
            description = "Lista todas as séries e indica se possuem horários cadastrados")
    public ResponseEntity<ApiReturn<List<SerieHorarioSerieView>>> listarSeries() {
        return ResponseEntity.ok(ApiReturn.of(service.listarSeriesComStatus()));
    }

    @DeleteMapping("/series/{serie}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Remover horários",
            description = "Remove todos os horários cadastrados da série")
    public ResponseEntity<ApiReturn<String>> remover(
            @Parameter(description = "Série", required = true)
            @PathVariable Serie serie) {
        service.removerHorarios(serie);
        return ResponseEntity.ok(ApiReturn.of("Horários removidos."));
    }

    @PostMapping("/series/copiar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(summary = "Copiar horários",
            description = "Copia os horários cadastrados de uma série para outra")
    public ResponseEntity<ApiReturn<String>> copiar(@RequestBody @Valid SerieHorarioAulaCopiarRequest request) {
        service.copiarHorarios(request.destino(), request.origem());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Horários copiados com sucesso."));
    }
}
