package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Exercicio;
import com.example.demo.entity.FichaTreino;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.FichaTreinoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ExercicioRepository;
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

    public FichaTreinoService(FichaTreinoRepository repository, FichaTreinoMapper mapper,
                              AlunoRepository alunoRepository, ProfessorRepository professorRepository,
                              UsuarioRepository usuarioRepository, ExercicioRepository exercicioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.usuarioRepository = usuarioRepository;
        this.exercicioRepository = exercicioRepository;
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

        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            if (academia != null && (professor.getAcademia() == null || !professor.getAcademia().getUuid().equals(academia.getUuid()))) {
                throw new ApiException("Professor não pertence à sua academia");
            }
            ficha.setProfessor(professor);
        }
        if (dto.getCategoria() == null || dto.getCategoria().isBlank()) {
            throw new ApiException("Categoria é obrigatória");
        }
        ficha.setCategoria(dto.getCategoria());
        ficha.setPreset(dto.isPreset());
        List<Exercicio> exercicios = exercicioRepository.findAllById(dto.getExerciciosUuids());
        ficha.setExercicios(exercicios);
        repository.save(ficha);
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
        ficha.setExercicios(new java.util.ArrayList<>(preset.getExercicios()));
        repository.save(ficha);
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
}
