package com.example.demo.mapper;

import com.example.demo.dto.ProfessorDTO;
import com.example.demo.entity.Professor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {
    private final ModelMapper mapper;

    public ProfessorMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProfessorDTO toDto(Professor professor) {
        return mapper.map(professor, ProfessorDTO.class);
    }

    public Professor toEntity(ProfessorDTO dto) {
        return mapper.map(dto, Professor.class);
    }
}
