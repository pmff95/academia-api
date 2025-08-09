package com.example.demo.service.vendas;

import com.example.demo.domain.enums.TipoPagamento;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.domain.model.vendas.PagamentoItem;

public interface PagamentoProcessor {
    void processaPagamento(Usuario usuarioPagante, PagamentoItem pagamentoItem);

    TipoPagamento getTipoSuportado();
}
