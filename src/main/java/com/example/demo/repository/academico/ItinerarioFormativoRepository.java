package com.example.demo.repository.academico;

import com.example.demo.domain.model.academico.intinerario.ItinerarioFormativo;
import com.example.demo.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ItinerarioFormativoRepository extends BaseRepository<ItinerarioFormativo, Long> {
    Optional<ItinerarioFormativo> findByUuid(UUID uuid);

    <T> Optional<T> findByUuid(UUID uuid, Class<T> type);

}
