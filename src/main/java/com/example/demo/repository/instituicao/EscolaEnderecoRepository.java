package com.example.demo.repository.instituicao;

import com.example.demo.domain.model.instituicao.EscolaEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscolaEnderecoRepository extends JpaRepository<EscolaEndereco, Long> {

    Optional<EscolaEndereco> findByEscola_Uuid(UUID escolaId);

    boolean existsByEscola_Uuid(UUID escolaId);
}
