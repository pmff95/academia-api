package com.example.demo.repository.instituicao;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.instituicao.ConfiguracaoSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfiguracaoSerieRepository extends JpaRepository<ConfiguracaoSerie, Long> {

    Optional<ConfiguracaoSerie> findByEscola_UuidAndSerie(UUID escolaId, Serie serie);

    List<ConfiguracaoSerie> findByEscola_Uuid(UUID escolaId);

    boolean existsByEscola_Uuid(UUID escolaId);

}
