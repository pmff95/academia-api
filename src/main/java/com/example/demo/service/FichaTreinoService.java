package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoCategoriaDTO;
import com.example.demo.dto.FichaTreinoHistoricoDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.FichaTreinoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ExercicioRepository;
import com.example.demo.repository.FichaTreinoHistoricoRepository;
import com.example.demo.repository.FichaTreinoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.domain.enums.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FichaTreinoService {
    private final FichaTreinoRepository repository;
    private final FichaTreinoMapper mapper;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExercicioRepository exercicioRepository;
    private final FichaTreinoHistoricoRepository historicoRepository;

    public FichaTreinoService(FichaTreinoRepository repository, FichaTreinoMapper mapper, AlunoRepository alunoRepository, ProfessorRepository professorRepository, UsuarioRepository usuarioRepository, ExercicioRepository exercicioRepository, FichaTreinoHistoricoRepository historicoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.usuarioRepository = usuarioRepository;
        this.exercicioRepository = exercicioRepository;
        this.historicoRepository = historicoRepository;
    }

    @Transactional
    public String create(FichaTreinoDTO dto) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);

        Academia academia = obterAcademiaSeNecessario(usuario, isMaster);
        Aluno aluno = obterAlunoSeNecessario(dto, usuario, isMaster, academia);
        Professor professor = obterProfessorSeNecessario(dto, academia);

        FichaTreino ficha = montarFichaTreino(dto, aluno, professor);
        ficha.setCategorias(montarCategorias(dto, ficha));

        repository.save(ficha);

        salvarHistoricoSeNecessario(ficha);

        return "Ficha de treino criada com sucesso";
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

    private Professor obterProfessorSeNecessario(FichaTreinoDTO dto, Academia academia) {
        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid()).orElseThrow(() -> new ApiException("Professor não encontrado"));

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
        ficha.setAluno(aluno);
        ficha.setProfessor(professor);
        return ficha;
    }

    private List<FichaTreinoCategoria> montarCategorias(FichaTreinoDTO dto, FichaTreino ficha) {
        if (dto.getCategorias() == null) return List.of();
        return dto.getCategorias().stream().map(cDto -> {
            FichaTreinoCategoria categoria = new FichaTreinoCategoria();
            categoria.setNome(cDto.getNome());
            categoria.setFicha(ficha);
            categoria.setExercicios(montarExercicios(cDto, categoria));
            return categoria;
        }).toList();
    }

    private List<FichaTreinoExercicio> montarExercicios(FichaTreinoCategoriaDTO cDto, FichaTreinoCategoria categoria) {
        if (cDto.getExercicios() == null) return List.of();
        return cDto.getExercicios().stream().map(eDto -> {
            Exercicio exercicio = exercicioRepository.findById(eDto.getExercicioUuid()).orElseThrow(() -> new ApiException("Exercício não encontrado"));
            FichaTreinoExercicio fe = new FichaTreinoExercicio();
            fe.setExercicio(exercicio);
            fe.setRepeticoes(eDto.getRepeticoes());
            fe.setCarga(eDto.getCarga());
            fe.setCategoria(categoria);
            return fe;
        }).toList();
    }

    private void salvarHistoricoSeNecessario(FichaTreino ficha) {
        if (!ficha.isPreset() && ficha.getAluno() != null) {
            historicoRepository.findByAluno_UuidAndAtualTrue(ficha.getAluno().getUuid())
                    .ifPresent(h -> {
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
        ficha.setCategorias(new java.util.ArrayList<>());
        for (FichaTreinoCategoria categoria : preset.getCategorias()) {
            FichaTreinoCategoria novaCat = new FichaTreinoCategoria();
            novaCat.setNome(categoria.getNome());
            novaCat.setFicha(ficha);
            novaCat.setExercicios(new java.util.ArrayList<>());
            for (FichaTreinoExercicio exercicio : categoria.getExercicios()) {
                FichaTreinoExercicio novo = new FichaTreinoExercicio();
                novo.setExercicio(exercicio.getExercicio());
                novo.setRepeticoes(exercicio.getRepeticoes());
                novo.setCarga(exercicio.getCarga());
                novo.setCategoria(novaCat);
                novaCat.getExercicios().add(novo);
            }
            ficha.getCategorias().add(novaCat);
        }
        repository.save(ficha);
        historicoRepository.findByAluno_UuidAndAtualTrue(aluno.getUuid())
                .ifPresent(h -> {
                    h.setAtual(false);
                    historicoRepository.save(h);
                });

        FichaTreinoHistorico historico = new FichaTreinoHistorico();
        historico.setAluno(aluno);
        historico.setFicha(ficha);
        historico.setAtual(true);
        historicoRepository.save(historico);
        return "Ficha atribuída ao aluno";
    }


    public FichaTreinoDTO findByUuid(UUID fichaUuid) {
        FichaTreino ficha = repository.findByUuid(fichaUuid)
                .orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));
        return mapper.toDto(ficha);
    }

    public List<FichaTreinoHistoricoDTO> findHistoricoByAluno(UUID alunoUuid) {
        return historicoRepository.findByAluno_UuidOrderByDataCadastroDesc(alunoUuid)
                .stream()
                .map(mapper::toHistoricoDto)
                .toList();
    }

    @Transactional
    public String atualizarFichaAtual(UUID fichaUuid) {
        FichaTreinoHistorico historico = historicoRepository.findByFicha_Uuid(fichaUuid)
                .orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));

        UUID alunoUuid = historico.getAluno().getUuid();

        historicoRepository.findByAluno_UuidAndAtualTrue(alunoUuid)
                .ifPresent(h -> {
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
        return historicoRepository.findByAluno_UuidAndAtualTrue(alunoUuid)
                .map(h -> mapper.toDto(h.getFicha()))
                .orElseThrow(() -> new ApiException("Aluno não possui ficha de treino"));
    }
}
