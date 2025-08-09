package com.example.demo.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Object getUsuarioLogado() {
//    public static UsuarioLogado getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null)
            return null;

        Object principal = authentication.getPrincipal();

        if (principal instanceof Object usuario)
//        if (principal instanceof UsuarioLogado usuario)
            return usuario;

        return null;
    }

}
