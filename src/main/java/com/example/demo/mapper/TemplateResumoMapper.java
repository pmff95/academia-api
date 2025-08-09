package com.example.demo.mapper;

import com.example.demo.domain.model.contrato.TemplateContrato;
import com.example.demo.dto.contrato.TemplateResumoDTO;

public final class TemplateResumoMapper {
    private TemplateResumoMapper() {}

    public static TemplateResumoDTO toDto(TemplateContrato entity) {
        return new TemplateResumoDTO(entity.getUuid(), entity.getNome(), entity.getPlaceholders());
    }
}
