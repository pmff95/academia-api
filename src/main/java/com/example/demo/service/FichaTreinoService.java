package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.FichaTreinoDTO;
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

        validarCategoria(dto.getCategoria());

        FichaTreino ficha = montarFichaTreino(dto, aluno, professor);
        ficha.setExercicios(montarExercicios(dto, ficha));

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

    private void validarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new ApiException("Categoria é obrigatória");
        }
    }

    private FichaTreino montarFichaTreino(FichaTreinoDTO dto, Aluno aluno, Professor professor) {
        FichaTreino ficha = new FichaTreino();
        ficha.setCategoria(dto.getCategoria());
        ficha.setPreset(dto.isPreset());
        ficha.setAluno(aluno);
        ficha.setProfessor(professor);
        return ficha;
    }

    private List<FichaTreinoExercicio> montarExercicios(FichaTreinoDTO dto, FichaTreino ficha) {
        return dto.getExercicios().stream().map(eDto -> {
            Exercicio exercicio = exercicioRepository.findById(eDto.getExercicioUuid()).orElseThrow(() -> new ApiException("Exercício não encontrado"));
            FichaTreinoExercicio fe = new FichaTreinoExercicio();
            fe.setExercicio(exercicio);
            fe.setRepeticoes(eDto.getRepeticoes());
            fe.setCarga(eDto.getCarga());
            fe.setFicha(ficha);
            return fe;
        }).toList();
    }

    private void salvarHistoricoSeNecessario(FichaTreino ficha) {
        if (!ficha.isPreset() && ficha.getAluno() != null) {
            FichaTreinoHistorico historico = new FichaTreinoHistorico();
            historico.setAluno(ficha.getAluno());
            historico.setFicha(ficha);
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
        ficha.setAluno(aluno);
        ficha.setProfessor(preset.getProfessor());
        ficha.setCategoria(preset.getCategoria());
        ficha.setPreset(false);
        ficha.setExercicios(new java.util.ArrayList<>());
        for (FichaTreinoExercicio exercicio : preset.getExercicios()) {
            FichaTreinoExercicio novo = new FichaTreinoExercicio();
            novo.setExercicio(exercicio.getExercicio());
            novo.setRepeticoes(exercicio.getRepeticoes());
            novo.setCarga(exercicio.getCarga());
            novo.setFicha(ficha);
            ficha.getExercicios().add(novo);
        }
        repository.save(ficha);
        FichaTreinoHistorico historico = new FichaTreinoHistorico();
        historico.setAluno(aluno);
        historico.setFicha(ficha);
        historicoRepository.save(historico);
        return "Ficha atribuída ao aluno";
    }

    public Page<FichaTreinoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public List<FichaTreinoDTO> findByAluno(UUID alunoUuid) {
        return repository.findByAluno_Uuid(alunoUuid).stream().map(mapper::toDto).toList();
    }

    public List<FichaTreinoDTO> findHistoricoByAluno(UUID alunoUuid) {
        return historicoRepository.findByAluno_UuidOrderByDataCadastroDesc(alunoUuid).stream().map(h -> mapper.toDto(h.getFicha())).toList();
    }
}
