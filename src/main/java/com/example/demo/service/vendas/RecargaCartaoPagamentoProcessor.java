package com.example.demo.service.vendas;

import com.example.demo.domain.enums.TipoPagamento;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.domain.model.vendas.PagamentoItem;
import com.example.demo.service.carteira.CarteiraService;
import org.springframework.stereotype.Service;

@Service
public class RecargaCartaoPagamentoProcessor implements PagamentoProcessor {

    private final CarteiraService carteiraService;

    public RecargaCartaoPagamentoProcessor(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @Override
    public void processaPagamento(Usuario usuarioPagante, PagamentoItem pagamentoItem) {
        carteiraService.realizarRecarga(
                pagamentoItem.getAluno().getUuid(),
                pagamentoItem.getValorIndividual(),
                usuarioPagante
        );
    }

    @Override
    public TipoPagamento getTipoSuportado() {
        return TipoPagamento.RECARGA_CARTAO;
    }
}

