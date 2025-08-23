package com.example.demo.mapper;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.entity.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {
    private final ModelMapper mapper;

    public ProdutoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProdutoDTO toDto(Produto entity) {
        ProdutoDTO dto = mapper.map(entity, ProdutoDTO.class);
        dto.setFornecedorUuid(entity.getFornecedor().getUuid());
        return dto;
    }

    public Produto toEntity(ProdutoDTO dto) {
        return mapper.map(dto, Produto.class);
    }
}

