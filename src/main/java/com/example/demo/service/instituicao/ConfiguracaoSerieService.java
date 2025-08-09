package com.example.demo.service.instituicao;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.instituicao.ConfiguracaoSerie;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.dto.instituicao.ConfiguracaoSerieItemRequest;
import com.example.demo.dto.instituicao.ConfiguracaoSerieResponse;
import com.example.demo.dto.instituicao.ConfiguracaoSerieSerieRequest;
import com.example.demo.repository.instituicao.ConfiguracaoSerieRepository;
import com.example.demo.repository.instituicao.EscolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfiguracaoSerieService {

    private final ConfiguracaoSerieRepository repository;
    private final EscolaRepository escolaRepository;

    @Transactional
    public String salvarOuAtualizar(List<ConfiguracaoSerieItemRequest> request) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        if (usuario == null || usuario.getEscola() == null) {
            throw EurekaException.ofNotFound("Escola do usuário não encontrada.");
        }
        UUID escolaUuid = usuario.getEscola().getUuid();
        Escola escola = escolaRepository.findByUuid(escolaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));

        boolean existe = repository.existsByEscola_Uuid(escolaUuid);

        for (ConfiguracaoSerieItemRequest item : request) {
            for (ConfiguracaoSerieSerieRequest serieConfig : item.series()) {
                ConfiguracaoSerie config = repository
                        .findByEscola_UuidAndSerie(escolaUuid, serieConfig.serie())
                        .orElse(new ConfiguracaoSerie());

                config.setEscola(escola);
                config.setSerie(serieConfig.serie());
                config.setTipoPeriodo(item.tipoPeriodo());
                config.setQuantidadeAvaliacoes(serieConfig.quantidadeAvaliacoes());
                config.setMedia(serieConfig.media());
                config.setStatus(serieConfig.status());

                repository.save(config);
            }
        }
        return existe ? "Configurações atualizadas com sucesso." : "Configurações criadas com sucesso.";
    }

//    @Transactional
//    public String salvar(ConfiguracaoSerieRequest request) {
//        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
//        if (usuario == null || usuario.getEscola() == null) {
//            throw EurekaException.ofNotFound("Escola do usuário não encontrada.");
//        }
//        UUID escolaUuid = usuario.getEscola().getUuid();
//        Escola escola = escolaRepository.findByUuid(escolaUuid)
//                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));
//
//        for (ConfiguracaoSerieItemRequest item : request.itens()) {
//            for (Serie serie : item.series()) {
//                ConfiguracaoSerie config = repository
//                        .findByEscola_UuidAndSerie(escolaUuid, serie)
//                        .orElse(new ConfiguracaoSerie());
//
//                config.setEscola(escola);
//                config.setSerie(serie);
//                config.setTipoPeriodo(item.tipoPeriodo());
//
//                repository.save(config);
//            }
//        }
//        return "Configurações salvas com sucesso.";
//    }

    public List<ConfiguracaoSerieResponse> listar() {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        if (usuario == null || usuario.getEscola() == null) {
            throw EurekaException.ofNotFound("Escola do usuário não encontrada.");
        }
        UUID escolaUuid = usuario.getEscola().getUuid();

        return repository.findByEscola_Uuid(escolaUuid)
                .stream()
                .map(c -> new ConfiguracaoSerieResponse(
                        c.getSerie(),
                        c.getTipoPeriodo(),
                        c.getQuantidadeAvaliacoes(),
                        c.getMedia(),
                        c.getStatus()))
                .toList();
    }
}
