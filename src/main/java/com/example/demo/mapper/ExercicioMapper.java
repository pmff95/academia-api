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
        ExercicioDTO dto = mapper.map(exercicio, ExercicioDTO.class);
        if (exercicio.getMaquina() != null) {
            dto.setMaquinaUuid(exercicio.getMaquina().getUuid());
        }
        return dto;
    }

    public Exercicio toEntity(ExercicioDTO dto) {
        return mapper.map(dto, Exercicio.class);
    }
}
