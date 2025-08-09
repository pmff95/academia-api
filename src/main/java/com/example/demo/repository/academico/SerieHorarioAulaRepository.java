package com.example.demo.repository.academico;

import com.example.demo.domain.enums.Serie;
import com.example.demo.domain.model.academico.SerieHorarioAula;
import com.example.demo.repository.BaseRepository;
import com.example.demo.dto.projection.SerieHorarioAulaSummary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieHorarioAulaRepository extends BaseRepository<SerieHorarioAula, Long> {
    List<SerieHorarioAula> findBySerie(Serie serie);

    <T> List<T> findBySerie(Serie serie, Class<T> projection);

    java.util.Optional<SerieHorarioAula> findBySerieAndOrdem(Serie serie, Integer ordem);

    boolean existsBySerie(Serie serie);
}
