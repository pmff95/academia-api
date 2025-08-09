package com.example.demo.service;

import com.example.demo.dto.MedidaHistoricaDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.MedidaHistorica;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.MedidaHistoricaMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.MedidaHistoricaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedidaHistoricaService {
    private final MedidaHistoricaRepository medidaRepository;
    private final AlunoRepository alunoRepository;
    private final MedidaHistoricaMapper mapper;

    public MedidaHistoricaService(MedidaHistoricaRepository medidaRepository,
                                  AlunoRepository alunoRepository,
                                  MedidaHistoricaMapper mapper) {
        this.medidaRepository = medidaRepository;
        this.alunoRepository = alunoRepository;
        this.mapper = mapper;
    }

    public MedidaHistoricaDTO create(Long alunoId, MedidaHistoricaDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ApiException("Aluno não encontrado"));
        MedidaHistorica entity = mapper.toEntity(dto, aluno);
        return mapper.toDto(medidaRepository.save(entity));
    }

    public Page<MedidaHistoricaDTO> listByAluno(Long alunoId, Pageable pageable) {
        return medidaRepository.findAll((root, query, cb) ->
                cb.equal(root.get("aluno").get("id"), alunoId), pageable)
                .map(mapper::toDto);
    }
}
