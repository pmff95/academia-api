package com.example.demo.repository.vendas;

import com.example.demo.domain.model.vendas.Pagamento;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends BaseRepository<Pagamento, Long> {
    Optional<Pagamento> findByUuid(UUID uuid);
}
