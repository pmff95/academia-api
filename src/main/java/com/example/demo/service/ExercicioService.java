package com.example.demo.service;

import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Exercicio;
import com.example.demo.mapper.ExercicioMapper;
import com.example.demo.repository.AcademiaRepository;
import com.example.demo.repository.ExercicioRepository;
import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.entity.Academia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExercicioService {
    private final ExercicioRepository repository;
    private final ExercicioMapper mapper;
    private final AcademiaRepository academiaRepository;

    public ExercicioService(ExercicioRepository repository, ExercicioMapper mapper,
                            AcademiaRepository academiaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.academiaRepository = academiaRepository;
    }

    public String create(ExercicioDTO dto) {
        Exercicio entity = mapper.toEntity(dto);
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        if (usuario != null) {
            academiaRepository.findByAdminUuid(usuario.getUuid())
                    .ifPresent(entity::setAcademia);
        }
        repository.save(entity);
        return "Exercício criado com sucesso";
    }

    public Page<ExercicioDTO> findAll(Pageable pageable) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogado();
        Page<Exercicio> page;
        if (usuario != null) {
            Academia academia = academiaRepository.findByAdminUuid(usuario.getUuid()).orElse(null);
            if (academia != null) {
                page = repository.findByAcademiaIsNullOrAcademiaUuid(academia.getUuid(), pageable);
            } else {
                page = repository.findByAcademiaIsNull(pageable);
            }
        } else {
            page = repository.findByAcademiaIsNull(pageable);
        }
        return page.map(mapper::toDto);
    }
}
