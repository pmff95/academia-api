package com.example.demo.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Retorna os detalhes do usuário autenticado.
     */
    public static UsuarioLogado getUsuarioLogadoDetalhes() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UsuarioLogado usuario) {
            return usuario;
        }

        return null;
    }

}
