package com.example.demo.repository.parametro;

import com.example.demo.domain.enums.Status;
import com.example.demo.domain.model.parametro.Parametro;
import com.example.demo.domain.enums.TipoParametro;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametroRepository extends BaseRepository<Parametro, Long> {

    Optional<Parametro> findByChaveAndStatus(String chave, Status status);

    List<Parametro> findByStatus(Status status);

    List<Parametro> findByStatusAndTipo(Status status, TipoParametro tipo);
}
