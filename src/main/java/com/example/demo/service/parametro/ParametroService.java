package com.example.demo.service.parametro;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.parametro.Parametro;
import com.example.demo.domain.model.parametro.ParametroEscola;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.dto.parametro.ParametroDTO;
import com.example.demo.dto.parametro.ParametroEscolaDTO;
import com.example.demo.dto.parametro.SimplesParametroValorView;
import com.example.demo.repository.specification.ParametroSpecification;
import com.example.demo.dto.parametro.ParamsView;
import com.example.demo.repository.instituicao.EscolaRepository;
import com.example.demo.repository.parametro.ParametroEscolaRepository;
import com.example.demo.repository.parametro.ParametroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParametroService {

    private final ParametroRepository parametroRepository;
    private final ParametroEscolaRepository parametroEscolaRepository;
    private final EscolaRepository escolaRepository;

    @Transactional
    public UUID criarOuEditarParametroGlobal(ParametroDTO dto) {
        String chave = normalize(dto.chave());
        Parametro parametro = parametroRepository.findByChaveAndStatus(chave, Status.ATIVO)
                .orElseGet(Parametro::new);

        parametro.setChave(chave);
        parametro.setDescricao(dto.descricao());
        parametro.setValorDefault(dto.valorDefault());
        parametro.setTipoValor(dto.tipoValor());
        parametro.setNecessarioConfirmacao(Boolean.TRUE.equals(dto.necessarioConfirmacao()));
        parametro.setMensagemConfirmacaoAtivo(dto.mensagemConfirmacaoAtivo());
        parametro.setMensagemConfirmacaoInativo(dto.mensagemConfirmacaoInativo());
        parametro.setMensagemConfirmacaoAlteracao(dto.mensagemConfirmacaoAlteracao());
        parametro.setTipo(dto.tipo());

        return parametroRepository.save(parametro).getUuid();
    }

    @Transactional
    public UUID criarOuEditarParametroEscola(ParametroEscolaDTO dto) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        Escola escola = escolaRepository.findByUuid(usuarioLogado.getEscola().getUuid())
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));

        Parametro parametro = parametroRepository.findByChaveAndStatus(normalize(dto.chaveParametro()), Status.ATIVO)
                .orElseThrow(() -> EurekaException.ofNotFound("Parâmetro não encontrado."));

        ParametroEscola parametroEscola = parametroEscolaRepository
                .findByEscola_UuidAndParametro_Chave(usuarioLogado.getEscola().getUuid(), parametro.getChave())
                .orElseGet(() -> {
                    ParametroEscola pe = new ParametroEscola();
                    pe.setEscola(escola);
                    pe.setParametro(parametro);
                    return pe;
                });

        parametroEscola.setValorCustomizado(dto.valorCustomizado());

        return parametroEscolaRepository.save(parametroEscola).getUuid();
    }

    @Transactional
    public UUID criarOuEditarParametroEscola(UUID escolaUuid, ParametroEscolaDTO dto) {
        Escola escola = escolaRepository.findByUuid(escolaUuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Escola não encontrada."));

        Parametro parametro = parametroRepository.findByChaveAndStatus(normalize(dto.chaveParametro()), Status.ATIVO)
                .orElseThrow(() -> EurekaException.ofNotFound("Parâmetro não encontrado."));

        ParametroEscola parametroEscola = parametroEscolaRepository
                .findByEscola_UuidAndParametro_Chave(escolaUuid, parametro.getChave())
                .orElseGet(() -> {
                    ParametroEscola pe = new ParametroEscola();
                    pe.setEscola(escola);
                    pe.setParametro(parametro);
                    return pe;
                });

        parametroEscola.setValorCustomizado(dto.valorCustomizado());

        return parametroEscolaRepository.save(parametroEscola).getUuid();
    }

    @Transactional(readOnly = true)
    public List<ParamsView> listarParametros(ParametroSpecification specification) {
        List<Parametro> parametros = parametroRepository.findAll(specification);
        List<ParamsView> result = new ArrayList<>();
        for (Parametro p : parametros) {
            result.add(new ParamsView(
                    p.getChave(),
                    p.getDescricao(),
                    p.getTipo(),
                    p.getTipoValor(),
                    p.getValorDefault(),
                    null,
                    p.getNecessarioConfirmacao(),
                    p.getMensagemConfirmacaoAtivo(),
                    p.getMensagemConfirmacaoInativo(),
                    p.getMensagemConfirmacaoAlteracao()
            ));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ParamsView> listarParametrosPorEscola() {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        List<Parametro> parametros = parametroRepository.findByStatus(Status.ATIVO);
        List<ParametroEscola> personalizados = parametroEscolaRepository.findByEscola_Uuid(usuarioLogado.getEscola().getUuid());
        return getParamsViews(personalizados, parametros);
    }

    @Transactional(readOnly = true)
    public List<ParamsView> listarParametrosPorEscolaPorTipo(UUID escolaUuid, TipoParametro tipo) {
        List<Parametro> parametros = parametroRepository.findByStatusAndTipo(Status.ATIVO, tipo);
        List<ParametroEscola> personalizados = parametroEscolaRepository.findByParametro_TipoAndEscola_Uuid(tipo, escolaUuid);
        return getParamsViews(personalizados, parametros);
    }

    @Transactional(readOnly = true)
    public SimplesParametroValorView retornaValorParametro(String chave, boolean throwError) {
        Parametro parametro = parametroRepository
                .findByChaveAndStatus(chave, Status.ATIVO)
                .orElse(null);

        if (parametro == null) {
            if (throwError) {
                EurekaException.ofNotFound("Parâmetro" + chave + "não encontrado.");
            } else {
                return new SimplesParametroValorView(chave, null);
            }
        }

        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();

        if(usuarioLogado == null || usuarioLogado.getEscola() == null) {
            return new SimplesParametroValorView(chave, parametro.getValorDefault());
        } else {
            ParametroEscola parametroEscola = parametroEscolaRepository.findByEscola_UuidAndStatusAndParametro_Chave(
                    usuarioLogado.getEscola().getUuid(),
                    Status.ATIVO,
                    chave
            ).orElse(new ParametroEscola());

            return new SimplesParametroValorView(
                    chave,
                    parametroEscola.getValorCustomizado() == null
                            ? parametro.getValorDefault()
                            : parametroEscola.getValorCustomizado()
                    );
        }
    }

    @Transactional
    public void toggleParametroEscolaStatus(String chave) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogado();
        ParametroEscola parametroEscola = parametroEscolaRepository
                .findByEscola_UuidAndParametro_Chave(usuarioLogado.getEscola().getUuid(), normalize(chave))
                .orElseThrow(() -> EurekaException.ofNotFound("Parâmetro da escola não encontrado."));

        Status novoStatus = parametroEscola.getStatus() == Status.ATIVO ? Status.INATIVO : Status.ATIVO;
        parametroEscola.setStatus(novoStatus);
        parametroEscolaRepository.save(parametroEscola);
    }

    @Transactional
    public void toggleParametroStatus(String chave) {
        String normalized = normalize(chave);
        Parametro parametro = parametroRepository.findByChaveAndStatus(normalized, Status.ATIVO)
                .orElseThrow(() -> EurekaException.ofNotFound("Parâmetro não encontrado."));

        Status novoStatus = parametro.getStatus() == Status.ATIVO ? Status.INATIVO : Status.ATIVO;

        if (parametro.getStatus() != novoStatus) {
            parametro.setStatus(novoStatus);
            parametroRepository.save(parametro);

            List<ParametroEscola> relacionados = parametroEscolaRepository.findByParametro_Chave(parametro.getChave());
            for (ParametroEscola pe : relacionados) {
                pe.setStatus(novoStatus);
            }
            parametroEscolaRepository.saveAll(relacionados);
        }
    }

    private String normalize(String chave) {
        if (chave == null) {
            return null;
        }
        String normalized = chave.trim().toLowerCase();
        normalized = normalized.replaceAll("[^a-z0-9]+", "_");
        normalized = normalized.replaceAll("_+", "_");
        if (normalized.startsWith("_")) {
            normalized = normalized.substring(1);
        }
        if (normalized.endsWith("_")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static List<ParamsView> getParamsViews(List<ParametroEscola> personalizados, List<Parametro> parametros) {
        Map<Long, ParametroEscola> map = personalizados.stream()
                .collect(Collectors.toMap(pe -> pe.getParametro().getId(), pe -> pe));

        List<ParamsView> result = new ArrayList<>();
        for (Parametro p : parametros) {
            ParametroEscola pe = map.get(p.getId());
            String valorCustom = pe != null ? pe.getValorCustomizado() : null;

            result.add(new ParamsView(
                    p.getChave(),
                    p.getDescricao(),
                    p.getTipo(),
                    p.getTipoValor(),
                    p.getValorDefault(),
                    valorCustom,
                    p.getNecessarioConfirmacao(),
                    p.getMensagemConfirmacaoAtivo(),
                    p.getMensagemConfirmacaoInativo(),
                    p.getMensagemConfirmacaoAlteracao()
            ));
        }
        return result;
    }
}
