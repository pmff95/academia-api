package com.example.demo.controller.instituicao;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.common.security.accesscontrol.annotation.CheckAccess;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.instituicao.EscolaFinanceiro;
import com.example.demo.dto.instituicao.EscolaCreationRequest;
import com.example.demo.dto.instituicao.EscolaFinanceiroRequest;
import com.example.demo.dto.instituicao.EscolaRequest;
import com.example.demo.dto.projection.escola.EscolaIdAndName;
import com.example.demo.dto.projection.escola.EscolaView;
import com.example.demo.dto.instituicao.EscolaFullResponse;
import com.example.demo.dto.projection.usuario.UsuarioFull;
import com.example.demo.dto.usuario.UsuarioRequest;
import com.example.demo.dto.parametro.ParametroEscolaDTO;
import com.example.demo.dto.common.StorageOutput;
import com.example.demo.repository.specification.EscolaSpecification;
import com.example.demo.service.instituicao.EscolaFinanceiroService;
import com.example.demo.service.instituicao.EscolaResponsavelService;
import com.example.demo.service.instituicao.EscolaService;
import com.example.demo.service.common.StorageService;
import com.example.demo.service.parametro.ParametroService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/escolas")
@Tag(name = "Escolas", description = "Endpoints para gerenciamento de escolas")
@RequiredArgsConstructor
public class EscolaController {

    private final EscolaService service;
    private final EscolaResponsavelService escolaResponsavelService;
    private final EscolaFinanceiroService escolaFinanceiroService;
    private final StorageService storageService;
    private final ParametroService parametroService;

