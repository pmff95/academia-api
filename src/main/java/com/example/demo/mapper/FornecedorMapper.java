package com.example.demo.mapper;

import com.example.demo.dto.FornecedorDTO;
import com.example.demo.entity.Fornecedor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper {
    private final ModelMapper mapper;

    public FornecedorMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public FornecedorDTO toDto(Fornecedor fornecedor) {
        return mapper.map(fornecedor, FornecedorDTO.class);
    }

    public Fornecedor toEntity(FornecedorDTO dto) {
        return mapper.map(dto, Fornecedor.class);
    }
}

