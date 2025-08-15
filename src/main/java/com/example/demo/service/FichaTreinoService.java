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

    public FichaTreinoService(FichaTreinoRepository repository, FichaTreinoMapper mapper,
                              AlunoRepository alunoRepository, ProfessorRepository professorRepository,
                              UsuarioRepository usuarioRepository, ExercicioRepository exercicioRepository,
                              FichaTreinoHistoricoRepository historicoRepository) {
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
        FichaTreino ficha = new FichaTreino();
        Aluno aluno = null;

        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        Academia academia = null;
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
        if (!dto.isPreset()) {
            if (dto.getAlunoUuid() == null) {
                throw new ApiException("Aluno é obrigatório");
            }
            aluno = alunoRepository.findById(dto.getAlunoUuid())
                    .orElseThrow(() -> new ApiException("Aluno não encontrado"));
            if (usuario != null && !isMaster) {
                Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                        .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
                academia = usuarioEntity.getAcademia();
                if (academia == null) {
                    throw new ApiException("Usuário precisa ter uma academia associada");
                }
                if (aluno.getAcademia() == null || !aluno.getAcademia().getUuid().equals(academia.getUuid())) {
                    throw new ApiException("Aluno não pertence à sua academia");
                }
            }
            ficha.setAluno(aluno);
        } else {
            if (usuario != null && !isMaster) {
                Usuario usuarioEntity = usuarioRepository.findByUuid(usuario.getUuid())
                        .orElseThrow(() -> new ApiException("Usuário precisa ter uma academia associada"));
                academia = usuarioEntity.getAcademia();
                if (academia == null) {
                    throw new ApiException("Usuário precisa ter uma academia associada");
                }
            }
        }

        Professor professor = null;
        if (usuario != null && usuario.possuiPerfil(Perfil.PROFESSOR)) {
            professor = professorRepository.findById(usuario.getUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
        } else {
            if (dto.getProfessorUuid() == null) {
                throw new ApiException("Professor é obrigatório");
            }
            professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
        }

        if (academia != null && (professor.getAcademia() == null || !professor.getAcademia().getUuid().equals(academia.getUuid()))) {
            throw new ApiException("Professor não pertence à sua academia");
        }

        ficha.setProfessor(professor);
        if (dto.getCategoria() == null || dto.getCategoria().isBlank()) {
            throw new ApiException("Categoria é obrigatória");
        }
        ficha.setCategoria(dto.getCategoria());
        ficha.setPreset(dto.isPreset());
        List<FichaTreinoExercicio> exercicios = dto.getExercicios().stream().map(eDto -> {
            Exercicio exercicio = exercicioRepository.findById(eDto.getExercicioUuid())
                    .orElseThrow(() -> new ApiException("Exercício não encontrado"));
            FichaTreinoExercicio fe = new FichaTreinoExercicio();
            fe.setExercicio(exercicio);
            fe.setRepeticoes(eDto.getRepeticoes());
            fe.setCarga(eDto.getCarga());
            fe.setFicha(ficha);
            return fe;
        }).toList();
        ficha.setExercicios(exercicios);
        repository.save(ficha);
        if (!ficha.isPreset() && ficha.getAluno() != null) {
            FichaTreinoHistorico historico = new FichaTreinoHistorico();
            historico.setAluno(ficha.getAluno());
            historico.setFicha(ficha);
            historicoRepository.save(historico);
        }
        return "Ficha de treino criada com sucesso";
    }

    @Transactional
    public String assignPreset(UUID presetUuid, UUID alunoUuid) {
        FichaTreino preset = repository.findById(presetUuid)
                .orElseThrow(() -> new ApiException("Ficha de treino não encontrada"));
        if (!preset.isPreset()) {
            throw new ApiException("Ficha não é um pré-set");
        }
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
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
        return repository.findByAluno_Uuid(alunoUuid).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<FichaTreinoDTO> findHistoricoByAluno(UUID alunoUuid) {
        return historicoRepository.findByAluno_UuidOrderByDataCadastroDesc(alunoUuid).stream()
                .map(h -> mapper.toDto(h.getFicha()))
                .toList();
    }
}
