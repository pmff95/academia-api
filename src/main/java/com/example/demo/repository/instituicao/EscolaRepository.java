package com.example.demo.repository.instituicao;

import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.dto.projection.escola.EscolaIdAndName;
import com.example.demo.dto.projection.escola.EscolaView;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscolaRepository extends BaseRepository<Escola, Long> {

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projectionClass);

    Optional<Escola> findByUuid(UUID uuid);

    Optional<Escola> findByCnpj(String cnpj);

    @Query("""
                SELECT e.uuid AS uuid, e.nome AS nome 
                FROM Escola e 
                WHERE e.status = 'ATIVO' 
                ORDER BY e.nome ASC
            """)
    List<EscolaIdAndName> findAllProjected();

    @Query("SELECT e FROM Escola e WHERE e.uuid = :uuid")
    Optional<EscolaView> findEscolaViewByUuid(UUID uuid);
}
