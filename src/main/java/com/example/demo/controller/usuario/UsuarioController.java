package com.example.demo.controller.usuario;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.security.accesscontrol.EntityNames;
import com.example.demo.common.security.accesscontrol.annotation.CheckAccess;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.dto.projection.usuario.UsuarioFull;
import com.example.demo.dto.projection.usuario.UsuarioSummary;
import com.example.demo.dto.usuario.*;
import com.example.demo.repository.specification.UsuarioSpecification;
import com.example.demo.service.aluno.AlunoService;
import com.example.demo.service.professor.ProfessorService;
import com.example.demo.service.usuario.AuthenticationService;
import com.example.demo.service.usuario.UsuarioService;
import com.example.demo.dto.common.StorageOutput;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final AuthenticationService authenticationService;
    private final ProfessorService professorService;
    private final AlunoService alunoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Criar um usuário",
            description = "Cria e persiste um novo usuário contendo as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de um usuário",
                    required = true
            )
            @ModelAttribute @Valid UsuarioRequest request
    ) {
        service.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiReturn.of("Usuário cadastrado com sucesso."));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PDV','RESPONSAVEL','ALUNO','PROFESSOR','RESPONSAVEL_CONTRATUAL')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Atualizar um usuário",
            description = "Atualiza, a partir do seu UUID, um usuário persistido com as informações especificadas na requisição."
    )
    public ResponseEntity<ApiReturn<String>> update(
            @Parameter(description = "UUID do usuário a ser atualizado", required = true)
            @PathVariable("uuid") UUID uuid,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de um usuário",
                    required = true
            )
            @ModelAttribute @Valid UsuarioRequest request
    ) {
        service.updateUser(uuid, request);
        return ResponseEntity.ok(ApiReturn.of("Usuário atualizado com sucesso."));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Lista os usuários",
            description = "Retorna um page contendo usuários de acordo com os filtros especificados."
    )
    public ResponseEntity<ApiReturn<Page<UsuarioSummary>>> findAll(
            @ParameterObject UsuarioSpecification specification,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiReturn.of(service.findAll(specification, pageable)));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PDV','RESPONSAVEL','ALUNO','PROFESSOR')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Busca um usuário",
            description = "Busca, a partir do seu UUID, um usuário persistido."
    )
    public ResponseEntity<ApiReturn<Object>> findByUuid(
            @Parameter(description = "UUID do usuário a ser buscado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        Usuario usuario = service.findByUuid(uuid);
        Object resposta;
        if (usuario.getPerfil() == Perfil.PROFESSOR) {
            resposta = professorService.findFull(uuid);
        } else if (usuario.getPerfil() == Perfil.ALUNO) {
            resposta = alunoService.findFull(uuid);
        } else {
            resposta = service.findByUuid(uuid, UsuarioFull.class);
        }
        return ResponseEntity.ok(ApiReturn.of(resposta));
    }

    @GetMapping("/current")
    @EurekaApiOperation(
            summary = "Busca o usuário logado",
            description = "Busca, o usuário que está logado."
    )
    public ResponseEntity<ApiReturn<CurrentUserView>> buscarUsuarioLogado() {
        return ResponseEntity.ok(ApiReturn.of(service.findCurrent()));
    }

    @PutMapping("/{uuid}/inativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Inativa um usuário",
            description = "Inativa, a partir do seu UUID, um usuário persistido."
    )
    public ResponseEntity<ApiReturn<String>> inativar(
            @Parameter(description = "UUID do usuário a ser inativado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        service.changeUserStatus(uuid, Status.INATIVO);
        return ResponseEntity.ok(ApiReturn.of("Usuário inativado com sucesso."));
    }

    @PutMapping("/{uuid}/ativar")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Ativa um usuário",
            description = "Ativa, a partir do seu UUID, um usuário persistido."
    )
    public ResponseEntity<ApiReturn<String>> ativar(
            @Parameter(description = "UUID do usuário a ser ativado", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        service.changeUserStatus(uuid, Status.ATIVO);
        return ResponseEntity.ok(ApiReturn.of("Usuário reativado com sucesso."));
    }

    @PutMapping("/{uuid}/definir-senha")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Definir senha de usuário",
            description = "Define uma nova senha numérica para o usuário informado."
    )
    public ResponseEntity<ApiReturn<String>> definirSenha(
            @Parameter(description = "UUID do usuário", required = true)
            @PathVariable("uuid") UUID uuid,
            @RequestBody @Valid DefinirSenhaRequest request
    ) {
        authenticationService.definirSenha(uuid, request.senha());
        return ResponseEntity.ok(ApiReturn.of("Senha definida com sucesso."));
    }

    @PutMapping("/{uuid}/foto")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','ALUNO','PROFESSOR','RESPONSAVEL','PDV','RESPONSAVEL_CONTRATUAL')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Alterar foto do usuário",
            description = "Realiza upload e altera a foto do usuário informado."
    )
    public ResponseEntity<ApiReturn<String>> alterarFoto(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("foto") MultipartFile foto
    ) {
        StorageOutput output = service.alterarImagemUsuario(uuid, foto);
        return ResponseEntity.ok(ApiReturn.of(output != null ? output.getUrl() : null));
    }

    @DeleteMapping("/{uuid}/foto")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','ALUNO','PROFESSOR','RESPONSAVEL','PDV','RESPONSAVEL_CONTRATUAL')")
    @CheckAccess(entity = EntityNames.USUARIO)
    @EurekaApiOperation(
            summary = "Remover foto do usuário",
            description = "Remove a foto do usuário informado."
    )
    public ResponseEntity<ApiReturn<String>> removerFoto(
            @PathVariable("uuid") UUID uuid
    ) {
        service.removerImagemUsuario(uuid);
        return ResponseEntity.ok(ApiReturn.of("Foto removida."));
    }
}
