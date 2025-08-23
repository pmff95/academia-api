package com.example.demo.mapper;

import com.example.demo.dto.PatrocinioDTO;
import com.example.demo.entity.Patrocinio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PatrocinioMapper {
    private final ModelMapper mapper;

    public PatrocinioMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PatrocinioDTO toDto(Patrocinio entity) {
        PatrocinioDTO dto = mapper.map(entity, PatrocinioDTO.class);
        dto.setPatrocinadorUuid(entity.getPatrocinador().getUuid());
        return dto;
    }

    public Patrocinio toEntity(PatrocinioDTO dto) {
        return mapper.map(dto, Patrocinio.class);
    }
}

