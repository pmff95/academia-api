package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoCategoriaDTO;
import com.example.demo.dto.FichaTreinoHistoricoDTO;
import com.example.demo.dto.FichaTreinoExercicioDTO;
import com.example.demo.dto.CategoriaListagemDTO;
import com.example.demo.dto.CargaDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.FichaTreinoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ExercicioRepository;
import com.example.demo.repository.FichaTreinoHistoricoRepository;
import com.example.demo.repository.FichaTreinoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.TreinoSessaoRepository;
import com.example.demo.repository.TreinoDesempenhoRepository;
import com.example.demo.service.TreinoSessaoService;
import com.example.demo.domain.enums.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;

import com.example.demo.domain.enums.StatusTreino;

@Service
public class FichaTreinoService {
    private final FichaTreinoRepository repository;
    private final FichaTreinoMapper mapper;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExercicioRepository exercicioRepository;
    private final FichaTreinoHistoricoRepository historicoRepository;
    private final NotificacaoService notificacaoService;
    private final TreinoSessaoRepository treinoSessaoRepository;
    private final TreinoDesempenhoRepository desempenhoRepository;
    private final TreinoSessaoService treinoSessaoService;

    public FichaTreinoService(FichaTreinoRepository repository, FichaTreinoMapper mapper, AlunoRepository alunoRepository, ProfessorRepository professorRepository, UsuarioRepository usuarioRepository, ExercicioRepository exercicioRepository, FichaTreinoHistoricoRepository historicoRepository, NotificacaoService notificacaoService, TreinoSessaoRepository treinoSessaoRepository, TreinoDesempenhoRepository desempenhoRepository, TreinoSessaoService treinoSessaoService) {
        this.repository = repository;
        this.mapper = mapper;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.usuarioRepository = usuarioRepository;
        this.exercicioRepository = exercicioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoService = notificacaoService;
        this.treinoSessaoRepository = treinoSessaoRepository;
        this.desempenhoRepository = desempenhoRepository;
        this.treinoSessaoService = treinoSessaoService;
    }

