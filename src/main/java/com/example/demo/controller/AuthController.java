package com.example.demo.controller;

import com.example.demo.common.security.JwtService;
import com.example.demo.service.UsuarioService;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
            usuarioService.registrarUltimoAcesso(request.getUsername());
            return ResponseEntity.ok(new ApiResponse<>(true, "Autenticado", token, null));
        }
        return ResponseEntity.status(401)
                .body(new ApiResponse<>(false, "Credenciais inválidas", null, null));
    }
}
