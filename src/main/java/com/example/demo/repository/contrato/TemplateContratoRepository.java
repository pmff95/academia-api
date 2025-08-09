package com.example.demo.repository.contrato;

import com.example.demo.domain.model.contrato.TemplateContrato;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemplateContratoRepository extends BaseRepository<TemplateContrato, Long> {
    Optional<TemplateContrato> findByUuid(UUID uuid);

    Page<TemplateContrato> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
