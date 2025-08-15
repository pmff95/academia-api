package com.example.demo.mapper;

import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoExercicioDTO;
import com.example.demo.entity.FichaTreino;
import com.example.demo.entity.FichaTreinoExercicio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FichaTreinoMapper {
    private final ModelMapper mapper;

    public FichaTreinoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public FichaTreinoDTO toDto(FichaTreino ficha) {
        FichaTreinoDTO dto = new FichaTreinoDTO();
        dto.setUuid(ficha.getUuid());
        if (ficha.getAluno() != null) {
            dto.setAlunoUuid(ficha.getAluno().getUuid());
        }
        if (ficha.getProfessor() != null) {
            dto.setProfessorUuid(ficha.getProfessor().getUuid());
        }
        dto.setCategoria(ficha.getCategoria());
        dto.setPreset(ficha.isPreset());
        dto.setExercicios(ficha.getExercicios().stream().map(this::toExercicioDto).collect(Collectors.toList()));
        return dto;
    }

    public FichaTreino toEntity(FichaTreinoDTO dto) {
        return mapper.map(dto, FichaTreino.class);
    }

    private FichaTreinoExercicioDTO toExercicioDto(FichaTreinoExercicio exercicio) {
        FichaTreinoExercicioDTO dto = new FichaTreinoExercicioDTO();
        dto.setExercicioUuid(exercicio.getExercicio().getUuid());
        dto.setRepeticoes(exercicio.getRepeticoes());
        dto.setCarga(exercicio.getCarga());
        return dto;
    }
}
