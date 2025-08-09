package com.example.demo.dto.instituicao;

public record EscolaEnderecoResponse(
        String cep,
        String endereco,
        String numero,
        String bairro,
        String complemento,
        String cidade,
        String estado
) {
}
