package com.example.demo.dto;

public class AuthResponse {
    private String token;
    private boolean primeiroAcesso;

    public AuthResponse() {
    }

    public AuthResponse(String token, boolean primeiroAcesso) {
        this.token = token;
        this.primeiroAcesso = primeiroAcesso;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }
}
