package com.example.demo.common.security;

import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.usuario.Usuario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UsuarioLogado implements UserDetails {

    private final Usuario usuario;

    public UsuarioLogado(Usuario usuario) {
        this.usuario = usuario;
    }

    public UUID getUuid() {
        return this.usuario.getUuid();
    }

    public Long getId() {
        return this.usuario.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Perfil perfil = this.usuario.getPerfil();

        if (perfil == Perfil.ADMIN || perfil == Perfil.RESPONSAVEL_CONTRATUAL)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_FUNCIONARIO"),
                    new SimpleGrantedAuthority("ROLE_PDV"));

        if (perfil == Perfil.FUNCIONARIO)
            return List.of(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"),
                    new SimpleGrantedAuthority("ROLE_PDV"));

        if (perfil == Perfil.RESPONSAVEL)
            return List.of(new SimpleGrantedAuthority("ROLE_RESPONSAVEL"),
                    new SimpleGrantedAuthority("ROLE_ALUNO"));


        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    public Perfil getPerfil() {
        return this.usuario.getPerfil();
    }

    public Status getStatus() {
        return this.usuario.getStatus();
    }

    public String getName() {
        return this.usuario.getNome();
    }
    public Boolean getPrimeiroAcesso() {
        return this.usuario.getPrimeiroAcesso();
    }

    public String getMatricula() { return this.usuario.getMatricula(); }

    public String getMatriculaManual() { return this.usuario.getMatriculaManual(); }

    public String getEmail() { return this.usuario.getEmail(); }

    public String getNickname() { return this.usuario.getNickname(); }

    public String getFoto() { return this.usuario.getFoto(); }

    public String getCpf() { return this.usuario.getCpf(); }

    public String getTelefone() { return this.usuario.getTelefone(); }

    public LocalDate getDataNascimento() { return this.usuario.getDataNascimento(); }

    public GrupoSanguineo getGrupoSanguineo() { return this.usuario.getGrupoSanguineo(); }

    @Override
    public String getPassword() {
        return this.usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return this.usuario.getMatricula();
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
        return StringUtils.equals(
                this.usuario.getStatus().name(),
                Status.ATIVO.name());
    }

    public boolean possuiPerfil(Perfil perfil) {
        String authorityToCheck = "ROLE_" + perfil.name().toUpperCase();
        return getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(authorityToCheck));
    }

    public Escola getEscola() {

        if (possuiPerfil(Perfil.MASTER))
            return null;

        return this.usuario.getEscola();
    }

    public boolean isPrimeiroAcesso() {
        return this.usuario.getPrimeiroAcesso();
    }

}
