package com.example.demo.service.instituicao;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.instituicao.EscolaFinanceiro;
import com.example.demo.dto.instituicao.EscolaFinanceiroRequest;
import com.example.demo.repository.instituicao.EscolaFinanceiroRepository;
import com.example.demo.repository.instituicao.EscolaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EscolaFinanceiroService {

    private final EscolaFinanceiroRepository escolaFinanceiroRepository;
    private final EscolaRepository escolaRepository;

    public EscolaFinanceiroService(EscolaFinanceiroRepository escolaFinanceiroRepository, EscolaRepository escolaRepository) {
        this.escolaFinanceiroRepository = escolaFinanceiroRepository;
        this.escolaRepository = escolaRepository;
    }

    public void save(UUID escolaId, EscolaFinanceiroRequest request) {

        this.validateRequest(request);

        Escola escola = escolaRepository.findByUuid(escolaId)
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada com ID: " + escolaId));

        EscolaFinanceiro escolaFinanceiro = this.escolaFinanceiroRepository.findByEscola_Uuid(escolaId)
                .orElseGet(() -> {
                    EscolaFinanceiro novoFinanceiro = new EscolaFinanceiro();
                    novoFinanceiro.setEscola(escola);
                    return novoFinanceiro;
                });

        escolaFinanceiro.setDiaPagamento(request.diaPagamento());
        escolaFinanceiro.setDiaRecebimento(request.diaRecebimento());

        escolaFinanceiroRepository.save(escolaFinanceiro);
    }

    public EscolaFinanceiro findByEscolaId(UUID escolaId) {
        return escolaFinanceiroRepository.findByEscola_Uuid(escolaId)
                .orElseThrow(() -> EurekaException.ofNotFound("Não há dados financeiros para a escola: " + escolaId));
    }

    private void validateRequest(EscolaFinanceiroRequest request) {

        Integer diaPagamento = request.diaPagamento();
        if (diaPagamento == null || diaPagamento < 1 || diaPagamento > 31) {
            throw EurekaException.ofValidation("O dia de pagamento deve estar entre 1 e 31.");
        }

        Integer diaRecebimento = request.diaRecebimento();
        if (diaRecebimento == null || diaRecebimento < 1 || diaRecebimento > 31) {
            throw EurekaException.ofValidation("O dia de recebimento deve estar entre 1 e 31.");
        }
    }
}