    /**
     * Cria uma nova escola.
     * Exemplo de requisição: POST /api/escolas
     * Corpo da requisição (JSON):
     * {
     * "nome": "Escola Exemplo",
     * "status": "ATIVO",
     * ...
     * }
     */
    @PostMapping
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Criar uma escola",
            description = "Cria e persiste uma nova escola contendo as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> criarEscola(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de uma escola",
                    required = true
            )
            @RequestBody @Valid EscolaCreationRequest request
    ) {

        service.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiReturn.of("Escola criada com sucesso."));
    }

    /**
     * Atualiza os dados de uma escola existente identificada pelo UUID.
     * Exemplo de requisição: PUT /api/escolas/{uuid}
     * Corpo da requisição (JSON):
     * {
     * "nome": "Escola Atualizada",
     * "status": "ATIVO",
     * ...
     * }
     */
    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Atualizar uma escola",
            description = "Atualiza, a partir do seu UUID, uma escola persistida com as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> atualizarEscola(
            @Parameter(description = "UUID da escola a ser buscada", required = true)
            @PathVariable("uuid") UUID uuid,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados da escola",
                    required = true
            )
            @RequestBody @Valid EscolaRequest request
    ) {
        service.salvar(uuid, request);

        return ResponseEntity.ok(ApiReturn.of("Escola atualizada com sucesso."));
    }

    /**
     * Retorna uma escola pelo UUID.
     * Exemplo de requisição: GET /api/escolas/{uuid}
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @CheckAccess(entity = EntityNames.ESCOLA)
    @EurekaApiOperation(
            summary = "Busca uma escola",
            description = "Busca, a partir do seu UUID, uma escola persistida."
    )
    public ResponseEntity<ApiReturn<EscolaFullResponse>> buscarEscolaPorUuid(
            @Parameter(description = "UUID da escola a ser buscada", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.buscarPorUuid(uuid)));
    }

    @GetMapping("/combobox")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Lista para combobox",
            description = "Retorna a lista de escolas disponíveis para seleção."
    )
    public ResponseEntity<ApiReturn<List<EscolaIdAndName>>> montarCombobox() {

        List<EscolaIdAndName> escolas = new ArrayList<>();

        UsuarioLogado currentUser = SecurityUtils.getUsuarioLogado();
        if (currentUser.possuiPerfil(Perfil.MASTER)) {
            escolas = service.getCombobox();
        } else {
            Escola escola = currentUser.getEscola();
            escolas.add(new EscolaIdAndName() {

                @Override
                public UUID getUuid() {
                    return escola.getUuid();
                }

                @Override
                public String getNome() {
                    return escola.getNome();
                }

            });
        }

        return ResponseEntity.ok(ApiReturn.of(escolas));
    }

    /**
     * Lista as escolas ativas com paginação.
     * Exemplo de requisição: GET /api/escolas?page=0&size=10
     */
    @GetMapping
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Lista as escolas",
            description = "Retorna um page contendo escolas de acordo com os filtros especificados."
    )
    public ResponseEntity<ApiReturn<Page<EscolaView>>> listarEscolas(
            @ParameterObject EscolaSpecification specification,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.listar(specification, pageable)));
    }

    @PutMapping("/{uuid}/inativar")
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Inativa uma escola",
            description = "Inativa, a partir do seu UUID, uma escola persistida."
    )
    public ResponseEntity<ApiReturn<String>> inativarEscola(
            @Parameter(description = "UUID da escola a ser inativada", required = true)
            @PathVariable("uuid") UUID uuid
    ) {

        service.inativar(uuid);

        return ResponseEntity.ok(ApiReturn.of("Escola inativada com sucesso."));
    }

    @PutMapping("/{uuid}/ativar")
    @PreAuthorize("hasRole('MASTER')")
    @EurekaApiOperation(
            summary = "Ativa uma escola",
            description = "Ativa, a partir do seu UUID, uma escola persistida."
    )
    public ResponseEntity<ApiReturn<String>> ativarEscola(
            @Parameter(description = "UUID da escola a ser ativada", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        service.ativar(uuid);
        return ResponseEntity.ok(ApiReturn.of("Escola ativada com sucesso."));
    }

//    /**
//     * Atualiza os parametros de uma escola existente identificada pelo UUID.
//     * Exemplo de requisição: PUT /api/escolas/params/{uuid}
//     */
//    @PutMapping("params/{uuid}")
//    @EurekaApiOperation(
//            summary = "Atualizar os parametros de uma escola",
//            description = "Atualiza, a partir do seu UUID, os parametros de uma escola persistida com as informações especificadas na requisição."
//    )
//    public ResponseEntity<ApiReturn<String>> atualizarParametrosEscola(
//            @Parameter(description = "UUID da escola a ser buscada", required = true)
//            @PathVariable("uuid") UUID uuid,
//
//            @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "Corpo da requisição com os parâmetros a serem salvos da escola",
//                    required = true
//            )
//            @RequestBody @Valid EscolaParametrosRequest request
//    ) {
//        service.atualizarParametrosEscola(uuid, request);
//
//        return ResponseEntity.ok(ApiReturn.of("Escola atualizada com sucesso."));
//    }


    @PutMapping("/{uuid}/responsavel")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Salvar responsável da escola",
            description = "Cria ou atualiza o usuário responsável pela escola."
    )
    public ResponseEntity<ApiReturn<Void>> saveResponsavel(@PathVariable("uuid") UUID uuid, @ModelAttribute UsuarioRequest request) {

        this.escolaResponsavelService.criarOuAtualizarResponsavel(uuid, request);

        return ResponseEntity.ok(ApiReturn.of(null));
    }

    @GetMapping("/{uuid}/responsavel")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Buscar responsável da escola",
            description = "Retorna o usuário responsável pela escola." 
    )
    public ResponseEntity<ApiReturn<UsuarioFull>> getResponsavel(@PathVariable("uuid") UUID uuid) {

        UsuarioFull responsavel = this.escolaResponsavelService.findResponsavelByEscolaId(uuid);

        return ResponseEntity.ok(ApiReturn.of(responsavel));
    }

    @PutMapping("/{uuid}/financeiro")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Salvar dados financeiros",
            description = "Salva ou atualiza as informações financeiras da escola."
    )
    public ResponseEntity<ApiReturn<Void>> saveFinanceiro(@PathVariable("uuid") UUID uuid, @ModelAttribute EscolaFinanceiroRequest request) {

        this.escolaFinanceiroService.save(uuid, request);

        return ResponseEntity.ok(ApiReturn.of(null));
    }

    @GetMapping("/{uuid}/financeiro")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Buscar dados financeiros",
            description = "Retorna as informações financeiras da escola."
    )
    public ResponseEntity<ApiReturn<EscolaFinanceiro>> getFinanceiro(@PathVariable("uuid") UUID uuid) {

        EscolaFinanceiro financeiro = this.escolaFinanceiroService.findByEscolaId(uuid);

        return ResponseEntity.ok(ApiReturn.of(financeiro));
    }

    @PutMapping("/{uuid}/logo")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN')")
    @EurekaApiOperation(
            summary = "Salvar logo da escola",
            description = "Faz upload e define a logo da escola informada."
    )
    public ResponseEntity<ApiReturn<String>> uploadLogo(@PathVariable("uuid") UUID uuid,
                                                       @RequestParam("logo") MultipartFile logo) {

        StorageOutput output = storageService.uploadFile(logo);
        parametroService.criarOuEditarParametroEscola(uuid,
                new ParametroEscolaDTO("logo_url", output != null ? output.getUrl() : null));

        return ResponseEntity.ok(ApiReturn.of("Logo atualizada com sucesso."));
    }
}
