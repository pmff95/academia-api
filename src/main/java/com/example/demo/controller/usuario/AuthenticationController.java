package com.example.demo.controller.usuario;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.usuario.*;
import com.example.demo.service.usuario.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints relacionados a autenticação do usuário")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }


    @PostMapping("/signin")
    @EurekaApiOperation(
            summary = "Método para fazer signin na aplicação",
            description = "Autentica o usuário e devolve para ele um token e um refresh token"
    )
    public ResponseEntity<ApiReturn<JwtAuthenticationResponse>> signin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de signin",
                    required = true
            )
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.signin(request)));
    }

    @EurekaApiOperation(
            summary = "Método para fazer atualizar o token de autenticação na aplicação",
            description = "Atualiza o token a partir do refresh token e devolve"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiReturn<JwtAuthenticationResponse>> signin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de refresh token",
                    required = true
            )
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(ApiReturn.of(service.refreshToken(request)));
    }

    @PostMapping("/forgot-password")
    @EurekaApiOperation(
            summary = "Recuperar senha",
            description = "Envia um token temporário por email ou define a nova senha informada"
    )
    public ResponseEntity<ApiReturn<String>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {
        service.forgotPassword(request);
        return ResponseEntity.ok(ApiReturn.of("Senha redefinida com sucesso."));
    }

    @PostMapping("/alterar-senha")
    @EurekaApiOperation(
            summary = "Trocar senha",
            description = "Troca a senha de um usuário persistido."
    )
    public ResponseEntity<ApiReturn<String>> changePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Corpo da requisição com os dados de troca de senha",
                    required = true
            )
            @RequestBody @Valid TrocarSenhaRequest request
    ) {
        service.alterarSenha(request);
        return ResponseEntity.ok(ApiReturn.of("Senha alterada com sucesso."));
    }
}
