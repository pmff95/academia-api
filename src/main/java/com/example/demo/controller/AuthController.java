package com.example.demo.controller;

import com.example.demo.common.security.JwtService;
import com.example.demo.service.UsuarioService;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.common.response.ErrorType;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ReenviarSenhaDTO;
import com.example.demo.dto.AlterarSenhaDTO;
import com.example.demo.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiReturn<?>> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                Usuario usuario = usuarioService.buscarPorLogin(request.getUsername());
                boolean primeiroAcesso = usuario != null && usuario.getUltimoAcesso() == null;
                if (usuario != null) {
                    usuarioService.registrarUltimoAcesso(usuario);
                }
                Map<String, Object> claims = Map.of(
                        "nome", usuario.getNome(),
                        "email", usuario.getEmail(),
                        "perfil", usuario.getPerfil().name()
                );
                return ResponseEntity.ok(ApiReturn.of(new AuthResponse(
                        jwtService.generateToken(claims, userDetails),
                        primeiroAcesso,
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getPerfil().name())));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(ApiReturn.of(ErrorType.UNAUTHORIZED, ErrorType.UNAUTHORIZED.getCode(), "Credenciais inválidas ou usuário inativo", e));
        }
        return ResponseEntity.status(401)
                .body(ApiReturn.of(ErrorType.UNAUTHORIZED, ErrorType.UNAUTHORIZED.getCode(), "Credenciais inválidas", null));
    }

    @PostMapping("/reenviar-senha")
    public ResponseEntity<ApiReturn<String>> reenviarSenha(@RequestBody ReenviarSenhaDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(usuarioService.reenviarSenha(dto.getLogin())));
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<ApiReturn<String>> alterarSenha(@RequestBody AlterarSenhaDTO dto) {
        return ResponseEntity.ok(ApiReturn.of(usuarioService.alterarSenha(dto.getLogin(), dto.getSenhaAtual(), dto.getNovaSenha())));
    }
}
