package com.example.demo.mapper;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.entity.Academia;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AcademiaMapper {
    private final ModelMapper mapper;

    public AcademiaMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AcademiaDTO toDto(Academia academia) {
        return mapper.map(academia, AcademiaDTO.class);
    }

    public Academia toEntity(AcademiaDTO dto) {
        return mapper.map(dto, Academia.class);
    }
}
