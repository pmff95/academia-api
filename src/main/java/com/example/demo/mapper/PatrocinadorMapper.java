package com.example.demo.mapper;

import com.example.demo.dto.PatrocinadorDTO;
import com.example.demo.entity.Patrocinador;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PatrocinadorMapper {
    private final ModelMapper mapper;

    public PatrocinadorMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PatrocinadorDTO toDto(Patrocinador entity) {
        return mapper.map(entity, PatrocinadorDTO.class);
    }

    public Patrocinador toEntity(PatrocinadorDTO dto) {
        return mapper.map(dto, Patrocinador.class);
    }
}

