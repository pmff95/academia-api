package com.example.demo.service;

import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Exercicio;
import com.example.demo.mapper.ExercicioMapper;
import com.example.demo.repository.ExercicioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExercicioService {
    private final ExercicioRepository repository;
    private final ExercicioMapper mapper;

    public ExercicioService(ExercicioRepository repository, ExercicioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public String create(ExercicioDTO dto) {
        Exercicio entity = mapper.toEntity(dto);
        repository.save(entity);
        return "Exercício criado com sucesso";
    }

    public Page<ExercicioDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}
