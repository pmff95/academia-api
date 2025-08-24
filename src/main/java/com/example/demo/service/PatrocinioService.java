package com.example.demo.service;

import com.example.demo.dto.PatrocinioDTO;
import com.example.demo.entity.Patrocinador;
import com.example.demo.entity.Patrocinio;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.PatrocinioMapper;
import com.example.demo.repository.PatrocinadorRepository;
import com.example.demo.repository.PatrocinioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
public class PatrocinioService {
    private final PatrocinioRepository repository;
    private final PatrocinadorRepository patrocinadorRepository;
    private final PatrocinioMapper mapper;
//    private final CloudflareService cloudflareService;

    public PatrocinioService(PatrocinioRepository repository,
                             PatrocinadorRepository patrocinadorRepository,
                             PatrocinioMapper mapper
//            ,
//                             CloudflareService cloudflareService
    ) {
        this.repository = repository;
        this.patrocinadorRepository = patrocinadorRepository;
        this.mapper = mapper;
//        this.cloudflareService = cloudflareService;
    }

    @Transactional
    public String create(PatrocinioDTO dto, MultipartFile imagem) {
        Patrocinador patrocinador = patrocinadorRepository.findById(dto.getPatrocinadorUuid())
                .orElseThrow(() -> new ApiException("Patrocinador não encontrado"));
        Patrocinio entity = mapper.toEntity(dto);
        entity.setPatrocinador(patrocinador);
//        if (imagem != null && !imagem.isEmpty()) {
//            try {
//                String url = cloudflareService.upload(imagem);
//                entity.setImagemUrl(url);
//            } catch (IOException e) {
//                throw new ApiException("Erro ao enviar imagem");
//            }
//        }
        repository.save(entity);
        return "Patrocínio criado";
    }

    public Page<PatrocinioDTO> findByPatrocinador(UUID patrocinadorUuid, Pageable pageable) {
        return repository.findByPatrocinadorUuid(patrocinadorUuid, pageable)
                .map(mapper::toDto);
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}

