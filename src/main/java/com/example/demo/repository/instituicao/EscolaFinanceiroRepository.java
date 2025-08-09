package com.example.demo.repository.instituicao;

import com.example.demo.domain.model.instituicao.EscolaFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscolaFinanceiroRepository extends JpaRepository<EscolaFinanceiro, Long> {

    Optional<EscolaFinanceiro> findByEscola_Uuid(UUID escolaId);
}
