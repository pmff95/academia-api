package com.example.demo.service.ppp;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.instituicao.EscolaEndereco;
import com.example.demo.domain.model.ppp.Ppp;
import com.example.demo.dto.ppp.PppRequest;
import com.example.demo.dto.ppp.PppView;
import com.example.demo.repository.instituicao.EscolaEnderecoRepository;
import com.example.demo.repository.instituicao.EscolaRepository;
import com.example.demo.repository.ppp.PppRepository;
import com.example.demo.service.common.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PppService {

    private final PppRepository repository;
    private final EscolaRepository escolaRepository;
    private final EscolaEnderecoRepository enderecoRepository;
    private final PdfGeneratorService pdfGenerator;

    @Transactional
    public UUID create(PppRequest request) {
        Escola escola = escolaRepository.findByUuid(request.escolaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));

        Ppp ppp = new Ppp();
        ppp.setEscola(escola);
        ppp.setInicioVigencia(request.inicioVigencia());
        ppp.setFimVigencia(request.fimVigencia());
        ppp.setDataAprovacao(request.dataAprovacao());
        ppp.setResponsavel(request.responsavel());
        ppp.setConteudo(request.conteudo());
        ppp.setStatus(Status.ATIVO);

        repository.save(ppp);
        return ppp.getUuid();
    }

    public PppView findViewByUuid(UUID uuid) {
        Ppp ppp = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("PPP não encontrado."));
        return new PppView(
                ppp.getUuid(),
                ppp.getEscola().getNome(),
                ppp.getInicioVigencia(),
                ppp.getFimVigencia(),
                ppp.getDataAprovacao(),
                ppp.getResponsavel()
        );
    }

    public byte[] gerarPdf(UUID uuid) throws IOException {
        Ppp ppp = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("PPP não encontrado."));

        Escola escola = ppp.getEscola();
        EscolaEndereco endereco = enderecoRepository.findByEscola_Uuid(escola.getUuid())
                .orElse(null);
        String endStr = "";
        if (endereco != null) {
            endStr = String.format("%s, %s - %s, %s/%s CEP %s", endereco.getEndereco(),
                    endereco.getNumero(), endereco.getBairro(), endereco.getCidade(),
                    endereco.getEstado(), endereco.getCep());
        }

        Map<String, Object> dados = Map.of(
                "ESCOLA_NOME", escola.getNome(),
                "ESCOLA_CNPJ", escola.getCnpj(),
                "ESCOLA_ENDERECO", endStr,
                "INICIO_VIGENCIA", ppp.getInicioVigencia(),
                "FIM_VIGENCIA", ppp.getFimVigencia(),
                "DATA_APROVACAO", ppp.getDataAprovacao(),
                "RESPONSAVEL", ppp.getResponsavel(),
                "CONTEUDO", ppp.getConteudo()
        );
        String template = pdfGenerator.carregarTemplate("ppp/ppp_template.html", true);
        return pdfGenerator.gerarPdf(template, dados);
    }
}
