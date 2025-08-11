package com.example.demo.service;

import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Exercicio;
import com.example.demo.entity.FichaTreino;
import com.example.demo.entity.Professor;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.FichaTreinoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ExercicioRepository;
import com.example.demo.repository.FichaTreinoRepository;
import com.example.demo.repository.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FichaTreinoService {
    private final FichaTreinoRepository repository;
    private final FichaTreinoMapper mapper;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final ExercicioRepository exercicioRepository;

    public FichaTreinoService(FichaTreinoRepository repository, FichaTreinoMapper mapper,
                              AlunoRepository alunoRepository, ProfessorRepository professorRepository,
                              ExercicioRepository exercicioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.exercicioRepository = exercicioRepository;
    }

    public String create(FichaTreinoDTO dto) {
        FichaTreino ficha = new FichaTreino();
        Aluno aluno = alunoRepository.findById(dto.getAlunoUuid())
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        ficha.setAluno(aluno);
        if (dto.getProfessorUuid() != null) {
            Professor professor = professorRepository.findById(dto.getProfessorUuid())
                    .orElseThrow(() -> new ApiException("Professor não encontrado"));
            ficha.setProfessor(professor);
        }
        List<Exercicio> exercicios = exercicioRepository.findAllById(dto.getExerciciosIds());
        ficha.setExercicios(exercicios);
        repository.save(ficha);
        return "Ficha de treino criada com sucesso";
    }

    public Page<FichaTreinoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}
