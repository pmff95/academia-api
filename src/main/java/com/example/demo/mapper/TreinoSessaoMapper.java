package com.example.demo.mapper;

import com.example.demo.dto.TreinoSessaoDTO;
import com.example.demo.entity.TreinoSessao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TreinoSessaoMapper {
    private final ModelMapper mapper;

    public TreinoSessaoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TreinoSessaoDTO toDto(TreinoSessao entity) {
        TreinoSessaoDTO dto = mapper.map(entity, TreinoSessaoDTO.class);
        dto.setExercicioUuid(entity.getExercicio().getUuid());
        return dto;
    }

    public TreinoSessao toEntity(TreinoSessaoDTO dto) {
        return mapper.map(dto, TreinoSessao.class);
    }
}