    @Transactional
    public String save(FichaTreinoDTO dto) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);

        boolean isNew = dto.getUuid() == null;
        Academia academia = obterAcademiaSeNecessario(usuario, isMaster);
        Aluno aluno = obterAlunoSeNecessario(dto, usuario, isMaster, academia);
        Professor professor = obterProfessorSeNecessario(dto, usuario, academia);

        FichaTreino ficha;
        if (isNew) {
            ficha = montarFichaTreino(dto, aluno, professor);
            ficha.setCategorias(montarCategorias(dto, ficha));
        } else {
            ficha = repository.findByUuid(dto.getUuid()).orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));
            ficha.setNome(dto.getNome());
            ficha.setPreset(dto.isPreset());
            ficha.setAluno(aluno);
            ficha.setProfessor(professor);
            ficha.setDataValidade(dto.getDataValidade());

            atualizarCategorias(ficha, dto.getCategorias());
        }

        repository.save(ficha);

        if (isNew) {
            if (aluno != null) {
                treinoSessaoRepository.deleteByAlunoUuid(aluno.getUuid());
                treinoSessaoService.gerarSessoesFuturas(ficha, LocalDate.now(), 0);
            }
            salvarHistoricoSeNecessario(ficha);
            if (aluno != null) {
                notificacaoService.notificarNovaFichaTreino(aluno);
            }
            return "Ficha de treino criada com sucesso";
        }
        if (aluno != null) {
            notificacaoService.notificarNovaFichaTreino(aluno);
        }
        return "Ficha de treino atualizada com sucesso";
    }


    private Academia obterAcademiaSeNecessario(UsuarioLogado usuario, boolean isMaster) {
        if (usuario != null && !isMaster) {
            Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid()).orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
            if (usuarioEntity.getAcademia() == null) {
                throw new ApiException("Usuário precisa ter uma academia associada");
            }
            return usuarioEntity.getAcademia();
        }
        return null;
    }

    private Aluno obterAlunoSeNecessario(FichaTreinoDTO dto, UsuarioLogado usuario, boolean isMaster, Academia academia) {
        if (!dto.isPreset()) {
            if (dto.getAlunoUuid() == null) throw new ApiException("Aluno é obrigatório");

            Aluno aluno = alunoRepository.findById(dto.getAlunoUuid()).orElseThrow(() -> new ApiException("Aluno não encontrado"));

            if (usuario != null && !isMaster && !alunoPertenceAcademia(aluno, academia)) {
                throw new ApiException("Aluno não pertence à sua academia");
            }
            return aluno;
        }
        return null;
    }

    private boolean alunoPertenceAcademia(Aluno aluno, Academia academia) {
        return aluno.getAcademia() != null && aluno.getAcademia().getUuid().equals(academia.getUuid());
    }

    private Professor obterProfessorSeNecessario(FichaTreinoDTO dto, UsuarioLogado usuario, Academia academia) {
        UUID professorUuid = dto.getProfessorUuid();

        if (usuario != null && usuario.possuiPerfil(Perfil.PROFESSOR)) {
            professorUuid = usuario.getUuid();
        }

        if (professorUuid != null) {
            Professor professor = professorRepository.findById(professorUuid).orElseThrow(() -> new ApiException("Professor não encontrado"));

            if (academia != null && !professorPertenceAcademia(professor, academia)) {
                throw new ApiException("Professor não pertence à sua academia");
            }
            return professor;
        }
        return null;
    }

    private boolean professorPertenceAcademia(Professor professor, Academia academia) {
        return professor.getAcademia() != null && professor.getAcademia().getUuid().equals(academia.getUuid());
    }

    private FichaTreino montarFichaTreino(FichaTreinoDTO dto, Aluno aluno, Professor professor) {
        FichaTreino ficha = new FichaTreino();
        ficha.setNome(dto.getNome());
        ficha.setPreset(dto.isPreset());
        ficha.setDataValidade(dto.getDataValidade());
        ficha.setAluno(aluno);
        ficha.setProfessor(professor);
        return ficha;
    }

    private List<FichaTreinoCategoria> montarCategorias(FichaTreinoDTO dto, FichaTreino ficha) {
        if (dto.getCategorias() == null) return new ArrayList<>();
        return dto.getCategorias().stream().map(cDto -> {
            FichaTreinoCategoria categoria = new FichaTreinoCategoria();
            categoria.setNome(cDto.getNome());
            categoria.setObservacao(cDto.getObservacao());
            categoria.setFicha(ficha);
            categoria.setExercicios(montarExercicios(cDto, categoria));
            return categoria;
        }).collect(Collectors.toList());
    }

    private List<FichaTreinoExercicio> montarExercicios(FichaTreinoCategoriaDTO cDto, FichaTreinoCategoria categoria) {
        if (cDto.getExercicios() == null) return new ArrayList<>();
        return cDto.getExercicios().stream().map(eDto -> {
            Exercicio exercicio = exercicioRepository.findById(eDto.getExercicioUuid()).orElseThrow(() -> new ApiException("Exercício não encontrado"));
            FichaTreinoExercicio fe = new FichaTreinoExercicio();
            fe.setExercicio(exercicio);
            fe.setTipo(eDto.getTipo());
            if (eDto.getCargas() != null) {
                fe.setRepeticoes(eDto.getCargas().stream().map(CargaDTO::getRepeticoes).collect(Collectors.toList()));
                fe.setCarga(eDto.getCargas().stream().map(CargaDTO::getPeso).collect(Collectors.toList()));
            }
            fe.setSeries(eDto.getSeries());
            fe.setTempoDescanso(eDto.getTempoDescanso());
            fe.setCategoria(categoria);
            return fe;
        }).collect(Collectors.toList());
    }

    private void atualizarCategorias(FichaTreino ficha, List<FichaTreinoCategoriaDTO> categoriasDto) {
        Map<UUID, FichaTreinoCategoria> existentes = ficha.getCategorias().stream().collect(Collectors.toMap(FichaTreinoCategoria::getUuid, c -> c));

        List<FichaTreinoCategoria> atualizadas = new ArrayList<>();

        if (categoriasDto != null) {
            for (FichaTreinoCategoriaDTO cDto : categoriasDto) {
                FichaTreinoCategoria categoria = cDto.getUuid() != null ? existentes.remove(cDto.getUuid()) : null;
                if (categoria == null) {
                    categoria = new FichaTreinoCategoria();
                    categoria.setFicha(ficha);
                }
                categoria.setNome(cDto.getNome());
                categoria.setObservacao(cDto.getObservacao());
                atualizarExercicios(categoria, cDto.getExercicios());
                atualizadas.add(categoria);
            }
        }

        for (FichaTreinoCategoria antiga : existentes.values()) {
            boolean referenced = antiga.getExercicios().stream().anyMatch(ex -> treinoSessaoRepository.existsByExercicio_Uuid(ex.getUuid()));
            if (referenced) {
                atualizadas.add(antiga);
            }
            // categorias não referenciadas não são adicionadas e serão removidas
        }

        ficha.getCategorias().clear();
        ficha.getCategorias().addAll(atualizadas);
    }

    private void atualizarExercicios(FichaTreinoCategoria categoria, List<FichaTreinoExercicioDTO> exerciciosDto) {
        Map<UUID, FichaTreinoExercicio> existentes = categoria.getExercicios().stream().collect(Collectors.toMap(FichaTreinoExercicio::getUuid, e -> e));

        List<FichaTreinoExercicio> atualizados = new ArrayList<>();

        if (exerciciosDto != null) {
            for (FichaTreinoExercicioDTO eDto : exerciciosDto) {
                FichaTreinoExercicio exercicio = eDto.getUuid() != null ? existentes.remove(eDto.getUuid()) : null;
                if (exercicio == null) {
                    exercicio = new FichaTreinoExercicio();
                    exercicio.setCategoria(categoria);
                }
                Exercicio exercicioEntity = exercicioRepository.findById(eDto.getExercicioUuid()).orElseThrow(() -> new ApiException("Exercício não encontrado"));
                exercicio.setExercicio(exercicioEntity);
                exercicio.setTipo(eDto.getTipo());
                if (eDto.getCargas() != null) {
                    exercicio.setRepeticoes(eDto.getCargas().stream().map(CargaDTO::getRepeticoes).collect(Collectors.toList()));
                    exercicio.setCarga(eDto.getCargas().stream().map(CargaDTO::getPeso).collect(Collectors.toList()));
                } else {
                    exercicio.setRepeticoes(null);
                    exercicio.setCarga(null);
                }
                exercicio.setSeries(eDto.getSeries());
                exercicio.setTempoDescanso(eDto.getTempoDescanso());
                atualizados.add(exercicio);
            }
        }

        for (FichaTreinoExercicio antigo : existentes.values()) {
            if (treinoSessaoRepository.existsByExercicio_Uuid(antigo.getUuid())) {
                atualizados.add(antigo);
            }
            // exercícios não referenciados não são adicionados e serão removidos
        }

        categoria.getExercicios().clear();
        categoria.getExercicios().addAll(atualizados);

    }

    private void salvarHistoricoSeNecessario(FichaTreino ficha) {
        if (!ficha.isPreset() && ficha.getAluno() != null) {
            historicoRepository.findByAluno_UuidAndAtualTrue(ficha.getAluno().getUuid()).ifPresent(h -> {
                h.setAtual(false);
                historicoRepository.save(h);
            });

            FichaTreinoHistorico historico = new FichaTreinoHistorico();
            historico.setAluno(ficha.getAluno());
            historico.setFicha(ficha);
            historico.setAtual(true);
            historicoRepository.save(historico);
        }
    }

    @Transactional
    public String assignPreset(UUID presetUuid, UUID alunoUuid) {
        FichaTreino preset = repository.findById(presetUuid).orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));
        if (!preset.isPreset()) {
            throw new ApiException("Ficha não é um pré-set");
        }
        Aluno aluno = alunoRepository.findById(alunoUuid).orElseThrow(() -> new ApiException("Aluno não encontrado"));
        FichaTreino ficha = new FichaTreino();
        ficha.setNome(preset.getNome());
        ficha.setAluno(aluno);
        ficha.setProfessor(preset.getProfessor());
        ficha.setPreset(false);
        ficha.setDataValidade(preset.getDataValidade());
        ficha.setCategorias(new ArrayList<>());
        for (FichaTreinoCategoria categoria : preset.getCategorias()) {
            FichaTreinoCategoria novaCat = new FichaTreinoCategoria();
            novaCat.setNome(categoria.getNome());
            novaCat.setObservacao(categoria.getObservacao());
            novaCat.setFicha(ficha);
            novaCat.setExercicios(new ArrayList<>());
            for (FichaTreinoExercicio exercicio : categoria.getExercicios()) {
                FichaTreinoExercicio novo = new FichaTreinoExercicio();
                novo.setExercicio(exercicio.getExercicio());
                novo.setTipo(exercicio.getTipo());
                novo.setRepeticoes(exercicio.getRepeticoes());
                novo.setCarga(exercicio.getCarga());
                novo.setSeries(exercicio.getSeries());
                novo.setTempoDescanso(exercicio.getTempoDescanso());
                novo.setCategoria(novaCat);
                novaCat.getExercicios().add(novo);
            }
            ficha.getCategorias().add(novaCat);
        }
        repository.save(ficha);
        historicoRepository.findByAluno_UuidAndAtualTrue(aluno.getUuid()).ifPresent(h -> {
            h.setAtual(false);
            historicoRepository.save(h);
        });

        FichaTreinoHistorico historico = new FichaTreinoHistorico();
        historico.setAluno(aluno);
        historico.setFicha(ficha);
        historico.setAtual(true);
        historicoRepository.save(historico);
        notificacaoService.notificarNovaFichaTreino(aluno);
        return "Ficha atribuída ao aluno";
    }


    @Transactional
    public FichaTreinoDTO findByUuid(UUID fichaUuid) {
        FichaTreino ficha = repository.findByUuid(fichaUuid)
                .orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));
        ficha.getCategorias().forEach(c -> c.getExercicios().size());
        return mapper.toDto(ficha);
    }

    @Transactional(readOnly = true)
    public FichaTreinoDTO findByCategoriaUuid(UUID categoriaUuid) {
        FichaTreino ficha = repository.findByCategorias_Uuid(categoriaUuid)
                .orElseThrow(() -> new ApiException("Categoria de treino não encontrada"));

        List<FichaTreinoCategoria> categorias = ficha.getCategorias().stream()
                .filter(c -> c.getUuid().equals(categoriaUuid))
                .collect(Collectors.toList());

        categorias.forEach(c -> c.getExercicios().size());

        FichaTreino fichaTemp = new FichaTreino();
        fichaTemp.setUuid(ficha.getUuid());
        fichaTemp.setAluno(ficha.getAluno());
        fichaTemp.setProfessor(ficha.getProfessor());
        fichaTemp.setNome(ficha.getNome());
        fichaTemp.setPreset(ficha.isPreset());
        fichaTemp.setDataValidade(ficha.getDataValidade());
        fichaTemp.setDataCadastro(ficha.getDataCadastro());
        fichaTemp.setDataAtualizacao(ficha.getDataAtualizacao());
        fichaTemp.setCategorias(categorias);

        return mapper.toDto(fichaTemp);
    }

    public List<FichaTreinoHistoricoDTO> findHistoricoByAluno(UUID alunoUuid) {
        return historicoRepository.findByAluno_UuidOrderByDataCadastroDesc(alunoUuid).stream().map(mapper::toHistoricoDto).toList();
    }

    @Transactional
    public String atualizarFichaAtual(UUID fichaUuid) {
        FichaTreinoHistorico historico = historicoRepository.findByFicha_Uuid(fichaUuid).orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));

        UUID alunoUuid = historico.getAluno().getUuid();

        historicoRepository.findByAluno_UuidAndAtualTrue(alunoUuid).ifPresent(h -> {
            if (!h.getFicha().getUuid().equals(fichaUuid)) {
                h.setAtual(false);
                historicoRepository.save(h);
            }
        });

        historico.setAtual(true);
        historicoRepository.save(historico);

        return "Ficha definida como atual";
    }

    public FichaTreinoDTO findCurrentByAluno(UUID alunoUuid) {
        FichaTreino ficha = historicoRepository.findByAluno_UuidAndAtualTrue(alunoUuid).map(FichaTreinoHistorico::getFicha).orElseThrow(() -> new ApiException("Aluno não possui ficha de treino"));

        FichaTreinoDTO dto = mapper.toDto(ficha);

        if (dto.getCategorias() == null || dto.getCategorias().isEmpty()) {
            return dto;
        }

        List<FichaTreinoCategoria> categorias = ficha.getCategorias();
        List<TreinoSessao> sessoesHoje = treinoSessaoRepository.findByAlunoUuidAndData(alunoUuid, LocalDate.now());

        UUID proximaCategoriaUuid;
        if (!sessoesHoje.isEmpty()) {
            proximaCategoriaUuid = sessoesHoje.get(0).getExercicio().getCategoria().getUuid();
        } else {
            List<TreinoSessao> sessoes = treinoSessaoRepository
                    .findByAlunoUuidAndDataBeforeOrderByDataDesc(alunoUuid, LocalDate.now());
            if (sessoes.isEmpty()) {
                proximaCategoriaUuid = categorias.get(0).getUuid();
            } else {
                LocalDate ultimaData = sessoes.get(0).getData();
                int lastIndex = -1;
                for (TreinoSessao sessao : sessoes) {
                    if (!sessao.getData().isEqual(ultimaData)) break;
                    int idx = categorias.indexOf(sessao.getExercicio().getCategoria());
                    if (idx > lastIndex) lastIndex = idx;
                }
                proximaCategoriaUuid = categorias.get((lastIndex + 1) % categorias.size()).getUuid();
            }
        }

        Map<UUID, StatusTreino> statusMap = sessoesHoje.stream()
                .collect(Collectors.toMap(s -> s.getExercicio().getUuid(), TreinoSessao::getStatus, (a, b) -> b));

        dto.getCategorias().forEach(c -> {
            boolean isAtivo = c.getUuid().equals(proximaCategoriaUuid);
            c.setAtivo(isAtivo);
            desempenhoRepository.findByAluno_UuidAndCategoria_UuidAndData(alunoUuid, c.getUuid(), LocalDate.now())
                    .ifPresent(d -> c.setPercentualConcluido(d.getPercentual()));
            c.getExercicios().forEach(e -> e.setStatus(statusMap.getOrDefault(e.getUuid(), StatusTreino.PENDENTE)));
        });

        return dto;
    }

    public List<CategoriaListagemDTO> listarCategorias(UUID usuarioUuid) {
        UUID alunoUuid = usuarioUuid;
        if (alunoUuid == null) {
            UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
            if (usuario == null) {
                throw new ApiException("Usuário não autenticado");
            }
            alunoUuid = usuario.getUuid();
        }

        FichaTreinoDTO ficha = findCurrentByAluno(alunoUuid);

        if (ficha.getCategorias() == null) {
            return new ArrayList<>();
        }

        return ficha.getCategorias().stream().map(c -> {
            CategoriaListagemDTO dto = new CategoriaListagemDTO();
            dto.setUuid(c.getUuid());
            dto.setNome(c.getNome());
            List<Musculo> musculos = c.getExercicios().stream()
                    .map(FichaTreinoExercicioDTO::getMusculo)
                    .distinct()
                    .collect(Collectors.toList());
            dto.setMusculos(musculos);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<FichaTreinoDTO> findPresetsByProfessor() {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null) {
            throw new ApiException("Usuário não autenticado");
        }
        List<FichaTreino> fichas = repository.findByProfessor_UuidAndPresetTrue(usuario.getUuid());
        return fichas.stream().map(mapper::toDto).toList();
    }
}
