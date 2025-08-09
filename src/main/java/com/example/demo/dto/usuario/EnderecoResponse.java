package com.example.demo.dto.usuario;

public record EnderecoResponse(
        String cep,
        String endereco,
        String numero,
        String cidade,
        String estado
) {
}
