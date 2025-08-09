package com.example.demo.controller.aluno;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.common.security.accesscontrol.annotation.CheckAccess;
import com.example.demo.domain.enums.Status;
import com.example.demo.dto.aluno.*;
import com.example.demo.dto.projection.TurmaDisciplinaAlunoView;
import com.example.demo.repository.specification.AlunoSpecification;
import com.example.demo.service.academico.TurmaDisciplinaService;
import com.example.demo.service.aluno.AlergiaService;
import com.example.demo.service.aluno.AlunoService;
import com.example.demo.service.aluno.MedicamentoService;
import com.example.demo.service.aluno.ResponsavelAlunoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos")
public class AlunoController {

    private final AlunoService service;
    private final ResponsavelAlunoService responsavelAlunoService;
    private final AlergiaService alergiaService;
    private final MedicamentoService medicamentoService;
    private final TurmaDisciplinaService turmaDisciplinaService;

    public AlunoController(AlunoService service, ResponsavelAlunoService responsavelAlunoService,
                           AlergiaService alergiaService,
                           MedicamentoService medicamentoService,
                           TurmaDisciplinaService turmaDisciplinaService) {
        this.service = service;
        this.responsavelAlunoService = responsavelAlunoService;
        this.alergiaService = alergiaService;
        this.medicamentoService = medicamentoService;
        this.turmaDisciplinaService = turmaDisciplinaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Criar um aluno",
            description = "Cria e persiste um novo aluno contendo as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de um aluno",
                    required = true
            )
            @ModelAttribute @Valid AlunoRequest request
    ) {
        String retorno = this.service.create(request);
        return ResponseEntity.ok(ApiReturn.of(retorno));
    }

    @PutMapping("/{uuid}")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','ALUNO')")
    @EurekaApiOperation(
            summary = "Atualizar um aluno",
            description = "Atualiza, a partir do seu UUID, um aluno persistido com as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> update(
            @Parameter(description = "UUID do aluno a ser atualizado", required = true)
            @PathVariable("uuid") UUID uuid,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados do aluno",
                    required = true
            )
            @ModelAttribute @Valid AlunoRequest request
    ) {
        this.service.update(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Aluno atualizado com sucesso."));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Lista os alunos",
            description = "Retorna um page contendo alunos de acordo com os filtros especificados."
    )
    public ResponseEntity<ApiReturn<Page<AlunoSummaryDTO>>> findAll(
            @ParameterObject AlunoSpecification specification,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findAll(specification, pageable)));
    }

    @GetMapping("/{uuid}")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','ALUNO')")
    @EurekaApiOperation(
            summary = "Busca um aluno",
            description = "Busca, a partir do seu UUID, um aluno persistido."
    )
    public ResponseEntity<ApiReturn<AlunoFullResponse>> findByUuid(
            @Parameter(description = "UUID do aluno a ser buscado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.service.findFull(uuid)));
    }

    @PutMapping("/{uuid}/ativar")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Ativa um aluno",
            description = "Ativa, a partir do seu UUID, um aluno persistido."
    )
    public ResponseEntity<ApiReturn<String>> activate(
            @Parameter(description = "UUID do aluno a ser ativado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        this.service.changeStudentStatus(uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Aluno ativado com sucesso."));
    }

    @PutMapping("/{uuid}/inativar")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Inativa um aluno",
            description = "Inativa, a partir do seu UUID, um aluno persistido."
    )
    public ResponseEntity<ApiReturn<String>> deactivate(
            @Parameter(description = "UUID do aluno a ser inativado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        this.service.changeStudentStatus(uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Aluno inativado com sucesso."));
    }

    @PutMapping("/{uuid}/responsavel")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Adicionar responsável",
            description = "Adiciona ou atualiza o responsável de um aluno."
    )
    public ResponseEntity<ApiReturn> addResponsavel(
            @Parameter(description = "UUID do aluno", required = true)
            @PathVariable("uuid") UUID uuid,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados do responsável",
                    required = true
            )
            @ModelAttribute @Valid ResponsavelAlunoRequest request
    ) {
        this.responsavelAlunoService.create(request, uuid);
        return ResponseEntity.ok(ApiReturn.of("Responsáveis adicionados com sucesso!"));
    }

    @PutMapping("/{uuid}/alergias")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Cadastrar alergias",
            description = "Cadastra uma lista de alergias para o aluno informado."
    )
    public ResponseEntity<ApiReturn<String>> cadastrarAlergias(
            @Parameter(description = "UUID do aluno", required = true)
            @PathVariable("uuid") UUID uuid,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lista de alergias",
                    required = true
            )
            @RequestBody @Valid List<AlergiaRequest> request
    ) {
        this.alergiaService.salvarLista(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Alergias cadastradas com sucesso."));
    }

    @GetMapping("/{uuid}/alergias")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','ALUNO')")
    @EurekaApiOperation(
            summary = "Listar alergias",
            description = "Lista as alergias cadastradas para o aluno."
    )
    public ResponseEntity<ApiReturn<List<com.example.demo.domain.model.saude.Alergia>>> listarAlergias(
            @Parameter(description = "UUID do aluno", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.alergiaService.listarPorAluno(uuid)));
    }

    @PutMapping("/{uuid}/medicamentos")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Cadastrar medicamentos",
            description = "Cadastra uma lista de medicamentos para o aluno informado."
    )
    public ResponseEntity<ApiReturn<String>> cadastrarMedicamentos(
            @Parameter(description = "UUID do aluno", required = true)
            @PathVariable("uuid") UUID uuid,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lista de medicamentos",
                    required = true
            )
            @RequestBody @Valid List<MedicamentoRequest> request
    ) {
        this.medicamentoService.salvarLista(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Medicamentos cadastrados com sucesso."));
    }

    @GetMapping("/{uuid}/medicamentos")
    @CheckAccess(entity = EntityNames.ALUNO)
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','ALUNO')")
    @EurekaApiOperation(
            summary = "Listar medicamentos",
            description = "Lista os medicamentos cadastrados para o aluno."
    )
    public ResponseEntity<ApiReturn<List<com.example.demo.domain.model.saude.Medicamento>>> listarMedicamentos(
            @Parameter(description = "UUID do aluno", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(this.medicamentoService.listarPorAluno(uuid)));
    }

    @GetMapping("/disciplinas")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','RESPONSAVEL','ALUNO')")
    @EurekaApiOperation(
            summary = "Listar disciplinas do aluno",
            description = "Lista as disciplinas do aluno logado e retorna os itinerários quando houver."
    )
    public ResponseEntity<ApiReturn<List<TurmaDisciplinaAlunoView>>> listarDisciplinas() {
        return ResponseEntity.ok(ApiReturn.of(
                this.turmaDisciplinaService.findByLoggedAluno(TurmaDisciplinaAlunoView.class)
        ));
    }
}
