package com.example.demo.service;

import com.example.demo.dto.AlunoPagamentoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.AlunoPagamento;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.AlunoPagamentoRepository;
import com.example.demo.repository.AlunoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
public class AlunoPagamentoService {
    private final AlunoPagamentoRepository repository;
    private final AlunoRepository alunoRepository;
    private final ModelMapper mapper;

    public AlunoPagamentoService(AlunoPagamentoRepository repository,
                                 AlunoRepository alunoRepository,
                                 ModelMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public String registrar(UUID alunoUuid, AlunoPagamentoDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        AlunoPagamento pagamento = mapper.map(dto, AlunoPagamento.class);
        pagamento.setAluno(aluno);
        repository.save(pagamento);
        return "Pagamento registrado";
    }

    public AlunoPagamentoDTO buscarUltimoPagamento(UUID alunoUuid) {
        return repository.findTopByAlunoUuidOrderByDataPagamentoDesc(alunoUuid)
                .map(p -> mapper.map(p, AlunoPagamentoDTO.class))
                .orElse(null);
    }
}
