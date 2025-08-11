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
        Aluno aluno = alunoRepository.findById(dto.getAlunoUuid())
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        Academia academia = null;
        boolean isMaster = usuario != null && usuario.possuiPerfil(Perfil.MASTER);
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
        List<Exercicio> exercicios = exercicioRepository.findAllById(dto.getExerciciosUuids());
        ficha.setExercicios(exercicios);
        repository.save(ficha);
        return "Ficha de treino criada com sucesso";
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
