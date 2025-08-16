package com.example.demo.service;

import com.example.demo.dto.TreinoSessaoDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.TreinoSessaoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.TreinoSessaoRepository;
import com.example.demo.repository.FichaTreinoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TreinoSessaoService {
    private final TreinoSessaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FichaTreinoRepository fichaTreinoRepository;
    private final TreinoSessaoMapper mapper;

    public TreinoSessaoService(TreinoSessaoRepository repository,
                               AlunoRepository alunoRepository,
                               UsuarioRepository usuarioRepository,
                               FichaTreinoRepository fichaTreinoRepository,
                               TreinoSessaoMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public String registrarSessao(UUID alunoUuid, TreinoSessaoDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        FichaTreinoExercicio exercicio = fichaTreinoRepository.findByAluno_Uuid(alunoUuid).stream()
                .flatMap(f -> f.getCategorias().stream())
                .flatMap(c -> c.getExercicios().stream())
                .filter(e -> e.getUuid().equals(dto.getExercicioUuid()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Exercício não encontrado na ficha do aluno"));

        TreinoSessao sessao = mapper.toEntity(dto);
        sessao.setAluno(aluno);
        sessao.setExercicio(exercicio);

        repository.save(sessao);
        return "Sessão registrada com sucesso";
    }

    public List<TreinoSessaoDTO> listarSessoes(UUID alunoUuid) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        validarMesmaAcademia(aluno);
        return repository.findByAlunoUuid(alunoUuid).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
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
}
