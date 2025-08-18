package com.example.demo.service;

import com.example.demo.dto.AlimentoDTO;
import com.example.demo.entity.Alimento;
import com.example.demo.mapper.AlimentoMapper;
import com.example.demo.repository.AlimentoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlimentoService {
    private final AlimentoRepository repository;
    private final AlimentoMapper mapper;

    public AlimentoService(AlimentoRepository repository, AlimentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public String create(AlimentoDTO dto) {
        Alimento entity = mapper.toEntity(dto);
        repository.save(entity);
        return "Alimento criado com sucesso";
    }
}
