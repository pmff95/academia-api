package com.example.demo.mapper;

import com.example.demo.dto.AlimentoDTO;
import com.example.demo.entity.Alimento;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AlimentoMapper {
    private final ModelMapper mapper;

    public AlimentoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public AlimentoDTO toDto(Alimento entity) {
        return mapper.map(entity, AlimentoDTO.class);
    }

    public Alimento toEntity(AlimentoDTO dto) {
        return mapper.map(dto, Alimento.class);
    }
}
