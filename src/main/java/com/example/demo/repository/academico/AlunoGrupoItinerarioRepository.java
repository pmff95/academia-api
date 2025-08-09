package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.intinerario.AlunoGrupoItinerario;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AlunoGrupoItinerarioRepository extends BaseRepository<AlunoGrupoItinerario, Long> {
    Optional<AlunoGrupoItinerario> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    <T> Optional<T> findByUuidAndGrupoUuid(UUID uuid, UUID grupoUuid, Class<T> projection);

    <T> Page<T> findAllByGrupoUuid(UUID grupoUuid, Pageable pageable, Class<T> projection);
}
