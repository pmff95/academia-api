package com.example.demo.repository.parametro;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.domain.model.parametro.ParametroEscola;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

@Repository
public interface ParametroEscolaRepository extends BaseRepository<ParametroEscola, Long> {

    Optional<ParametroEscola> findByEscola_UuidAndParametro_Chave(UUID escolaUuid, String chave);

    List<ParametroEscola> findByEscola_Uuid(UUID escolaUuid);
    Optional<ParametroEscola> findByEscola_UuidAndStatusAndParametro_Chave(UUID escolaUuid, Status status, String chave);
    List<ParametroEscola> findByParametro_TipoAndEscola_Uuid(TipoParametro tipo, UUID escolaUuid);

    List<ParametroEscola> findByParametro_Chave(String chave);
}
