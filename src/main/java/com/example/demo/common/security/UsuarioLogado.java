package com.example.demo.common.security;

import com.example.demo.domain.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Representa o usuário autenticado no sistema.
 */
public class UsuarioLogado implements UserDetails {

    private final UUID uuid;
    private final Perfil perfil;

    public UsuarioLogado(UUID uuid, Perfil perfil) {
        this.uuid = uuid;
        this.perfil = perfil;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Verifica se o usuário possui o perfil informado.
     */
    public boolean possuiPerfil(Perfil p) {
        return this.perfil == p;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return uuid.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

