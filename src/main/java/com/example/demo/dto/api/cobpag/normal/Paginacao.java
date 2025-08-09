package com.example.demo.dto.api.cobpag.normal;

public record Paginacao(
        int paginaAtual,
        int itensPorPagina,
        int quantidadeDePaginas,
        int quantidadeTotalDeItens
) {
}
