package com.example.demo.controller;

import com.example.demo.common.security.JwtService;
import com.example.demo.service.UsuarioService;
import com.example.demo.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
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
                String token = jwtService.generateToken(userDetails);
                AuthResponse resp = new AuthResponse(token, primeiroAcesso);
                return ResponseEntity.ok(new ApiResponse<>(true, "Autenticado", resp, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(false, "Credenciais inválidas ou usuário inativo", null, null));
        }
        return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "Credenciais inválidas", null, null));
    }

    @PostMapping("/reenviar-senha")
    public ResponseEntity<ApiResponse<String>> reenviarSenha(@RequestBody ReenviarSenhaDTO dto) {
        String msg = usuarioService.reenviarSenha(dto.getLogin());
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<ApiResponse<String>> alterarSenha(@RequestBody AlterarSenhaDTO dto) {
        String msg = usuarioService.alterarSenha(dto.getLogin(), dto.getSenhaAtual(), dto.getNovaSenha());
        return ResponseEntity.ok(new ApiResponse<>(true, msg, null, null));
    }
}
