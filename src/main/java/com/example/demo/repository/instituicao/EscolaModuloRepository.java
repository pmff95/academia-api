package com.example.demo.repository.instituicao;

import com.example.demo.domain.model.instituicao.EscolaModulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EscolaModuloRepository extends JpaRepository<EscolaModulo, Long> {
    List<EscolaModulo> findByEscolaUuid(UUID escolaUuid);

    List<EscolaModulo> findByEscola_Id(Long escolaId);
}
