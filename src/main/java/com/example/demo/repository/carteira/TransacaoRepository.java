package com.example.demo.repository.carteira;

import com.example.demo.domain.model.carteira.Transacao;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends BaseRepository<Transacao, Long> {
}
