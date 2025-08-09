package com.example.demo.mapper;

import com.example.demo.dto.AlunoMedidaDTO;
import com.example.demo.entity.AlunoMedida;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AlunoMedidaMapper {
    private final ModelMapper mapper;

    public AlunoMedidaMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AlunoMedidaDTO toDto(AlunoMedida entity) {
        return mapper.map(entity, AlunoMedidaDTO.class);
    }

    public AlunoMedida toEntity(AlunoMedidaDTO dto) {
        return mapper.map(dto, AlunoMedida.class);
    }
}
