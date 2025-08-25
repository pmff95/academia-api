package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
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
    private final TreinoDesempenhoRepository desempenhoRepository;

    public TreinoSessaoService(TreinoSessaoRepository repository,
                               AlunoRepository alunoRepository,
                               UsuarioRepository usuarioRepository,
                               FichaTreinoRepository fichaTreinoRepository,
                               TreinoDesempenhoRepository desempenhoRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.fichaTreinoRepository = fichaTreinoRepository;
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
                .findByAluno_UuidAndExercicio_UuidAndData(alunoUuid, dto.getExercicioUuid(), LocalDate.now())
                .orElse(null);

        FichaTreinoCategoria categoria = exercicio.getCategoria();

        if (sessao != null && sessao.getStatus() == StatusTreino.CONCLUIDO) {
            sessao.setRepeticoesRealizadas(null);
            sessao.setCargaRealizada(null);
            sessao.setStatus(StatusTreino.PENDENTE);
            repository.save(sessao);

            int total = categoria.getExercicios().size();
            long realizados = repository.countByAlunoUuidAndExercicio_Categoria_UuidAndDataAndStatus(alunoUuid, categoria.getUuid(), LocalDate.now(), StatusTreino.CONCLUIDO);
            double percentual = (double) realizados / total * 100.0;

            TreinoDesempenho desempenho = desempenhoRepository
                    .findByAluno_UuidAndCategoria_UuidAndData(alunoUuid, categoria.getUuid(), LocalDate.now())
                    .orElseGet(() -> {
                        TreinoDesempenho d = new TreinoDesempenho();
                        d.setAluno(aluno);
                        d.setCategoria(categoria);
                        d.setData(LocalDate.now());
                        return d;
                    });
            desempenho.setPercentual(percentual);
            desempenhoRepository.save(desempenho);

            FichaTreino ficha = categoria.getFicha();
            repository.deleteByAlunoUuidAndDataAfter(alunoUuid, LocalDate.now());
            List<FichaTreinoCategoria> categorias = ficha.getCategorias();
            int idx = categorias.indexOf(categoria);
            gerarSessoesFuturas(ficha, LocalDate.now().plusDays(1), idx);

            return percentual;
        }

        if (sessao == null) {
            sessao = repository.findFirstByAluno_UuidAndExercicio_UuidAndStatusOrderByDataAsc(
                            alunoUuid, dto.getExercicioUuid(), StatusTreino.PENDENTE)
                    .orElseThrow(() -> new ApiException("Sessão de treino não encontrada"));

            if (sessao.getData().isAfter(LocalDate.now())) {
                List<TreinoSessao> mesmasDatas = repository.findByAlunoUuidAndData(alunoUuid, sessao.getData());
                mesmasDatas.forEach(s -> s.setData(LocalDate.now()));
                repository.saveAll(mesmasDatas);
            }
        }

        sessao.setRepeticoesRealizadas(dto.getRepeticoesRealizadas());
        sessao.setCargaRealizada(dto.getCargaRealizada());
        sessao.setStatus(StatusTreino.CONCLUIDO);

        repository.save(sessao);

        int total = categoria.getExercicios().size();
        long realizados = repository.countByAlunoUuidAndExercicio_Categoria_UuidAndDataAndStatus(alunoUuid, categoria.getUuid(), LocalDate.now(), StatusTreino.CONCLUIDO);
        double percentual = (double) realizados / total * 100.0;

        TreinoDesempenho desempenho = desempenhoRepository
                .findByAluno_UuidAndCategoria_UuidAndData(alunoUuid, categoria.getUuid(), LocalDate.now())
                .orElseGet(() -> {
                    TreinoDesempenho d = new TreinoDesempenho();
                    d.setAluno(aluno);
                    d.setCategoria(categoria);
                    d.setData(LocalDate.now());
                    return d;
                });
        desempenho.setPercentual(percentual);
        desempenhoRepository.save(desempenho);

        FichaTreino ficha = categoria.getFicha();
        repository.deleteByAlunoUuidAndDataAfter(alunoUuid, LocalDate.now());
        List<FichaTreinoCategoria> categorias = ficha.getCategorias();
        int idx = categorias.indexOf(categoria);
        gerarSessoesFuturas(ficha, LocalDate.now().plusDays(1), idx + 1);

        return percentual;
    }

    public void gerarSessoesFuturas(FichaTreino ficha, LocalDate inicio, int startIndex) {
        List<FichaTreinoCategoria> categorias = ficha.getCategorias();
        if (categorias == null || categorias.isEmpty()) return;
        Aluno aluno = ficha.getAluno();
        LocalDate data = inicio;
        LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());
        int idx = startIndex;
        while (!data.isAfter(fim)) {
            FichaTreinoCategoria cat = categorias.get(idx % categorias.size());
            for (FichaTreinoExercicio fe : cat.getExercicios()) {
                TreinoSessao s = new TreinoSessao();
                s.setAluno(aluno);
                s.setExercicio(fe);
                s.setData(data);
                s.setStatus(StatusTreino.PENDENTE);
                repository.save(s);
            }
            data = data.plusDays(1);
            idx++;
        }
    }

    public TreinoResumoDTO resumoTreinos(UUID alunoUuid, int ano, int mes) {
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());

        List<TreinoDesempenhoDTO> diasTreinados = desempenhoRepository
                .findByAluno_UuidAndDataBetweenOrderByData(alunoUuid, inicio, fim).stream()
                .map(d -> {
                    TreinoDesempenhoDTO td = new TreinoDesempenhoDTO();
                    td.setData(d.getData());
                    td.setPercentual(d.getPercentual());
                    return td;
                }).collect(Collectors.toList());

        List<TreinoDesempenho> todos = desempenhoRepository.findByAluno_UuidOrderByDataAsc(alunoUuid);
        int streak = calcularStreak(todos.stream().map(TreinoDesempenho::getData).toList(), LocalDate.now());

        TreinoResumoDTO resumo = new TreinoResumoDTO();
        resumo.setDiasConsecutivos(streak);
        resumo.setDiasTreinados(diasTreinados);
        return resumo;
    }

    private int calcularStreak(List<LocalDate> dias, LocalDate hoje) {
        int streak = 0;
        LocalDate data = hoje;
        while (true) {
            if (dias.contains(data)) {
                streak++;
            } else {
                switch (data.getDayOfWeek()) {
                    case SATURDAY, SUNDAY -> {
                        data = data.minusDays(1);
                        continue;
                    }
                    default -> {
                        return streak;
                    }
                }
            }
            data = data.minusDays(1);
        }
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
