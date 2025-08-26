package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.dto.ProdutoDTO;
import com.example.demo.dto.ProdutoFiltroDTO;
import com.example.demo.entity.Fornecedor;
import com.example.demo.entity.Produto;
import com.example.demo.entity.ProdutoDetalhe;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.ProdutoMapper;
import com.example.demo.repository.FornecedorRepository;
import com.example.demo.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
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
        UsuarioLogado usuarioLogado = SecurityUtils.getUsuarioLogadoDetalhes();

        Fornecedor fornecedor;
        if (usuarioLogado.possuiPerfil(Perfil.FORNECEDOR)) {
            fornecedor = fornecedorRepository.findById(usuarioLogado.getUuid())
                    .orElseThrow(() -> new ApiException("Fornecedor do usuário logado não encontrado"));
        } else {
            fornecedor = fornecedorRepository.findByUuid(dto.getFornecedorUuid())
                    .orElseThrow(() -> new ApiException("Fornecedor não encontrado"));
        }
        Produto entity = mapper.toEntity(dto);
        entity.setFornecedor(fornecedor);
        if (entity.getDetalhe() != null) {
            entity.getDetalhe().forEach(d -> d.setProduto(entity));
        }
        repository.save(entity);
        return "Produto criado";
    }

    public Page<ProdutoDTO> findByFornecedor(UUID fornecedorUuid, Pageable pageable) {
        return repository.findByFornecedorUuid(fornecedorUuid, pageable)
                .map(mapper::toDto);
    }

    public Page<ProdutoDTO> filtrar(ProdutoFiltroDTO filtro, Pageable pageable) {
        Specification<Produto> spec = (root, query, cb) -> {
            query.distinct(true);
            Join<Produto, ProdutoDetalhe> detalhe = root.join("detalhe", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
            }

            if (filtro.getMarcas() != null && !filtro.getMarcas().isEmpty()) {
                predicates.add(root.get("marca").in(filtro.getMarcas()));
            }

            if (filtro.getTamanhos() != null && !filtro.getTamanhos().isEmpty()) {
                predicates.add(detalhe.get("tamanho").in(filtro.getTamanhos()));
            }

            if (filtro.getCores() != null && !filtro.getCores().isEmpty()) {
                predicates.add(detalhe.get("cor").in(filtro.getCores()));
            }

            if (filtro.getSabores() != null && !filtro.getSabores().isEmpty()) {
                predicates.add(detalhe.get("sabor").in(filtro.getSabores()));
            }

            if (filtro.getVolumes() != null && !filtro.getVolumes().isEmpty()) {
                predicates.add(detalhe.get("volume").in(filtro.getVolumes()));
            }

            if (filtro.getPromocao() != null) {
                predicates.add(cb.equal(detalhe.get("promocao"), filtro.getPromocao()));
            }

            if (filtro.getPrecoMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(detalhe.get("preco"), filtro.getPrecoMin()));
            }

            if (filtro.getPrecoMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(detalhe.get("preco"), filtro.getPrecoMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

    public ProdutoDTO findByUuid(UUID uuid) {
        Produto entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Produto não encontrado"));
        return mapper.toDto(entity);
    }

    @Transactional
    public String update(UUID uuid, ProdutoDTO dto) {
        Produto entity = repository.findById(uuid)
                .orElseThrow(() -> new ApiException("Produto não encontrado"));
        Produto mapped = mapper.toEntity(dto);
        entity.setNome(mapped.getNome());
        entity.setDescricao(mapped.getDescricao());
        entity.setTipo(mapped.getTipo());
        entity.setMarca(mapped.getMarca());
        entity.setDetalhe(mapped.getDetalhe());
        entity.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : entity.isAtivo());
        if (entity.getDetalhe() != null) {
            entity.getDetalhe().forEach(d -> d.setProduto(entity));
        }
        repository.save(entity);
        return "Produto atualizado";
    }

    @Transactional
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }
}

