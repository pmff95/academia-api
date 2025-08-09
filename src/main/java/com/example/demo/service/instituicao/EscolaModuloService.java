package com.example.demo.service.instituicao;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.instituicao.EscolaModulo;
import com.example.demo.dto.instituicao.AtivarModuloRequest;
import com.example.demo.repository.instituicao.EscolaModuloRepository;
import com.example.demo.repository.instituicao.EscolaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EscolaModuloService {


    private final EscolaModuloRepository escolaModuloRepository;
    private final EscolaRepository escolaRepository;

    public EscolaModuloService(EscolaModuloRepository escolaModuloRepository,
                               EscolaRepository escolaRepository) {
        this.escolaModuloRepository = escolaModuloRepository;
        this.escolaRepository = escolaRepository;
    }

    public List<EscolaModulo> listarPorEscola(UUID escolaUuid) {
        return escolaModuloRepository.findByEscolaUuid(escolaUuid);
    }

    @Transactional
    public List<EscolaModulo> ativarModulo(AtivarModuloRequest request) {
        Escola escola = escolaRepository.findByUuid(request.escolaUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada"));

        List<EscolaModulo> existentes = escolaModuloRepository.findByEscola_Id(escola.getId());
        if (!existentes.isEmpty()) {
            escolaModuloRepository.deleteAll(existentes);
        }

        List<EscolaModulo> novos = new java.util.ArrayList<>();
        for (var dto : request.modulos()) {
            EscolaModulo relacao = new EscolaModulo();
            relacao.setEscola(new Escola(escola.getId()));
            relacao.setModulo(dto.nomeModulo());
            relacao.setAtivo(true);
            relacao.setDataAtivacao(LocalDate.now());
            relacao.setDataExpiracao(dto.dataExpiracao());

            novos.add(relacao);
        }
        return escolaModuloRepository.saveAll(novos);
    }

    public void desativarModulo(List<Long> ids) {
        List<EscolaModulo> modulos = escolaModuloRepository.findAllById(ids);
        if (modulos.isEmpty()) {
            throw EurekaException.ofException("Relacionamento não encontrado");
        }
        modulos.forEach(m -> m.setAtivo(false));
        escolaModuloRepository.saveAll(modulos);
    }
}
