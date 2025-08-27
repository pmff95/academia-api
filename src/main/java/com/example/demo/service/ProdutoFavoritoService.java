package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.dto.ProdutoDTO;
import com.example.demo.entity.Produto;
import com.example.demo.entity.ProdutoFavorito;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.ProdutoMapper;
import com.example.demo.repository.ProdutoFavoritoRepository;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProdutoFavoritoService {
    private final ProdutoFavoritoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoFavoritoService(ProdutoFavoritoRepository repository,
                                  ProdutoRepository produtoRepository,
                                  UsuarioRepository usuarioRepository,
                                  ProdutoMapper produtoMapper) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoMapper = produtoMapper;
    }

    @Transactional
    public String favoritar(UUID produtoUuid) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null) {
            throw new ApiException("Usuário não autenticado");
        }

        if (repository.existsByUsuarioUuidAndProdutoUuid(usuario.getUuid(), produtoUuid)) {
            return "Produto já favoritado";
        }

        Usuario usuarioEntity = usuarioRepository.findById(usuario.getUuid())
                .orElseThrow(() -> new ApiException("Usuário não encontrado"));
        Produto produto = produtoRepository.findById(produtoUuid)
                .orElseThrow(() -> new ApiException("Produto não encontrado"));

        ProdutoFavorito favorito = new ProdutoFavorito();
        favorito.setUsuario(usuarioEntity);
        favorito.setProduto(produto);
        repository.save(favorito);
        return "Produto favoritado";
    }

    @Transactional
    public String desfavoritar(UUID produtoUuid) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null) {
            throw new ApiException("Usuário não autenticado");
        }
        repository.deleteByUsuarioUuidAndProdutoUuid(usuario.getUuid(), produtoUuid);
        return "Produto removido dos favoritos";
    }

    public Page<ProdutoDTO> listar(Pageable pageable) {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null) {
            throw new ApiException("Usuário não autenticado");
        }

        return repository.findByUsuarioUuid(usuario.getUuid(), pageable)
                .map(f -> produtoMapper.toDto(f.getProduto()));
    }
}
