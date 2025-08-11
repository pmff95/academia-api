package com.example.demo.common.security;

import com.example.demo.domain.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Representa o usuário autenticado no sistema.
 */
public class UsuarioLogado implements UserDetails {

    private final UUID uuid;
    private final UUID academiaUuid;
    private final Perfil perfil;

    public UsuarioLogado(UUID uuid, UUID academiaUuid, Perfil perfil) {
        this.uuid = uuid;
        this.academiaUuid = academiaUuid;
        this.perfil = perfil;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getAcademiaUuid() {
        return academiaUuid != null ? academiaUuid : null;
    }

    /**
     * Verifica se o usuário possui o perfil informado.
     */
    public boolean possuiPerfil(Perfil p) {
        return this.perfil == p;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
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

