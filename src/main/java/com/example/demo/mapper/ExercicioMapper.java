package com.example.demo.mapper;

import com.example.demo.dto.ExercicioDTO;
import com.example.demo.entity.Exercicio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExercicioMapper {
    private final ModelMapper mapper;

    public ExercicioMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ExercicioDTO toDto(Exercicio exercicio) {
        return mapper.map(exercicio, ExercicioDTO.class);
    }

    public Exercicio toEntity(ExercicioDTO dto) {
        return mapper.map(dto, Exercicio.class);
    }
}
