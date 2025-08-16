package com.example.demo.repository;

import com.example.demo.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
    List<Notificacao> findByDestinatarioUuidOrderByDataCriacaoDesc(UUID uuid);
}
