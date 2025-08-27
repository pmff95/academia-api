package com.example.demo.repository;

import com.example.demo.entity.ProdutoFavorito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProdutoFavoritoRepository extends BaseRepository<ProdutoFavorito, UUID> {
    boolean existsByUsuarioUuidAndProdutoUuid(UUID usuarioUuid, UUID produtoUuid);
    void deleteByUsuarioUuidAndProdutoUuid(UUID usuarioUuid, UUID produtoUuid);
    Page<ProdutoFavorito> findByUsuarioUuid(UUID usuarioUuid, Pageable pageable);
}
