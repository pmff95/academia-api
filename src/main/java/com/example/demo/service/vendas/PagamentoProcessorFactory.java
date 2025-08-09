package com.example.demo.service.vendas;

import com.example.demo.domain.enums.TipoPagamento;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagamentoProcessorFactory {

    private final Map<TipoPagamento, PagamentoProcessor> processorsPorTipo;

    public PagamentoProcessorFactory(List<PagamentoProcessor> processors) {
        this.processorsPorTipo = new HashMap<>();
        for (PagamentoProcessor processor : processors) {
            this.processorsPorTipo.put(processor.getTipoSuportado(), processor);
        }
    }

    public PagamentoProcessor getProcessor(TipoPagamento tipoPagamento) {
        return processorsPorTipo.get(tipoPagamento);
    }
}

