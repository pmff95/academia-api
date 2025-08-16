package com.example.demo.service;

import com.example.demo.domain.enums.StatusInscricao;
import com.example.demo.entity.*;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AulaService {
    private final AulaRepository aulaRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final AulaAlunoRepository aulaAlunoRepository;

    public AulaService(AulaRepository aulaRepository, ProfessorRepository professorRepository,
                       AlunoRepository alunoRepository, AulaAlunoRepository aulaAlunoRepository) {
        this.aulaRepository = aulaRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.aulaAlunoRepository = aulaAlunoRepository;
    }

    @Transactional
    public Aula salvar(Aula aula) {
        if (aula.getProfessor() == null || aula.getProfessor().getUuid() == null) {
            throw new ApiException("Professor é obrigatório");
        }
        Professor professor = professorRepository.findById(aula.getProfessor().getUuid())
                .orElseThrow(() -> new ApiException("Professor não encontrado"));

        boolean overlap;
        if (aula.getUuid() == null) {
            overlap = aulaRepository.existsByProfessorAndInicioLessThanAndFimGreaterThan(professor, aula.getFim(), aula.getInicio());
        } else {
            overlap = aulaRepository.existsByProfessorAndInicioLessThanAndFimGreaterThanAndUuidNot(professor, aula.getFim(), aula.getInicio(), aula.getUuid());
        }
        if (overlap) {
            throw new ApiException("Professor já possui aula no horário informado");
        }
        aula.setProfessor(professor);
        return aulaRepository.save(aula);
    }

    public List<Aula> listar() {
        return aulaRepository.findAll();
    }

    @Transactional
    public String inscrever(UUID aulaUuid, UUID alunoUuid) {
        Aula aula = aulaRepository.findById(aulaUuid)
                .orElseThrow(() -> new ApiException("Aula não encontrada"));
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));

        AulaAluno inscricao = aulaAlunoRepository.findByAulaAndAluno(aula, aluno).orElse(null);
        if (inscricao != null && inscricao.getStatus() == StatusInscricao.CONFIRMADA) {
            throw new ApiException("Aluno já inscrito nesta aula");
        }

        long confirmadas = aulaAlunoRepository.countByAulaAndStatus(aula, StatusInscricao.CONFIRMADA);
        if (confirmadas >= aula.getVagas()) {
            throw new ApiException("Aula sem vagas disponíveis");
        }

        if (inscricao == null) {
            inscricao = new AulaAluno();
            inscricao.setAula(aula);
            inscricao.setAluno(aluno);
        }
        inscricao.setStatus(StatusInscricao.CONFIRMADA);
        aulaAlunoRepository.save(inscricao);
        return "Inscrição realizada com sucesso";
    }

    @Transactional
    public String cancelar(UUID aulaUuid, UUID alunoUuid) {
        Aula aula = aulaRepository.findById(aulaUuid)
                .orElseThrow(() -> new ApiException("Aula não encontrada"));
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        AulaAluno inscricao = aulaAlunoRepository.findByAulaAndAluno(aula, aluno)
                .orElseThrow(() -> new ApiException("Inscrição não encontrada"));
        inscricao.setStatus(StatusInscricao.CANCELADA);
        aulaAlunoRepository.save(inscricao);
        return "Inscrição cancelada com sucesso";
    }
}
