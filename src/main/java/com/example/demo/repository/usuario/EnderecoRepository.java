package com.example.demo.repository.usuario;

import com.example.demo.domain.model.usuario.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> type);

    Optional<Endereco> findByUsuarioId(Long usuarioId);

    <T> Optional<T> findByUsuarioId(Long usuarioId, Class<T> type);
    <T> Optional<T> findByUsuarioUuid(UUID usuarioUuid, Class<T> type);

    boolean existsByUsuarioId(Long usuarioId);
}
