package com.example.demo.service;

import com.example.demo.dto.ProdutoDTO;
import com.example.demo.entity.Fornecedor;
import com.example.demo.entity.Produto;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.ProdutoMapper;
import com.example.demo.repository.FornecedorRepository;
import com.example.demo.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoMapper mapper;
//    private final CloudflareService cloudflareService;

    public ProdutoService(ProdutoRepository repository,
                          FornecedorRepository fornecedorRepository,
                          ProdutoMapper mapper
//            ,
//                          CloudflareService cloudflareService
    ) {
        this.repository = repository;
        this.fornecedorRepository = fornecedorRepository;
        this.mapper = mapper;
//        this.cloudflareService = cloudflareService;
    }

    @Transactional
    public String create(ProdutoDTO dto, MultipartFile imagem) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorUuid())
                .orElseThrow(() -> new ApiException("Fornecedor não encontrado"));
        Produto entity = mapper.toEntity(dto);
        entity.setFornecedor(fornecedor);
//        if (imagem != null && !imagem.isEmpty()) {
//            try {
//                String url = cloudflareService.upload(imagem);
//                entity.setImagemUrl(url);
//            } catch (IOException e) {
//                throw new ApiException("Erro ao enviar imagem");
//            }
//        }
        repository.save(entity);
        return "Produto criado";
    }

    public Page<ProdutoDTO> findByFornecedor(UUID fornecedorUuid, Pageable pageable) {
        return repository.findByFornecedorUuid(fornecedorUuid, pageable)
                .map(mapper::toDto);
    }

    public ProdutoDTO findByUuid(UUID uuid) {
        Produto entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Produto não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, ProdutoDTO dto, MultipartFile imagem) {
        Produto entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Produto não encontrado"));
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setEstoque(dto.getEstoque());
        entity.setPreco(dto.getPreco());
        entity.setPrecoDesconto(dto.getPrecoDesconto());
        entity.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : entity.isAtivo());
//        if (imagem != null && !imagem.isEmpty()) {
//            try {
//                String url = cloudflareService.upload(imagem);
//                entity.setImagemUrl(url);
//            } catch (IOException e) {
//                throw new ApiException("Erro ao enviar imagem");
//            }
//        }
        repository.save(entity);
        return "Produto atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}

