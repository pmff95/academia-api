package com.example.demo.dto;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private boolean primeiroAcesso;
    private String nome;
    private String email;
    private String perfil;

    public AuthResponse() {
    }

    public AuthResponse(String token, String refreshToken, boolean primeiroAcesso, String nome, String email, String perfil) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.primeiroAcesso = primeiroAcesso;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
