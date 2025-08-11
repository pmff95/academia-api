package com.example.demo.service;

import com.example.demo.dto.AlunoMedidaDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.AlunoMedida;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.AlunoMedidaMapper;
import com.example.demo.repository.AlunoMedidaRepository;
import com.example.demo.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoMedidaService {

    private final AlunoMedidaRepository repository;
    private final AlunoRepository alunoRepository;
    private final AlunoMedidaMapper mapper;

    public AlunoMedidaService(AlunoMedidaRepository repository,
                              AlunoRepository alunoRepository,
                              AlunoMedidaMapper mapper) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public String adicionarMedida(UUID alunoUuid, AlunoMedidaDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoUuid)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        AlunoMedida medida = mapper.toEntity(dto);
        medida.setAluno(aluno);
        repository.save(medida);
        return "Medida registrada com sucesso";
    }

    public List<AlunoMedidaDTO> listarMedidas(UUID alunoUuid) {
        return repository.findByAlunoUuid(alunoUuid)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
