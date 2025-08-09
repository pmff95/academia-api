package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.intinerario.GrupoItinerario;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface GrupoItinerarioRepository extends BaseRepository<GrupoItinerario, Long> {
    Optional<GrupoItinerario> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> projection);

    <T> Optional<T> findByUuidAndItinerarioUuid(UUID uuid, UUID itinerarioUuid, Class<T> projection);

    <T> Page<T> findAllByItinerarioUuid(UUID itinerarioUuid, Pageable pageable, Class<T> projection);

    Optional<GrupoItinerario> findFirstByItinerarioUuidAndAnoLetivo(UUID itinerarioUuid, Integer anoLetivo);
}
