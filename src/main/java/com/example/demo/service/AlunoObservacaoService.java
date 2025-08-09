package com.example.demo.service;

import com.example.demo.dto.AlunoObservacaoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.AlunoObservacao;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoObservacaoMapper;
import com.example.demo.repository.AlunoObservacaoRepository;
import com.example.demo.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoObservacaoService {

    private final AlunoObservacaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final AlunoObservacaoMapper mapper;

    public AlunoObservacaoService(AlunoObservacaoRepository repository,
                                  AlunoRepository alunoRepository,
                                  AlunoObservacaoMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.mapper = mapper;
    }

    public String adicionarObservacao(Long alunoId, AlunoObservacaoDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        AlunoObservacao obs = mapper.toEntity(dto);
        obs.setAluno(aluno);
        repository.save(obs);
        return "Observação registrada com sucesso";
    }

    public List<AlunoObservacaoDTO> listarObservacoes(Long alunoId) {
        return repository.findByAlunoId(alunoId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
