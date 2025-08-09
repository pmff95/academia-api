package com.example.demo.controller.professor;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.common.security.accesscontrol.annotation.CheckAccess;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.professor.HorarioDisponivelCadastroRequest;
import com.example.demo.dto.professor.ProfessorRequest;
import com.example.demo.dto.projection.*;
import com.example.demo.dto.professor.ProfessorFullResponse;
import com.example.demo.dto.professor.ProfessorAulasResponse;
import com.example.demo.repository.specification.ProfessorSpecification;
import com.example.demo.service.professor.ProfessorService;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/professores")
@Tag(name = "Professores", description = "Endpoints para gerenciamento de professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Criar um professor",
            description = "Cria e persiste um novo professor contendo as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de um professor",
                    required = true
            )
            @RequestPart("professor") @Valid ProfessorRequest request,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        this.service.create(request, foto);
        return ResponseEntity.ok(ApiReturn.of("Professor cadastrado com sucesso."));
    }

    @PutMapping(value = "/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','PROFESSOR')")
    @EurekaApiOperation(
            summary = "Atualizar um professor",
            description = "Atualiza, a partir do seu UUID, um professor persistido com as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> update(
            @Parameter(description = "UUID do professor a ser atualizado", required = true)
            @PathVariable("uuid") UUID uuid,
            @RequestPart("professor") @Valid ProfessorRequest request,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        this.service.update(uuid, request, foto);
        return ResponseEntity.ok(ApiReturn.of("Professor atualizado com sucesso."));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Lista os professores",
            description = "Retorna um page contendo professores de acordo com os filtros especificados."
    )
    public ResponseEntity<ApiReturn<Page<ProfessorSummary>>> findAll(
            @ParameterObject ProfessorSpecification specification,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findAll(specification, pageable)));
    }

    @PutMapping("/{uuid}/ativar")
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Ativa um professor",
            description = "Ativa, a partir do seu UUID, um professor persistido."
    )
    public ResponseEntity<ApiReturn<String>> activate(
            @Parameter(description = "UUID do professor a ser ativado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        this.service.changeStudentStatus(uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Professor ativado com sucesso."));
    }

    @PutMapping("/{uuid}/inativar")
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Inativa um professor",
            description = "Inativa, a partir do seu UUID, um professor persistido."
    )
    public ResponseEntity<ApiReturn<String>> deactivate(
            @Parameter(description = "UUID do professor a ser inativado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        this.service.changeStudentStatus(uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Professor inativado com sucesso."));
    }

    @GetMapping("/{uuid}")
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','PROFESSOR')")
    @EurekaApiOperation(
            summary = "Busca um professor",
            description = "Busca, a partir do seu UUID, um professor persistido."
    )
    public ResponseEntity<ApiReturn<ProfessorFullResponse>> findByUuid(
            @Parameter(description = "UUID do professor a ser buscado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findFull(uuid)));
    }

    @PostMapping("/horarios")
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasAnyRole('ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(
            summary = "Cadastro de horarios disponíveis",
            description = "Cadastro de horarios disponíveis do professor."
    )
    public ResponseEntity<ApiReturn<Boolean>> cadastroHorariosProfessor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados dos horarios disponíveis do professor",
                    required = true
            )
            @RequestBody @Valid HorarioDisponivelCadastroRequest request
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.service.vincularHorariosDisponiveis(request)));
    }

    @GetMapping("/{uuid}/horarios")
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','PROFESSOR')")
    @EurekaApiOperation(
            summary = "Lista horários disponíveis",
            description = "Lista os horários disponíveis de um professor."
    )
    public ResponseEntity<ApiReturn<List<HorarioDisponivelSummary>>> listarHorariosDisponiveis(
            @Parameter(description = "UUID do professor", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.service.listarHorariosDisponiveis(uuid)));
    }

    @GetMapping("/disciplinas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Lista professores com disciplinas",
            description = "Lista todos os professores ativos e suas disciplinas"
    )
    public ResponseEntity<ApiReturn<List<ProfessorDisciplinaView>>> listarComDisciplinas() {
        return ResponseEntity.ok(ApiReturn.of(this.service.listarProfessoresComDisciplinas()));
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('PROFESSOR')")
    @EurekaApiOperation(
            summary = "Busca o professor logado",
            description = "Retorna o professor autenticado com suas disciplinas"
    )
    public ResponseEntity<ApiReturn<ProfessorFullResponse>> buscarProfessorLogado() {
        return ResponseEntity.ok(ApiReturn.of(this.service.findCurrent()));
    }

    @GetMapping("/turmas")
    @PreAuthorize("hasRole('PROFESSOR')")
    @EurekaApiOperation(
            summary = "Sugestões de turmas e disciplinas do professor",
            description = "Retorna as turmas e disciplinas vinculadas ao professor logado."
    )
    public ResponseEntity<ApiReturn<List<TurmaDisciplinaView>>> listarSugestoesDoProfessorLogado() {
        return ResponseEntity.ok(ApiReturn.of(service.listarSugestoesProfessorLogado()));
    }

    @GetMapping("/{uuid}/aulas")
    @CheckAccess(entity = EntityNames.PROFESSOR)
    @PreAuthorize("hasRole('PROFESSOR')")
    @EurekaApiOperation(
            summary = "Lista aulas do professor",
            description = "Retorna as aulas normais e de itinerário do professor"
    )
    public ResponseEntity<ApiReturn<ProfessorAulasResponse>> listarAulas(
            @Parameter(description = "UUID do professor", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.listarAulas(uuid)));
    }

}
