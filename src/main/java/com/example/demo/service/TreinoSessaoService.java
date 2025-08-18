package com.example.demo.service;

import com.example.demo.dto.TreinoSessaoDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.TreinoSessaoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.TreinoSessaoRepository;
import com.example.demo.repository.FichaTreinoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.TreinoDesempenhoRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.StatusTreino;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
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
    private final TreinoDesempenhoRepository desempenhoRepository;

    public TreinoSessaoService(TreinoSessaoRepository repository,
                               AlunoRepository alunoRepository,
                               UsuarioRepository usuarioRepository,
                               FichaTreinoRepository fichaTreinoRepository,
                               TreinoSessaoMapper mapper,
                               TreinoDesempenhoRepository desempenhoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.mapper = mapper;
        this.desempenhoRepository = desempenhoRepository;
    }

    @Transactional
    public Double registrarSessao(UUID alunoUuid, TreinoSessaoDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        validarMesmaAcademia(aluno);

        FichaTreinoExercicio exercicio = fichaTreinoRepository.findByAluno_Uuid(alunoUuid).stream()
                .flatMap(f -> f.getCategorias().stream())
                .flatMap(c -> c.getExercicios().stream())
                .filter(e -> e.getExercicio().getUuid().equals(dto.getExercicioUuid()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Exercício não encontrado na ficha do aluno"));

        TreinoSessao sessao = repository
                .findByAlunoUuidAndExercicio_UuidAndData(alunoUuid, dto.getExercicioUuid(), dto.getData())
                .orElse(null);

        if (sessao == null) {
            sessao = mapper.toEntity(dto);
            sessao.setAluno(aluno);
            sessao.setExercicio(exercicio);
            sessao.setData(LocalDate.now());
            sessao.setStatus(StatusTreino.CONCLUIDO);
        } else if (sessao.getStatus() == StatusTreino.CONCLUIDO) {
            sessao.setStatus(StatusTreino.PENDENTE);
            sessao.setRepeticoesRealizadas(null);
            sessao.setCargaRealizada(null);
        } else {
            sessao.setRepeticoesRealizadas(dto.getRepeticoesRealizadas());
            sessao.setData(LocalDate.now());
            sessao.setCargaRealizada(dto.getCargaRealizada());
            sessao.setStatus(StatusTreino.CONCLUIDO);
        }

        repository.save(sessao);

        FichaTreinoCategoria categoria = exercicio.getCategoria();
        int total = categoria.getExercicios().size();
        long realizados = repository.countByAlunoUuidAndExercicio_Categoria_UuidAndDataAndStatus(alunoUuid, categoria.getUuid(), dto.getData(), StatusTreino.CONCLUIDO);
        double percentual = (double) realizados / total * 100.0;

        TreinoDesempenho desempenho = desempenhoRepository
                .findByAluno_UuidAndCategoria_UuidAndData(alunoUuid, categoria.getUuid(), dto.getData())
                .orElseGet(() -> {
                    TreinoDesempenho d = new TreinoDesempenho();
                    d.setAluno(aluno);
                    d.setCategoria(categoria);
                    d.setData(LocalDate.now());
                    return d;
                });
        desempenho.setPercentual(percentual);
        desempenhoRepository.save(desempenho);

        return percentual;
    }

    public List<TreinoSessaoDTO> listarSessoes(UUID alunoUuid) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        validarMesmaAcademia(aluno);
        return repository.findByAlunoUuid(alunoUuid).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public double buscarPercentualDoDia(UUID alunoUuid, UUID categoriaUuid) {
        return desempenhoRepository
                .findByAluno_UuidAndCategoria_UuidAndData(alunoUuid, categoriaUuid, LocalDate.now())
                .map(TreinoDesempenho::getPercentual)
                .orElse(0d);
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
