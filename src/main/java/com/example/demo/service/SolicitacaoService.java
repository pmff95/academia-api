package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.StatusSolicitacao;
import com.example.demo.dto.SolicitacaoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Solicitacao;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.SolicitacaoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final SolicitacaoMapper mapper;

    public SolicitacaoService(SolicitacaoRepository repository,
                              ProfessorRepository professorRepository,
                              AlunoRepository alunoRepository,
                              SolicitacaoMapper mapper) {
        this.repository = repository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.mapper = mapper;
    }

    public String solicitar(UUID alunoUuid) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null) {
            throw new ApiException("Usuário não autenticado");
        }
        Professor professor = professorRepository.findById(usuario.getUuid())
                .orElseThrow(() -> new ApiException("Professor não encontrado"));
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setProfessor(professor);
        solicitacao.setAluno(aluno);
        repository.save(solicitacao);
        return "Solicitação enviada";
    }

    public String responder(UUID solicitacaoUuid, boolean aceita) {
        Solicitacao solicitacao = repository.findById(solicitacaoUuid)
                .orElseThrow(() -> new ApiException("Solicitação não encontrada"));

        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new ApiException("Solicitação já respondida");
        }

        if (aceita) {
            solicitacao.setStatus(StatusSolicitacao.ACEITA);
            repository.save(solicitacao);
            Aluno aluno = solicitacao.getAluno();
            aluno.setProfessor(solicitacao.getProfessor());
            alunoRepository.save(aluno);
            return "Solicitação aceita";
        } else {
            solicitacao.setStatus(StatusSolicitacao.RECUSADA);
            repository.save(solicitacao);
            return "Solicitação recusada";
        }
    }

    public List<SolicitacaoDTO> listarPendentes(UUID alunoUuid) {
        return repository.findByAlunoUuidAndStatus(alunoUuid, StatusSolicitacao.PENDENTE)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
