package com.example.demo.service;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.entity.Academia;
import com.example.demo.mapper.AcademiaMapper;
import com.example.demo.repository.AcademiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AcademiaService {
    private final AcademiaRepository repository;
    private final AcademiaMapper mapper;

    public AcademiaService(AcademiaRepository repository, AcademiaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public String create(AcademiaDTO dto) {
        Academia entity = mapper.toEntity(dto);
        repository.save(entity);
        return "Academia criada com sucesso";
    }

    public Page<AcademiaDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}
