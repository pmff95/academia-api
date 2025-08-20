package com.example.demo.service;

import com.example.demo.dto.AlunoMedidaDTO;
import com.example.demo.dto.AlunoMedidaHistoricoDTO;
import com.example.demo.dto.AlunoMedidaResultadoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.AlunoMedida;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoMedidaMapper;
import com.example.demo.repository.AlunoMedidaRepository;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoMedidaService {

    private final AlunoMedidaRepository repository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlunoMedidaMapper mapper;

    public AlunoMedidaService(AlunoMedidaRepository repository,
                              AlunoRepository alunoRepository,
                              UsuarioRepository usuarioRepository,
                              AlunoMedidaMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AlunoMedidaResultadoDTO adicionarMedida(UUID alunoUuid, AlunoMedidaDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        AlunoMedida ultima = repository.findTopByAlunoUuidOrderByDataRegistroDesc(alunoUuid).orElse(null);
        AlunoMedida nova = mapper.toEntity(dto);
        if (ultima != null) {
            if (nova.getPeso() == null) nova.setPeso(ultima.getPeso());
            if (nova.getAltura() == null) nova.setAltura(ultima.getAltura());
            if (nova.getBracoEsquerdo() == null) nova.setBracoEsquerdo(ultima.getBracoEsquerdo());
            if (nova.getBracoDireito() == null) nova.setBracoDireito(ultima.getBracoDireito());
            if (nova.getPeito() == null) nova.setPeito(ultima.getPeito());
            if (nova.getAbdomen() == null) nova.setAbdomen(ultima.getAbdomen());
            if (nova.getCintura() == null) nova.setCintura(ultima.getCintura());
            if (nova.getQuadril() == null) nova.setQuadril(ultima.getQuadril());
            if (nova.getCoxaEsquerda() == null) nova.setCoxaEsquerda(ultima.getCoxaEsquerda());
            if (nova.getCoxaDireita() == null) nova.setCoxaDireita(ultima.getCoxaDireita());
            if (nova.getPanturrilhaEsquerda() == null) nova.setPanturrilhaEsquerda(ultima.getPanturrilhaEsquerda());
            if (nova.getPanturrilhaDireita() == null) nova.setPanturrilhaDireita(ultima.getPanturrilhaDireita());
        }
        nova.setAluno(aluno);
        repository.save(nova);

        // manter apenas 6 meses
        repository.deleteByAlunoUuidAndDataRegistroBefore(alunoUuid, LocalDateTime.now().minusMonths(6));

        Map<String, BigDecimal> variacoes = new HashMap<>();
        if (ultima != null) {
            variacoes = calcularVariacoes(ultima, nova);
        }

        AlunoMedidaResultadoDTO resultado = new AlunoMedidaResultadoDTO();
        resultado.setAtual(mapper.toDto(nova));
        resultado.setVariacoes(variacoes);
        return resultado;
    }

    public AlunoMedidaDTO buscarUltima(UUID alunoUuid) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        return repository.findTopByAlunoUuidOrderByDataRegistroDesc(alunoUuid)
                .map(mapper::toDto)
                .orElse(null);
    }

    public AlunoMedidaHistoricoDTO listarHistorico(UUID alunoUuid) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        List<AlunoMedida> medidas = repository.findTop10ByAlunoUuidOrderByDataRegistroDesc(alunoUuid);
        List<AlunoMedidaDTO> dtos = medidas.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        Map<String, BigDecimal> medias = new HashMap<>();
        if (medidas.size() > 1) {
            Map<String, BigDecimal> somas = new HashMap<>();
            List<AlunoMedida> ordenadas = new ArrayList<>(medidas);
            Collections.reverse(ordenadas); // oldest to newest
            for (int i = 1; i < ordenadas.size(); i++) {
                Map<String, BigDecimal> variacoes = calcularVariacoes(ordenadas.get(i - 1), ordenadas.get(i));
                variacoes.forEach((k, v) -> somas.merge(k, v, BigDecimal::add));
            }
            int count = ordenadas.size() - 1;
            somas.forEach((k, v) -> medias.put(k, v.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP)));
        }

        AlunoMedidaHistoricoDTO historico = new AlunoMedidaHistoricoDTO();
        historico.setMedidas(dtos);
        historico.setMediaVariacoes(medias);
        return historico;
    }

    private void validarMesmaAcademia(Aluno aluno) {
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuarioLogado != null && usuarioLogado.possuiPerfil(Perfil.MASTER);

        if (usuarioLogado != null && !isMaster) {
            Usuario usuario = usuarioRepository.findByUuid(usuarioLogado.getUuid())
                    .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));

            if (usuario.getAcademia() == null || aluno.getAcademia() == null
                    || !usuario.getAcademia().getUuid().equals(aluno.getAcademia().getUuid())) {
                throw new ApiException("Acesso negado a aluno de outra academia");
            }
        }
    }

    private Map<String, BigDecimal> calcularVariacoes(AlunoMedida antiga, AlunoMedida atual) {
        Map<String, BigDecimal> map = new HashMap<>();

        adicionarVariacao(map, "peso", antiga.getPeso(), atual.getPeso());
        adicionarVariacao(map, "altura", antiga.getAltura(), atual.getAltura());
        adicionarVariacao(map, "bracoEsquerdo", antiga.getBracoEsquerdo(), atual.getBracoEsquerdo());
        adicionarVariacao(map, "bracoDireito", antiga.getBracoDireito(), atual.getBracoDireito());
        adicionarVariacao(map, "peito", antiga.getPeito(), atual.getPeito());
        adicionarVariacao(map, "abdomen", antiga.getAbdomen(), atual.getAbdomen());
        adicionarVariacao(map, "cintura", antiga.getCintura(), atual.getCintura());
        adicionarVariacao(map, "quadril", antiga.getQuadril(), atual.getQuadril());
        adicionarVariacao(map, "coxaEsquerda", antiga.getCoxaEsquerda(), atual.getCoxaEsquerda());
        adicionarVariacao(map, "coxaDireita", antiga.getCoxaDireita(), atual.getCoxaDireita());
        adicionarVariacao(map, "panturrilhaEsquerda", antiga.getPanturrilhaEsquerda(), atual.getPanturrilhaEsquerda());
        adicionarVariacao(map, "panturrilhaDireita", antiga.getPanturrilhaDireita(), atual.getPanturrilhaDireita());

        return map;
    }

    private void adicionarVariacao(Map<String, BigDecimal> map, String chave, BigDecimal antigo, BigDecimal atual) {
        if (antigo != null && atual != null && antigo.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal variacao = atual.subtract(antigo)
                    .divide(antigo, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            map.put(chave, variacao);
        }
    }
}
